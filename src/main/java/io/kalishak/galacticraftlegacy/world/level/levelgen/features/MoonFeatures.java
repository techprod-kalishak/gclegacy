/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.levelgen.CraterSize;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class MoonFeatures {
    public static final ResourceKey<Feature> FALLEN_METEOR = GalacticraftFeatures.key("moon_fallen_meteor");
    public static final ResourceKey<Feature> MOON_CRATER = GalacticraftFeatures.key("moon_crater");
    public static final ResourceKey<Feature> MOON_CRATER_LARGE = GalacticraftFeatures.key("moon_crater_large");
    public static final ResourceKey<Feature> ORE_MOON_COPPER = GalacticraftFeatures.key("moon_copper_ore");
    public static final ResourceKey<Feature> ORE_MOON_TIN = GalacticraftFeatures.key("moon_tin_ore");
    public static final ResourceKey<Feature> ORE_MOON_CHEESE = GalacticraftFeatures.key("moon_cheese_ore");
    public static final ResourceKey<Feature> ORE_MOON_SAPPHIRE = GalacticraftFeatures.key("moon_sapphire_ore");
    public static final ResourceKey<Feature> DIRT_PATCH = GalacticraftFeatures.key("moon_dirt_patch");

    static void bootstrap(BootstrapContext<Feature> cxt) {
        RuleTest moonRockReplaceable = new BlockStateMatchTest(GalacticraftBlocks.MOON_ROCK.get().defaultBlockState());

        GalacticraftFeatures.craterSimple(
                cxt,
                MOON_CRATER,
                CraterSize.SMALL,
                20
        );
        GalacticraftFeatures.craterSimple(
                cxt,
                MOON_CRATER_LARGE,
                CraterSize.LARGE,
                8
        );

        cxt.register(
                FALLEN_METEOR,
                FallenMeteorFeature.DEFAULT
        );

        cxt.register(
                ORE_MOON_COPPER,
                new OreFeature(moonRockReplaceable, GalacticraftBlocks.MOON_COPPER_ORE.get().defaultBlockState(), 4)
        );
        cxt.register(
                ORE_MOON_TIN,
                new OreFeature(moonRockReplaceable, GalacticraftBlocks.MOON_TIN_ORE.get().defaultBlockState(), 4)
        );
        cxt.register(
                ORE_MOON_CHEESE,
                new OreFeature(moonRockReplaceable, GalacticraftBlocks.MOON_CHEESE_ORE.get().defaultBlockState(), 3)
        );
        cxt.register(
                ORE_MOON_SAPPHIRE,
                new OreFeature(moonRockReplaceable, GalacticraftBlocks.MOON_SAPPHIRE_ORE.get().defaultBlockState(), 6)
        );
        cxt.register(
                DIRT_PATCH,
                new OreFeature(moonRockReplaceable, Blocks.DIRT.defaultBlockState(), 32)
        );
    }
}
