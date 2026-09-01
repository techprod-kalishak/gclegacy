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

    public static final BlockFamily ASTEROID_ROCKS = new BlockFamily.Builder(GalacticraftBlocks.ASTEROID_ROCK.get())
            .stairs(GalacticraftBlocks.ASTEROID_ROCK_STAIRS.get())
            .slab(GalacticraftBlocks.ASTEROID_ROCK_SLAB.get())
            .wall(GalacticraftBlocks.ASTEROID_ROCK_WALL.get())
            .generateStonecutterRecipe()
            .recipeUnlockedBy("has_asteroid_rock")
            .recipeGroupPrefix("asteroid_rock")
            .getFamily();
    public static final BlockFamily MARS_STONE = new BlockFamily.Builder(GalacticraftBlocks.MARS_STONE.get())
            .stairs(GalacticraftBlocks.MARS_STONE_STAIRS.get())
            .slab(GalacticraftBlocks.MARS_STONE_SLAB.get())
            .pressurePlate(GalacticraftBlocks.MARS_STONE_PRESSURE_PLATE.get())
            .button(GalacticraftBlocks.MARS_STONE_BUTTON.get())
            .generateStonecutterRecipe()
            .recipeUnlockedBy("has_mars_stone")
            .recipeGroupPrefix("mars_stone")
            .getFamily();
    public static final BlockFamily MOON_BRICKS = new BlockFamily.Builder(GalacticraftBlocks.MOON_BRICKS.get())
            .stairs(GalacticraftBlocks.MOON_BRICK_STAIRS.get())
            .slab(GalacticraftBlocks.MOON_BRICK_SLAB.get())
            .wall(GalacticraftBlocks.MOON_BRICK_WALL.get())
            .generateStonecutterRecipe()
            .recipeUnlockedBy("has_moon_bricks")
            .recipeGroupPrefix("moon_bricks")
            .getFamily();
    public static final BlockFamily TIN_DECORATION = new BlockFamily.Builder(GalacticraftBlocks.TIN_DECORATION_BLOCK.get())
            .stairs(GalacticraftBlocks.TIN_DECORATION_STAIRS.get())
            .slab(GalacticraftBlocks.TIN_DECORATION_SLAB.get())
            .wall(GalacticraftBlocks.TIN_DECORATION_WALL.get())
            .recipeUnlockedBy("has_tin")
            .recipeGroupPrefix("tin_decoration")
            .getFamily();
    public static final BlockFamily TIN_WALL_DECORATION = new BlockFamily.Builder(GalacticraftBlocks.TIN_WALL_DECORATION_BLOCK.get())
            .stairs(GalacticraftBlocks.TIN_WALL_DECORATION_STAIRS.get())
            .slab(GalacticraftBlocks.TIN_WALL_DECORATION_SLAB.get())
            .recipeUnlockedBy("has_tin")
            .recipeGroupPrefix("tin_wall_decoration")
            .dontGenerateModel()
            .getFamily();
    public static final BlockFamily MARS_BRICKS = new BlockFamily.Builder(GalacticraftBlocks.MARS_BRICKS.get())
            .stairs(GalacticraftBlocks.MARS_BRICK_STAIRS.get())
            .slab(GalacticraftBlocks.MARS_BRICK_SLAB.get())
            .wall(GalacticraftBlocks.MARS_BRICK_WALL.get())
            .generateStonecutterRecipe()
            .recipeUnlockedBy("has_mars_bricks")
            .recipeGroupPrefix("mars_bricks")
            .getFamily();

    public static Stream<BlockFamily> getFamilies() {
        return Stream.of(MOON_BRICKS, MARS_STONE, MARS_BRICKS, ASTEROID_ROCKS, TIN_DECORATION, TIN_WALL_DECORATION);
    }
}
