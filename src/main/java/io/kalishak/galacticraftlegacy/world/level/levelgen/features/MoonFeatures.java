/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.levelgen.CraterSize;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations.CraterConfiguration;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations.FallenMeteorConfiguration;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class MoonFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_METEOR = GalacticraftFeatures.key("moon_fallen_meteor");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_CRATER = GalacticraftFeatures.key("moon_crater");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_CRATER_LARGE = GalacticraftFeatures.key("moon_crater_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MOON_COPPER = GalacticraftFeatures.key("ore_moon_copper");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MOON_COPPER_SMALL = GalacticraftFeatures.key("ore_moon_copper_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MOON_TIN = GalacticraftFeatures.key("ore_moon_tin");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MOON_CHEESE = GalacticraftFeatures.key("ore_moon_cheese");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MOON_SAPPHIRE = GalacticraftFeatures.key("ore_moon_sapphire");

    static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> cxt) {
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

        FeatureUtils.register(
                cxt,
                FALLEN_METEOR,
                GalacticraftFeatures.FALLEN_METEOR.get(),
                FallenMeteorConfiguration.DEFAULT
        );

        FeatureUtils.register(
                cxt,
                ORE_MOON_COPPER,
                Feature.ORE,
                new OreConfiguration(moonRockReplaceable, GalacticraftBlocks.MOON_COPPER_ORE.get().defaultBlockState(), 8)
        );
        FeatureUtils.register(
                cxt,
                ORE_MOON_COPPER_SMALL,
                Feature.ORE,
                new OreConfiguration(moonRockReplaceable, GalacticraftBlocks.MOON_COPPER_ORE.get().defaultBlockState(), 4)
        );
        FeatureUtils.register(
                cxt,
                ORE_MOON_TIN,
                Feature.ORE,
                new OreConfiguration(moonRockReplaceable, GalacticraftBlocks.MOON_TIN_ORE.get().defaultBlockState(), 9)
        );
        FeatureUtils.register(
                cxt,
                ORE_MOON_CHEESE,
                Feature.ORE,
                new OreConfiguration(moonRockReplaceable, GalacticraftBlocks.MOON_CHEESE_ORE.get().defaultBlockState(), 10)
        );
        FeatureUtils.register(
                cxt,
                ORE_MOON_SAPPHIRE,
                Feature.ORE,
                new OreConfiguration(moonRockReplaceable, GalacticraftBlocks.MOON_SAPPHIRE_ORE.get().defaultBlockState(), 4)
        );
    }
}
