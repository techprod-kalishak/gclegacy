/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations.CrudeOilPoolConfiguration;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class OverworldFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ALUMINUM = GalacticraftFeatures.key("ore_aluminum");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ALUMINUM_SMALL = GalacticraftFeatures.key("ore_aluminum_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TIN = GalacticraftFeatures.key("ore_tin");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TIN_SMALL = GalacticraftFeatures.key("ore_tin_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILICON = GalacticraftFeatures.key("ore_silicon");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILICON_BURIED = GalacticraftFeatures.key("ore_silicon_buried");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CRUDE_OIL_FOUNTAIN = GalacticraftFeatures.key("crude_oil_fountain");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CRUDE_OIL_FOUNTAIN_SMALL = GalacticraftFeatures.key("crude_oil_small_fountain");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CRUDE_OIL = GalacticraftFeatures.key("ore_crude_oil");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CRUDE_OIL_MAJOR = GalacticraftFeatures.key("ore_crude_oil_major");

    static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> cxt) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> aluminumOres = List.of(
                OreConfiguration.target(stoneReplaceable, GalacticraftBlocks.ALUMINUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE.get().defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> tinOres = List.of(
                OreConfiguration.target(stoneReplaceable, GalacticraftBlocks.TIN_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, GalacticraftBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> siliconOres = List.of(
                OreConfiguration.target(stoneReplaceable, GalacticraftBlocks.SILICON_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, GalacticraftBlocks.DEEPSLATE_SILICON_ORE.get().defaultBlockState())
        );

        FeatureUtils.register(
                cxt,
                ORE_ALUMINUM,
                Feature.ORE,
                new OreConfiguration(aluminumOres, 8)
        );
        FeatureUtils.register(
                cxt,
                ORE_ALUMINUM_SMALL,
                Feature.ORE,
                new OreConfiguration(aluminumOres, 5)
        );
        FeatureUtils.register(
                cxt,
                ORE_TIN,
                Feature.ORE,
                new OreConfiguration(tinOres, 14)
        );
        FeatureUtils.register(
                cxt,
                ORE_TIN_SMALL,
                Feature.ORE,
                new OreConfiguration(tinOres, 9)
        );
        FeatureUtils.register(
                cxt,
                ORE_SILICON,
                Feature.ORE,
                new OreConfiguration(siliconOres, 6)
        );
        FeatureUtils.register(
                cxt,
                ORE_SILICON_BURIED,
                Feature.ORE,
                new OreConfiguration(siliconOres, 6, 0.32F)
        );

        FeatureUtils.register(
                cxt,
                CRUDE_OIL_FOUNTAIN,
                GalacticraftFeatures.CRUDE_OIL_POOL.get(),
                new CrudeOilPoolConfiguration.Builder(Blocks.WATER)
                        .radius(3, 6)
                        .height(1, 7)
                        .distanceFromRoof(50)
                        .build()
        );
        FeatureUtils.register(
                cxt,
                CRUDE_OIL_FOUNTAIN_SMALL,
                GalacticraftFeatures.CRUDE_OIL_POOL.get(),
                new CrudeOilPoolConfiguration.Builder(Blocks.WATER)
                        .radius(2)
                        .height(2, 4)
                        .distanceFromRoof(80)
                        .build()
        );
        FeatureUtils.register(
                cxt,
                ORE_CRUDE_OIL,
                GalacticraftFeatures.CRUDE_OIL_POOL.get(),
                CrudeOilPoolConfiguration.Builder.ofTag(GalacticraftTags.Blocks.CRUDE_OIL_POOL_REPLACEABLE)
                        .height(1, 2)
                        .radius(2, 4)
                        .build()
        );
        FeatureUtils.register(
                cxt,
                ORE_CRUDE_OIL_MAJOR,
                GalacticraftFeatures.CRUDE_OIL_POOL.get(),
                CrudeOilPoolConfiguration.Builder.ofTag(GalacticraftTags.Blocks.CRUDE_OIL_POOL_REPLACEABLE)
                        .height(3, 7)
                        .radius(1, 6)
                        .build()
        );
    }
}
