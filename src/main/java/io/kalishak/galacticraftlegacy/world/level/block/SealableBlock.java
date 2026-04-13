package io.kalishak.galacticraftlegacy.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

public interface SealableBlock {
    boolean isSealed(BlockGetter level, BlockPos pos, Direction facing);
}
