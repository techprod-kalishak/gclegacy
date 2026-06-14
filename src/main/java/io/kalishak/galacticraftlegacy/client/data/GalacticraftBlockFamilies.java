/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

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
    public static final BlockFamily ASTEROID_ROCKS = new BlockFamily.Builder(GalacticraftBlocks.ASTEROID_ROCK.get())
            .stairs(GalacticraftBlocks.ASTEROID_ROCK_STAIRS.get())
            .slab(GalacticraftBlocks.ASTEROID_ROCK_SLAB.get())
            .wall(GalacticraftBlocks.ASTEROID_ROCK_WALL.get())
            .recipeUnlockedBy("has_asteroid_rock")
            .recipeGroupPrefix("asteroid_rock")
            .getFamily();
    public static final BlockFamily TIN_DECORATION = new BlockFamily.Builder(GalacticraftBlocks.TIN_DECORATION_BLOCK.get())
            .stairs(GalacticraftBlocks.TIN_DECORATION_STAIRS.get())
            .slab(GalacticraftBlocks.TIN_DECORATION_SLAB.get())
            .wall(GalacticraftBlocks.TIN_DECORATION_WALL.get())
            .recipeUnlockedBy("has_tin")
            .recipeGroupPrefix("tin_decoration")
            .getFamily();

    public static Stream<BlockFamily> getFamilies() {
        return Stream.of(MOON_BRICKS, ASTEROID_ROCKS, TIN_DECORATION);
    }
}
