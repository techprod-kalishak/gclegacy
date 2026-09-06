package io.kalishak.galacticraftlegacy.world.level.redstone;

import io.kalishak.galacticraftlegacy.world.level.block.SealedRedstoneWireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

public abstract class SealedRedstoneEvaluator {
    protected final SealedRedstoneWireBlock sealedWireBlock;

    protected SealedRedstoneEvaluator(SealedRedstoneWireBlock sealedWireBlock) {
        this.sealedWireBlock = sealedWireBlock;
    }

    public abstract void updatePowerStrength(Level level, BlockPos pos, BlockState state, @Nullable Orientation orientation, boolean includeVerticalSides);

    protected int getBlockSignal(Level level, BlockPos pos) {
        return this.sealedWireBlock.getBlockSignal(level, pos);
    }

    protected int getWireSignal(BlockPos pos, BlockState state) {
        return state.is(this.sealedWireBlock) ? state.getValue(RedStoneWireBlock.POWER) : 0;
    }

    protected int getIncomingWireSignal(Level level, BlockPos pos, boolean includeVerticalSides) {
        int wireSignal = 0;
        for (Direction direction : Direction.values()) {
            if (!includeVerticalSides && Direction.Plane.VERTICAL.test(direction)) {
                continue;
            }

            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            wireSignal = Math.max(wireSignal, getWireSignal(neighborPos, neighborState));
            BlockPos abovePos = pos.above();

            if (neighborState.isRedstoneConductor(level, neighborPos) && !level.getBlockState(abovePos).isRedstoneConductor(level, abovePos)) {
                BlockPos aboveNeighborPos = neighborPos.above();
                wireSignal = Math.max(wireSignal, getWireSignal(aboveNeighborPos, level.getBlockState(aboveNeighborPos)));
            } else if (!neighborState.isRedstoneConductor(level, neighborPos)) {
                BlockPos belowNeighborPos = neighborPos.below();
                wireSignal = Math.max(wireSignal, getWireSignal(belowNeighborPos, level.getBlockState(belowNeighborPos)));
            }
        }

        return Math.max(0, wireSignal - 1);
    }
}
