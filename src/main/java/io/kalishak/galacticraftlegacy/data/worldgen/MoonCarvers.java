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
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.*;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

public class MoonCarvers {
    public static final ResourceKey<WorldCarver> CAVE = GalacticraftCarvers.key("moon_cave");

    static void bootstrap(BootstrapContext<WorldCarver> cxt, HolderGetter<Block> blocks) {


        cxt.register(
                CAVE,
                new CaveWorldCarver(
                        0.05F,
                        UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(56)),
                        UniformInt.of(1, 5),
                        UniformFloat.of(0.1F, 0.3F),
                        false,
                        UniformFloat.of(0.2F, 0.6F),
                        UniformFloat.of(0.2F, 0.5F),
                        UniformFloat.of(-0.9F, -0.1F),
                        UniformFloat.of(-0.9F, -0.1F),
                        UniformFloat.of(0.2F, 0.4F)
                )
        );
    }
}
