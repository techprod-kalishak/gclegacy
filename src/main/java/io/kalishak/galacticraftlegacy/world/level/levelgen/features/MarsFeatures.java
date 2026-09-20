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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class MarsFeatures {
    public static final ResourceKey<Feature> COPPER_ORE = GalacticraftFeatures.key("mars_copper_ore");
    public static final ResourceKey<Feature> TIN_ORE = GalacticraftFeatures.key("mars_tin_ore");
    public static final ResourceKey<Feature> DESH_ORE = GalacticraftFeatures.key("mars_desh_ore");
    public static final ResourceKey<Feature> IRON_ORE = GalacticraftFeatures.key("mars_iron_ore");
    public static final ResourceKey<Feature> PACKED_ICE_PATCH = GalacticraftFeatures.key("mars_packed_ice_patch");
    public static final ResourceKey<Feature> DIRT_PATCH = GalacticraftFeatures.key("mars_dirt_patch");

    static void bootstrap(BootstrapContext<Feature> cxt) {
        RuleTest marsStone = new TagMatchTest(GalacticraftTags.Blocks.BASE_STONE_MARS);
        cxt.register(
                COPPER_ORE,
                new OreFeature(marsStone, GalacticraftBlocks.MARS_COPPER_ORE.get().defaultBlockState(), 4)
        );
        cxt.register(
                TIN_ORE,
                new OreFeature(marsStone, GalacticraftBlocks.MARS_TIN_ORE.get().defaultBlockState(), 4)
        );
        cxt.register(
                DESH_ORE,
                new OreFeature(marsStone, GalacticraftBlocks.MARS_DESH_ORE.get().defaultBlockState(), 6)
        );
        cxt.register(
                IRON_ORE,
                new OreFeature(marsStone, GalacticraftBlocks.MARS_IRON_ORE.get().defaultBlockState(), 8)
        );
        cxt.register(
                PACKED_ICE_PATCH,
                new OreFeature(marsStone, Blocks.PACKED_ICE.defaultBlockState(), 18)
        );
        cxt.register(
                DIRT_PATCH,
                new OreFeature(List.of(BlockReplacement.replace(marsStone, Blocks.DIRT.defaultBlockState())), 32, 0.8F)
        );
    }
}
