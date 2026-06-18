/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class GalacticraftNoiseRouterData {
    public static NoiseRouter template(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters, DensityFunction postProcessor) {
        DensityFunction xShift = getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("shift_x")));
        DensityFunction zShift = getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("shift_z")));
        DensityFunction temperature = DensityFunctions.shiftedNoise2d(xShift, zShift, 0.25, noiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d(xShift, zShift, 0.25, noiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction finalDensity = postProcess(postProcessor);

        return new NoiseRouter(
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                temperature,
                vegetation,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                finalDensity,
                DensityFunctions.constant(0.1D),
                DensityFunctions.constant(1.0D),
                DensityFunctions.constant(0.3D)
        );
    }

    public static NoiseRouter empty() {
        return new NoiseRouter(
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero()
        );
    }

    public static NoiseRouter moon(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        DensityFunction caveFunction = slideCaves(densityFunctions, 10, 100);

        return template(densityFunctions, noiseParameters, caveFunction);
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> densityFunctionRegistry, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(densityFunctionRegistry.getOrThrow(key));
    }

    private static DensityFunction postProcess(DensityFunction densityFunction) {
        DensityFunction densityfunction = DensityFunctions.blendDensity(densityFunction);
        return DensityFunctions.mul(DensityFunctions.interpolated(densityfunction), DensityFunctions.constant(0.64)).squeeze();
    }

    private static DensityFunction slideCaves(HolderGetter<DensityFunction> densityFunctions, int minY, int height) {
        return slide(
                getFunction(
                        densityFunctions,
                        NoiseRouterData.CONTINENTS_LARGE),
                minY,
                height,
                16,
                0,
                0.9375,
                -8,
                24,
                2.5
        );
    }

    private static DensityFunction slide(
            DensityFunction input, int minY, int height, int topStartOffset, int topEndOffset, double topDelta, int bottomStartOffset, int bottomEndOffset, double bottomDelta
    ) {
        DensityFunction densityfunction1 = DensityFunctions.yClampedGradient(minY + height - topStartOffset, minY + height - topEndOffset, 1.0, 0.0);
        DensityFunction $$9 = DensityFunctions.lerp(densityfunction1, topDelta, input);
        DensityFunction densityfunction2 = DensityFunctions.yClampedGradient(minY + bottomStartOffset, minY + bottomEndOffset, 0.0, 1.0);
        return DensityFunctions.lerp(densityfunction2, bottomDelta, $$9);
    }
}
