package io.kalishak.galacticraftlegacy.client;

import io.kalishak.galacticraftlegacy.world.level.block.FallenMeteorBlock;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GalacticraftBlockTintSources {
    public static BlockTintSource meteor() {
        return new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return 16777215;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return FallenMeteorBlock.colorMultiplier(level, pos);
            }
        };
    }
}
