/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.data.worldgen.GalacticraftSurfaceRuleData;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public final class GalacticraftNoiseGeneratorSettings {
    public static final ResourceKey<NoiseGeneratorSettings> MOON = Constants.key(Registries.NOISE_SETTINGS, "moon");
    public static final ResourceKey<NoiseGeneratorSettings> OPEN_SPACE = Constants.key(Registries.NOISE_SETTINGS, "open_space");
    public static final ResourceKey<NoiseGeneratorSettings> MARS = Constants.key(Registries.NOISE_SETTINGS, "mars");
    public static final ResourceKey<NoiseGeneratorSettings> ASTEROIDS = Constants.key(Registries.NOISE_SETTINGS, "asteroids");
    public static final ResourceKey<NoiseGeneratorSettings> VENUS = Constants.key(Registries.NOISE_SETTINGS, "venus");

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> cxt) {
        HolderGetter<DensityFunction> densityFunctions = cxt.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParameters = cxt.lookup(Registries.NOISE);

        cxt.register(
                MOON,
                new NoiseGeneratorSettings(
                        NoiseSettings.create(-32, 128, 2, 2),
                        GalacticraftBlocks.MOON_TURF.get().defaultBlockState(),
                        Blocks.AIR.defaultBlockState(),
                        GalacticraftNoiseRouterData.moon(densityFunctions, noiseParameters),
                        GalacticraftSurfaceRuleData.bruh(),
                        List.of(
                                new Climate.ParameterPoint(
                                        Climate.Parameter.point(-2.0F),
                                        Climate.Parameter.point(0.0F),
                                        Climate.Parameter.span(-1.0F, 0.0F),
                                        Climate.Parameter.point(0.0F),
                                        Climate.Parameter.point(-1.0F),
                                        Climate.Parameter.point(-0.3F),
                                        0L
                                )
                        ),
                        68,
                        true,
                        false,
                        true,
                        false
                )
        );
        cxt.register(
                OPEN_SPACE,
                new NoiseGeneratorSettings(
                        NoiseSettings.create(0, 256, 1, 1),
                        Blocks.AIR.defaultBlockState(),
                        Blocks.AIR.defaultBlockState(),
                        GalacticraftNoiseRouterData.empty(),
                        GalacticraftSurfaceRuleData.empty(),
                        List.of(),
                        0,
                        true,
                        false,
                        false,
                        false
                )
        );
        cxt.register(
                MARS,
                new NoiseGeneratorSettings(
                        NoiseSettings.create(0, 128, 1, 1),
                        Blocks.AIR.defaultBlockState(),
                        Blocks.AIR.defaultBlockState(),
                        GalacticraftNoiseRouterData.empty(),
                        GalacticraftSurfaceRuleData.empty(),
                        List.of(),
                        0,
                        true,
                        false,
                        false,
                        false
                )
        );
    }
}
