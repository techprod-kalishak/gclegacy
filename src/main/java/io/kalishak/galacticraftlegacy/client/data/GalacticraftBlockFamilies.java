package io.kalishak.galacticraftlegacy.client.data;

import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.data.BlockFamily;

import java.util.stream.Stream;

public class GalacticraftBlockFamilies {
    public static final BlockFamily MOON_BRICKS = new BlockFamily.Builder(GalacticraftBlocks.MOON_BRICKS.get())
            .stairs(GalacticraftBlocks.MOON_BRICK_STAIRS.get())
            .slab(GalacticraftBlocks.MOON_BRICK_SLAB.get())
            .wall(GalacticraftBlocks.MOON_BRICK_WALL.get())
            .recipeUnlockedBy("has_moon_bricks")
            .recipeGroupPrefix("moon_bricks")
            .getFamily();

    public static Stream<BlockFamily> getFamilies() {
        return Stream.of(MOON_BRICKS);
    }
}
