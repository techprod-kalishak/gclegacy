package io.kalishak.galacticraftlegacy.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockWithDummies {
    default void setDummyBlock(Level level, BlockPos blockPos, BlockState blockState) {
        level.setBlockAndUpdate(blockPos, blockState);
    }

    default void destroyDummyBlock(Level level, Holder<Block> rawType, BlockPos blockPos, Player player) {
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.is(rawType)) {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 35);
            level.levelEvent(player, LevelEvent.PARTICLES_AND_SOUND_DESTROY_BLOCK, blockPos, Block.getId(blockState));
        }
    }
}
