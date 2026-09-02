/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class MarsFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> COPPER_ORE = GalacticraftFeatures.key("mars_copper_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE = GalacticraftFeatures.key("mars_tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DESH_ORE = GalacticraftFeatures.key("mars_desh_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> IRON_ORE = GalacticraftFeatures.key("mars_iron_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PACKED_ICE_PATCH = GalacticraftFeatures.key("mars_packed_ice_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DIRT_PATCH = GalacticraftFeatures.key("mars_dirt_patch");

    static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> cxt) {
        RuleTest marsStone = new TagMatchTest(GalacticraftTags.Blocks.BASE_STONE_MARS);
        FeatureUtils.register(
                cxt,
                COPPER_ORE,
                Feature.ORE,
                new OreConfiguration(marsStone, GalacticraftBlocks.MARS_COPPER_ORE.get().defaultBlockState(), 4)
        );
        FeatureUtils.register(
                cxt,
                TIN_ORE,
                Feature.ORE,
                new OreConfiguration(marsStone, GalacticraftBlocks.MARS_TIN_ORE.get().defaultBlockState(), 4)
        );
        FeatureUtils.register(
                cxt,
                DESH_ORE,
                Feature.ORE,
                new OreConfiguration(marsStone, GalacticraftBlocks.MARS_DESH_ORE.get().defaultBlockState(), 6)
        );
        FeatureUtils.register(
                cxt,
                IRON_ORE,
                Feature.ORE,
                new OreConfiguration(marsStone, GalacticraftBlocks.MARS_IRON_ORE.get().defaultBlockState(), 8)
        );
        FeatureUtils.register(
                cxt,
                PACKED_ICE_PATCH,
                Feature.ORE,
                new OreConfiguration(marsStone, Blocks.PACKED_ICE.defaultBlockState(), 18)
        );
        FeatureUtils.register(
                cxt,
                DIRT_PATCH,
                Feature.ORE,
                new OreConfiguration(marsStone, Blocks.DIRT.defaultBlockState(), 32, 0.8F)
        );
    }
}
