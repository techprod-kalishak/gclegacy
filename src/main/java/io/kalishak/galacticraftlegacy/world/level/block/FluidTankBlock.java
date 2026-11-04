/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.block.entity.FluidTankBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class FluidTankBlock extends BaseEntityBlock {
    public static final BooleanProperty DOWN_CONNECTION = BooleanProperty.create("down_connection");
    public static final BooleanProperty UP_CONNECTION = BooleanProperty.create("up_connection");
    private static final VoxelShape SHAPE = Shapes.box(0.05F, 0.0F, 0.05F, 0.95F, 1.0F, 0.95F);

    public FluidTankBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(DOWN_CONNECTION, false).setValue(UP_CONNECTION, false));
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hidesNeighborFace(@NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull BlockState state, @NonNull BlockState neighborState, Direction dir) {
        if (dir.getAxis().isHorizontal()) {
            return false;
        }

        BlockState relativeState = level.getBlockState(pos.relative(dir));

        if (relativeState.is(this)) {
            return dir.getAxis().isVertical();
        }

        return super.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    protected boolean skipRendering(@NonNull BlockState state, BlockState neighborState, @NonNull Direction direction) {
        return neighborState.is(this);
    }

    @Override
    protected @NonNull VoxelShape getVisualShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected float getShadeBrightness(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(@NonNull BlockState state) {
        return true;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos ownPos = context.getClickedPos();
        BlockState ownState = defaultBlockState();

        BlockState stateAbove = level.getBlockState(ownPos.above());
        if (stateAbove.is(this)) {
            ownState = ownState.setValue(UP_CONNECTION, true);
        }

        BlockState stateBelow = level.getBlockState(ownPos.below());
        if (stateBelow.is(this)) {
            ownState = ownState.setValue(DOWN_CONNECTION, true);
        }

        return ownState;
    }

    @Override
    protected @NonNull BlockState updateShape(@NonNull BlockState state, LevelReader level, @NonNull ScheduledTickAccess ticks, BlockPos pos, @NonNull Direction directionToNeighbour, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        BlockState stateAbove = level.getBlockState(pos.above());
        if (stateAbove.isAir()) {
            state = state.setValue(UP_CONNECTION, false);
        } else if (stateAbove.is(this)) {
            state = state.setValue(UP_CONNECTION, true);
        }

        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.isAir()) {
            state = state.setValue(DOWN_CONNECTION, false);
        } else if (stateBelow.is(this)) {
            state = state.setValue(DOWN_CONNECTION, true);
        }

        return state;
    }

    @Override
    protected @NonNull InteractionResult useItemOn(ItemStack itemStack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        if (itemStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));

        if (fluidHandler != null) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof FluidTankBlockEntity tank) {
                return tank.interactWithItem(fluidHandler, level, pos, player);
            }
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @NonNull ItemStack getCloneItemStack(@NonNull LevelReader level, @NonNull BlockPos pos, @NonNull BlockState state, boolean includeData, @NonNull Player player) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, includeData, player);

        if (includeData) {
            level.getBlockEntity(
                    pos,
                    GalacticraftBlockEntityTypes.FLUID_TANK.get()
            ).ifPresent(tank -> stack.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(tank.getFluidStack())));
        }

        return stack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP_CONNECTION, DOWN_CONNECTION);
    }

    @Override
    public BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new FluidTankBlockEntity(blockPos, blockState);
    }
}
