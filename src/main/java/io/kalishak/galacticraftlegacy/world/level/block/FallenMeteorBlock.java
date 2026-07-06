/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.world.item.HotItem;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import io.kalishak.galacticraftlegacy.world.level.block.entity.FallenMeteorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class FallenMeteorBlock extends FallingBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final MapCodec<FallenMeteorBlock> CODEC = simpleCodec(FallenMeteorBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Shapes.box(0.15, 0.05, 0.15, 0.85, 0.75, 0.85);
    private static final IntProvider EXPERIENCE = UniformInt.of(3, 7);

    public FallenMeteorBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    public static int colorMultiplier(BlockGetter level, BlockPos pos) {
        BlockEntity tile = level.getBlockEntity(pos);

        if (tile instanceof FallenMeteorBlockEntity meteor) {

            Vec3 col = new Vec3(198, 108, 58);
            col.add(200 - meteor.getScaledHeatLevel() * 200);

            return to32BitColor(255, (byte) Math.min(255, col.x), (byte) Math.min(255, col.y), (byte) Math.min(255, col.z));
        }

        return 16777215;
    }

    public static int to32BitColor(int a, int r, int g, int b) {
        a = a << 24;
        r = r << 16;
        g = g << 8;

        return a | r | g | b;
    }

    @Override
    protected MapCodec<FallenMeteorBlock> codec() {
        return CODEC;
    }

    @Override
    public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
        return colorMultiplier(level, pos);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(pos);

        return defaultBlockState().setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        HotContent content = itemStack.get(GalacticraftDataComponents.HOT_CONTENT);

        if (content != null) {
            level.getBlockEntity(pos, GalacticraftBlockEntityType.FALLEN_METEOR.get()).ifPresent(fallenMeteor -> fallenMeteor.setData(GalacticraftAttachments.HOT_CONTENT, content));
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
        entity.getExistingData(GalacticraftAttachments.HOT_CONTENT)
                        .ifPresent(hotContent -> {
                            if (hotContent.getScaledHeatLevel() > 0.5F && entity.getStartPos().getY() - 150 > pos.getY()) {
                                entity.setInvulnerable(true);
                                level.explode(entity, pos.getX(), pos.getY(), pos.getZ(), 1.0F, Level.ExplosionInteraction.BLOCK);
                            }
                        });
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (level instanceof ServerLevel serverLevel) {
            HotItem.blockHurt(serverLevel, pos, entity);
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemStack tool) {
        return EXPERIENCE.sample(level.getRandom());
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack result = super.getCloneItemStack(level, pos, state, includeData, player);

        if (includeData) {
            level.getBlockEntity(pos, GalacticraftBlockEntityType.FALLEN_METEOR.get()).ifPresent(meteor -> result.set(GalacticraftDataComponents.HOT_CONTENT, meteor.getData(GalacticraftAttachments.HOT_CONTENT)));
        }

        return result;
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
        super.triggerEvent(state, level, pos, b0, b1);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(b0, b1);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new FallenMeteorBlockEntity(worldPosition, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel
                ? createTickerHelper(type, GalacticraftBlockEntityType.FALLEN_METEOR.get(), (_, pos, state, entity) -> FallenMeteorBlockEntity.serverTick(serverLevel, pos, state, entity))
                : null;
    }

    @SuppressWarnings("unchecked")
    static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker) {
        return expected == actual ? (BlockEntityTicker<A>) ticker : null;
    }
}
