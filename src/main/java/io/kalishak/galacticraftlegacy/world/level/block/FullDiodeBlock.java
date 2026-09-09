/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.ticks.TickPriority;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;

public abstract class FullDiodeBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public FullDiodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected abstract MapCodec<? extends FullDiodeBlock> codec();

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isLocked(level, pos, state)) return;

        boolean on = state.getValue(POWERED);
        boolean shouldTurnOn = shouldTurnOn(level, pos, state);

        if (on && !shouldTurnOn) {
            level.setBlock(pos, state.setValue(POWERED, false), FullDiodeBlock.UPDATE_CLIENTS);
        } else if (!on) {
            level.setBlock(pos, state.setValue(POWERED, true), FullDiodeBlock.UPDATE_CLIENTS);

            if (!shouldTurnOn) {
                level.scheduleTick(pos, this, getDelay(state), TickPriority.VERY_HIGH);
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.getBlockState(pos).is(this)) {
            if (state.canSurvive(level, pos)) {
                checkTickOnNeighbor(level, pos, state);
                return;
            }

            BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            dropResources(state, level, pos, blockEntity);
            level.removeBlock(pos, false);

            for(Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        if (isLocked(level, pos, state)) return;

        boolean on = state.getValue(POWERED);
        boolean shouldTurnOn = shouldTurnOn(level, pos, state);

        if (on != shouldTurnOn && !level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority priority = TickPriority.HIGH;

            if (shouldPrioritize(level, pos, state)) {
                priority = TickPriority.EXTREMELY_HIGH;
            } else if (on) {
                priority = TickPriority.VERY_HIGH;
            }

            level.scheduleTick(pos, this, getDelay(state), priority);
        }
    }

    public boolean isLocked(LevelReader level, BlockPos pos, BlockState state) {
        return false;
    }

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        return getInputSignal(level, pos, state) > 0;
    }

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos targetPos = pos.relative(direction);
        int input = level.getSignal(targetPos, direction);

        if (input >= 15) {
            return input;
        }

        BlockState targetBlockState = level.getBlockState(targetPos);
        boolean isTargetWire = targetBlockState.is(Blocks.REDSTONE_WIRE) || targetBlockState.is(GalacticraftBlocks.SEALED_REDSTONE_WIRE);

        return Math.max(input, isTargetWire ? targetBlockState.getValue(RedStoneWireBlock.POWER) : 0);
    }

    protected int getAlternateSignal(SignalGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        Direction clockWise = direction.getClockWise();
        Direction counterClockWise = direction.getCounterClockWise();

        boolean sideInputDiodesOnly = sideInputDiodesOnly();

        return Math.max(level.getControlInputSignal(pos.relative(clockWise), clockWise, sideInputDiodesOnly), level.getControlInputSignal(pos.relative(counterClockWise), counterClockWise, sideInputDiodesOnly));
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(POWERED) ? getOutputSignal(level, pos, state) : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getSignal(level, pos, direction);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(FACING) == direction ? ownSignal(state, level, pos) : 0;
    }

    @Override
    public @NonNull BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        if (shouldTurnOn(level, pos, state)) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        updateNeighborsInFront(level, pos, state);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        if (!movedByPiston) {
            updateNeighborsInFront(level, pos, state);
        }
    }

    protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos oppositePos = pos.relative(direction.getOpposite());

        if (!EventHooks.onNeighborNotify(level, pos, level.getBlockState(pos), EnumSet.of(direction.getOpposite()), false).isCanceled()) {
            Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, direction.getOpposite(), Direction.UP);
            level.neighborChanged(oppositePos, this, orientation);
            level.updateNeighborsAtExceptFromFacing(oppositePos, this, direction, orientation);
        }
    }

    protected boolean sideInputDiodesOnly() {
        return false;
    }

    protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
        return 15;
    }

    public static boolean isDiode(BlockState state) {
        return state.getBlock() instanceof DiodeBlock || state.getBlock() instanceof FullDiodeBlock;
    }

    public boolean shouldPrioritize(BlockGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockState oppositeState = level.getBlockState(pos.relative(direction));
        return isDiode(oppositeState) && oppositeState.getValue(FACING) != direction;
    }

    protected abstract int getDelay(BlockState state);
}
