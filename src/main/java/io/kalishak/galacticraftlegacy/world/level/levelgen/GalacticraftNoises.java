/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class GalacticraftNoises {
    public static final ResourceKey<NormalNoise.NoiseParameters> OPENSPACE = Constants.key(Registries.NOISE, "moon/temperature");
    public static final ResourceKey<NormalNoise.NoiseParameters> OPENSPACE_VEG = Constants.key(Registries.NOISE, "moon/vegetation");

    public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
        context.register(OPENSPACE, new NormalNoise.NoiseParameters(4, 0.0D, 1.0D));
        context.register(OPENSPACE_VEG, new NormalNoise.NoiseParameters(1, 0.0D, 1.0D));
    }
}
