/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import io.kalishak.galacticraftlegacy.data.worldgen.GalacticraftMaterialRules;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
import java.util.Optional;

public final class GalacticraftNoiseGeneratorSettings {
    public static final ResourceKey<NoiseGeneratorSettings> MOON = Constants.key(Registries.NOISE_SETTINGS, "moon");
    public static final ResourceKey<NoiseGeneratorSettings> OPEN_SPACE = Constants.key(Registries.NOISE_SETTINGS, "open_space");
    public static final ResourceKey<NoiseGeneratorSettings> MARS = Constants.key(Registries.NOISE_SETTINGS, "mars");
    public static final ResourceKey<NoiseGeneratorSettings> ASTEROIDS = Constants.key(Registries.NOISE_SETTINGS, "asteroids");
    public static final ResourceKey<NoiseGeneratorSettings> VENUS = Constants.key(Registries.NOISE_SETTINGS, "venus");

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> cxt) {
        HolderGetter<DensityFunction> densityFunctions = cxt.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise> noises = cxt.lookup(Registries.NOISE);
        HolderGetter<MaterialRule> rules = cxt.lookup(Registries.MATERIAL_RULE);

        NoiseRouter moonRouter = GalacticraftNoiseRouterData.moon(densityFunctions, noises);
        cxt.register(
                MOON,
                new NoiseGeneratorSettings(
                        NoiseSettings.create(-32, 192),
                        GalacticraftBlocks.MOON_TURF.get().defaultBlockState(),
                        Blocks.AIR.defaultBlockState(),
                        moonRouter,
                        rules.getOrThrow(GalacticraftMaterialRules.MOON),
                        List.of(),
                        68,
                        true,
                        Optional.empty(),
                        false,
                        new NoiseGeneratorSettings.DebugFunctions(
                                List.of(
                                        new NoiseGeneratorSettings.DebugFunctionEntry("N", moonRouter.finalDensity())
                                )
                        )
                )
        );
        cxt.register(
                OPEN_SPACE,
                new NoiseGeneratorSettings(
                        NoiseSettings.create(0, 128),
                        Blocks.AIR.defaultBlockState(),
                        Blocks.AIR.defaultBlockState(),
                        GalacticraftNoiseRouterData.empty(),
                        GalacticraftMaterialRules.empty(),
                        List.of(),
                        0,
                        true,
                        Optional.empty(),
                        false,
                        new NoiseGeneratorSettings.DebugFunctions(List.of())
                )
        );
        cxt.register(
                MARS,
                new NoiseGeneratorSettings(
                        NoiseSettings.create(-64, 192),
                        Blocks.AIR.defaultBlockState(),
                        Blocks.AIR.defaultBlockState(),
                        GalacticraftNoiseRouterData.empty(),
                        GalacticraftMaterialRules.empty(),
                        List.of(),
                        0,
                        true,
                        Optional.empty(),
                        false,
                        new NoiseGeneratorSettings.DebugFunctions(List.of())
                )
        );
    }
}
