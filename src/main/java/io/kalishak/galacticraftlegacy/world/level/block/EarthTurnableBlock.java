/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

public interface EarthTurnableBlock {
    @Nullable BlockState getTerraformedBlock(BlockGetter level, BlockPos blockPos);

    static boolean adjacentToOrNearWater(BlockGetter level, BlockPos blockPos, TagKey<Block> block) {
        for (Direction face : Direction.values()) {
            BlockState nextTo = level.getBlockState(blockPos.relative(face));

            if (nextTo.getBlock() instanceof SimpleWaterloggedBlock) {
                return nextTo.getValue(BlockStateProperties.WATERLOGGED);
            } else if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
                return true;
            }

            if (nextTo.is(block)) return true;
        }

        return false;
    }
}
