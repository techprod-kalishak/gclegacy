/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class GalacticraftNoises {
    public static final ResourceKey<NormalNoise> OPENSPACE = Constants.key(Registries.NOISE, "moon/temperature");
    public static final ResourceKey<NormalNoise> OPENSPACE_VEG = Constants.key(Registries.NOISE, "moon/vegetation");

    public static void bootstrap(BootstrapContext<NormalNoise> context) {
//        context.register(
//                OPENSPACE,
//                NormalNoise.builder()
//                        .setBaseAmplitude(0.0D)
//                        .setOctaveCount(1)
//                        .setAmplitudeModifier(0, 1.0D)
//                        .build()
//        );
//        context.register(
//                OPENSPACE_VEG,
//                NormalNoise.builder()
//                        .setBaseAmplitude(0.0D)
//                        .setOctaveCount(1)
//                        .setAmplitudeModifier(0, 1.0D)
//                        .build()
//        );
    }
}
