/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.HotItem;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import io.kalishak.galacticraftlegacy.world.level.block.entity.FallenMeteorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class FallenMeteorBlock extends FallingBlock implements EntityBlock, SimpleWaterloggedBlock {
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

            return to32BitColor(255, (int) Math.min(255, col.x), (int) Math.min(255, col.y), (int) Math.min(255, col.z));
        }

        return 16777215;
    }

    public static int to32BitColor(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    @Override
    public int getDustColor(@NonNull BlockState blockState, @NonNull BlockGetter level, @NonNull BlockPos pos) {
        return colorMultiplier(level, pos);
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
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
    public void setPlacedBy(@NonNull Level level, @NonNull BlockPos pos, @NonNull BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        HotContent content = itemStack.get(GalacticraftDataComponents.HOT_CONTENT);

        if (content != null) {
            level.getBlockEntity(pos, GalacticraftBlockEntityTypes.FALLEN_METEOR.get()).ifPresent(fallenMeteor -> fallenMeteor.setData(GalacticraftAttachments.HOT_CONTENT, content));
        }
    }

    @Override
    public void onBrokenAfterFall(@NonNull Level level, @NonNull BlockPos pos, FallingBlockEntity entity) {
        entity.getExistingData(GalacticraftAttachments.HOT_CONTENT)
                        .ifPresent(hotContent -> {
                            if (hotContent.getScaledHeatLevel() > 0.5F && entity.getStartPos().getY() - 150 > pos.getY()) {
                                entity.setPermanentlyInvulnerable(true);
                                level.explode(entity, pos.getX(), pos.getY(), pos.getZ(), 1.0F, Level.ExplosionInteraction.BLOCK);
                            }
                        });
    }

    @Override
    protected void entityInside(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Entity entity, @NonNull InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (level instanceof ServerLevel serverLevel) {
            HotItem.blockHurt(serverLevel, pos, entity);
        }
    }

    @Override
    protected @NonNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected @NonNull BlockState updateShape(BlockState state, @NonNull LevelReader level, @NonNull ScheduledTickAccess ticks, @NonNull BlockPos pos, @NonNull Direction directionToNeighbour, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public int getExpDrop(@NonNull BlockState state, LevelAccessor level, @NonNull BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, @NonNull ItemStack tool) {
        return EXPERIENCE.sample(level.getRandom());
    }

    @Override
    public @NonNull ItemStack getCloneItemStack(@NonNull LevelReader level, @NonNull BlockPos pos, @NonNull BlockState state, boolean includeData, @NonNull Player player) {
        ItemStack result = super.getCloneItemStack(level, pos, state, includeData, player);

        if (includeData) {
            level.getBlockEntity(pos, GalacticraftBlockEntityTypes.FALLEN_METEOR.get()).ifPresent(meteor -> result.set(GalacticraftDataComponents.HOT_CONTENT, meteor.getData(GalacticraftAttachments.HOT_CONTENT)));
        }

        return result;
    }

    @Override
    protected boolean triggerEvent(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, int b0, int b1) {
        super.triggerEvent(state, level, pos, b0, b1);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(b0, b1);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new FallenMeteorBlockEntity(worldPosition, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel
                ? createTickerHelper(type, GalacticraftBlockEntityTypes.FALLEN_METEOR.get(), (_, pos, state, entity) -> FallenMeteorBlockEntity.serverTick(serverLevel, pos, state, entity))
                : null;
    }

    @SuppressWarnings("unchecked")
    static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker) {
        return expected == actual ? (BlockEntityTicker<A>) ticker : null;
    }
}
