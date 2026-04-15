/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class GalacticraftNoiseRouterData {
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE_MOON = Constants.key(Registries.DENSITY_FUNCTION, "moon/base_3d_noise");
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE_MARS = Constants.key(Registries.DENSITY_FUNCTION, "mars/base_3d_noise");
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE_VENUS = Constants.key(Registries.DENSITY_FUNCTION, "venus/base_3d_noise");
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE_ASTEROIDS = Constants.key(Registries.DENSITY_FUNCTION, "asteroids/base_3d_noise");
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE_SPACE_STATION = Constants.key(Registries.DENSITY_FUNCTION, "space_station/base_3d_noise");
    public static final ResourceKey<DensityFunction> MOON_OCEANS = Constants.key(Registries.DENSITY_FUNCTION, "moon_oceans");

    public static void bootstrap(BootstrapContext<DensityFunction> cxt) {
        HolderGetter<NormalNoise.NoiseParameters> noiseParameters = cxt.lookup(Registries.NOISE);
        HolderGetter<DensityFunction> densityFunction = cxt.lookup(Registries.DENSITY_FUNCTION);

        cxt.register(BASE_3D_NOISE_MOON, BlendedNoise.createUnseeded(0.3F, 0.215F, 40.0F, 56.0F, 4.0F));
    }

    public static NoiseRouter template(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters, DensityFunction postProcessor) {
        DensityFunction densityfunction = getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("shift_x")));
        DensityFunction densityfunction1 = getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("shift_z")));
        DensityFunction densityfunction2 = DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, 0.25, noiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction densityfunction3 = DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, 0.25, noiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction densityfunction4 = postProcess(postProcessor);
        return new NoiseRouter(
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                densityfunction2,
                densityfunction3,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                densityfunction4,
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
        DensityFunction caveFunction = slideCaves(densityFunctions, -64, 320);
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
        return slide(getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("nether/base_3d_noise"))), minY, height, 24, 0, 0.9375, -8, 24, 2.5);
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
