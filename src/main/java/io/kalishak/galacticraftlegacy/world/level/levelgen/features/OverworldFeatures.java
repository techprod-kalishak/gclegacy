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
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class OverworldFeatures {
    public static final ResourceKey<Feature> ORE_ALUMINUM = GalacticraftFeatures.key("aluminum_ore");
    public static final ResourceKey<Feature> ORE_ALUMINUM_SMALL = GalacticraftFeatures.key("aluminum_ore_small");
    public static final ResourceKey<Feature> ORE_TIN = GalacticraftFeatures.key("tin_ore");
    public static final ResourceKey<Feature> ORE_TIN_SMALL = GalacticraftFeatures.key("tin_ore_small");
    public static final ResourceKey<Feature> ORE_SILICON = GalacticraftFeatures.key("silicon_ore");
    public static final ResourceKey<Feature> ORE_SILICON_BURIED = GalacticraftFeatures.key("silicon_ore_buried");

    public static final ResourceKey<Feature> CRUDE_OIL_FOUNTAIN = GalacticraftFeatures.key("crude_oil_fountain");
    public static final ResourceKey<Feature> CRUDE_OIL_FOUNTAIN_SMALL = GalacticraftFeatures.key("crude_oil_small_fountain");
    public static final ResourceKey<Feature> ORE_CRUDE_OIL = GalacticraftFeatures.key("ore_crude_oil");
    public static final ResourceKey<Feature> ORE_CRUDE_OIL_MAJOR = GalacticraftFeatures.key("ore_crude_oil_major");

    static void bootstrap(BootstrapContext<Feature> cxt) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<BlockReplacement> aluminumOres = List.of(
                BlockReplacement.replace(stoneReplaceable, GalacticraftBlocks.ALUMINUM_ORE.get().defaultBlockState()),
                BlockReplacement.replace(deepslateReplaceable, GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE.get().defaultBlockState())
        );
        List<BlockReplacement> tinOres = List.of(
                BlockReplacement.replace(stoneReplaceable, GalacticraftBlocks.TIN_ORE.get().defaultBlockState()),
                BlockReplacement.replace(deepslateReplaceable, GalacticraftBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())
        );
        List<BlockReplacement> siliconOres = List.of(
                BlockReplacement.replace(stoneReplaceable, GalacticraftBlocks.SILICON_ORE.get().defaultBlockState()),
                BlockReplacement.replace(deepslateReplaceable, GalacticraftBlocks.DEEPSLATE_SILICON_ORE.get().defaultBlockState())
        );

        cxt.register(ORE_ALUMINUM, new OreFeature(aluminumOres, 6));
        cxt.register(ORE_ALUMINUM_SMALL, new OreFeature(aluminumOres, 3));
        cxt.register(ORE_TIN, new OreFeature(tinOres, 10));
        cxt.register(ORE_TIN_SMALL, new OreFeature(tinOres, 4));
        cxt.register(ORE_SILICON, new OreFeature(siliconOres, 5));
        cxt.register(ORE_SILICON_BURIED, new OreFeature(siliconOres, 4, 0.32F));
        cxt.register(
                CRUDE_OIL_FOUNTAIN,
                new CrudeOilPoolFeature.Builder(Blocks.WATER)
                        .radius(3, 6)
                        .height(1, 7)
                        .distanceFromRoof(50)
                        .build()
        );
        cxt.register(
                CRUDE_OIL_FOUNTAIN_SMALL,
                new CrudeOilPoolFeature.Builder(Blocks.WATER)
                        .radius(2)
                        .height(2, 4)
                        .distanceFromRoof(80)
                        .build()
        );
        cxt.register(
                ORE_CRUDE_OIL,
                CrudeOilPoolFeature.Builder.ofTag(GalacticraftTags.Blocks.CRUDE_OIL_POOL_REPLACEABLE)
                        .height(1, 2)
                        .radius(2, 4)
                        .build()
        );
        cxt.register(
                ORE_CRUDE_OIL_MAJOR,
                CrudeOilPoolFeature.Builder.ofTag(GalacticraftTags.Blocks.CRUDE_OIL_POOL_REPLACEABLE)
                        .height(3, 7)
                        .radius(1, 6)
                        .build()
        );
    }
}
