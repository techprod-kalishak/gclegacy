package io.kalishak.galacticraftlegacy.world.level.redstone;

import com.google.common.collect.Sets;
import io.kalishak.galacticraftlegacy.world.level.block.SealedRedstoneWireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public class DefaultSealedRedstoneEvaluator extends SealedRedstoneEvaluator {
    public DefaultSealedRedstoneEvaluator(SealedRedstoneWireBlock sealedWireBlock) {
        super(sealedWireBlock);
    }

    @Override
    public void updatePowerStrength(Level level, BlockPos pos, BlockState state, @Nullable Orientation orientation, boolean includeVerticalSides) {
        int targetStrength = calculateTargetStrength(level, pos, includeVerticalSides);

        if (state.getValue(SealedRedstoneWireBlock.POWER) != targetStrength) {
            if (level.getBlockState(pos) == state) {
                level.setBlock(pos, state.setValue(SealedRedstoneWireBlock.POWER, targetStrength), 2);
            }

            Set<BlockPos> toUpdate = Sets.newHashSet();
            toUpdate.add(pos);

            for (Direction direction : Direction.values()) {
                toUpdate.add(pos.relative(direction));
            }

            for (BlockPos blockPos : toUpdate) {
                level.updateNeighborsAt(blockPos, this.sealedWireBlock);
            }
        }
    }

    private int calculateTargetStrength(Level level, BlockPos pos, boolean includeVerticalSides) {
        int blockSignal = this.getBlockSignal(level, pos);
        return blockSignal == 15 ? blockSignal : Math.max(blockSignal, getIncomingWireSignal(level, pos, includeVerticalSides));
    }
}
