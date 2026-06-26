/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.worldgen;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

public class MoonCarvers {
    public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE = GalacticraftCarvers.key("moon_cave");

    static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> cxt, HolderGetter<Block> blocks) {
        cxt.register(
                CAVE,
                WorldCarver.CAVE.configured(
                        new CaveCarverConfiguration(
                                0.2F,
                                UniformHeight.of(VerticalAnchor.aboveBottom(12), VerticalAnchor.absolute(56)),
                                UniformFloat.of(0.1F, 0.3F),
                                VerticalAnchor.bottom(),
                                CarverDebugSettings.of(false, Blocks.STONE_BUTTON.defaultBlockState()),
                                blocks.getOrThrow(GalacticraftTags.Blocks.MOON_CARVER_REPLACEABLES),
                                UniformFloat.of(0.2F, 0.4F),
                                UniformFloat.of(0.2F, 0.4F),
                                UniformFloat.of(-0.4F, 0.1F)
                        )
                )
        );
    }
}
