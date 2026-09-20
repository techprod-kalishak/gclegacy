/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.TilingMode;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import static net.minecraft.world.level.levelgen.NoiseRouterData.getFunction;

public class GalacticraftNoiseRouterData {
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_MOON = key("moon/base_3d_noise");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_MARS = key("mars/base_3d_noise");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_ASTEROIDS = key("asteroids/base_3d_noise");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_VENUS = key("venus/base_3d_noise");
    public static final ResourceKey<DensityFunction> OVERWORLD_ORE_VEIN_ALUMINUM_DENSITY = key("overworld/ore_vein/aluminum_density");
    public static final ResourceKey<DensityFunction> OVERWORLD_ORE_VEIN_TIN_DENSITY = key("overworld/ore_vein/tin_density");

    public static void bootstrap(BootstrapContext<DensityFunction> context) {
        HolderGetter<DensityFunction> functions = context.lookup(Registries.DENSITY_FUNCTION);

        context.register(
                BASE_3D_NOISE_MOON,
                new BlendedNoise(0.25D, 0.25F, 80.0D, 60.0D, 4.0D)
        );

        DensityFunction y = getFunction(functions, vanillaKey("y"));
        DensityFunction veinToggle = getFunction(functions, vanillaKey("overworld/ore_vein/toggle"));
        DensityFunction veinMask = getFunction(functions, vanillaKey("overworld/ore_vein/mask"));

        context.register(
                OVERWORLD_ORE_VEIN_ALUMINUM_DENSITY,
                createOreVeinDensity(
                        -20,
                        35,
                        y,
                        veinToggle,
                        veinMask,
                        true
                )
        );
        context.register(
                OVERWORLD_ORE_VEIN_TIN_DENSITY,
                createOreVeinDensity(
                        -9,
                        20,
                        y,
                        veinToggle,
                        veinMask,
                        true
                )
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
                DensityFunctions.zero()
        );
    }

    public static NoiseRouter moon(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise> noises) {
        DensityFunction gradient = DensityFunctions.gradient(
                Direction.Axis.X,
                TilingMode.CLAMP_TO_EDGE,
                0,
                128,
                0.0F,
                1.0F
        );

        DensityFunction shiftX = getFunction(densityFunctions, vanillaKey("shift_x"));
        DensityFunction shiftZ = getFunction(densityFunctions, vanillaKey("shift_z"));
        DensityFunction temperature = DensityFunctions.shiftedNoise2d(
                shiftX, shiftZ, 0.25, noises.getOrThrow(Noises.TEMPERATURE)
        );
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d(
                shiftX, shiftZ, 0.25, noises.getOrThrow(Noises.VEGETATION)
        );

        return new NoiseRouter(
                temperature,
                vegetation,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                gradient,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                postSlide(densityFunctions, gradient)
        );
    }

    private static DensityFunction postProcess(DensityFunction slide, int cellSizeXz, int cellSizeY) {
        DensityFunction blended = DensityFunctions.blendDensity(slide);

        return DensityFunctions.interpolated(DensityFunctions.mul(blended, DensityFunctions.constant(0.64F)), cellSizeXz, cellSizeY).squeeze();
    }

    protected static DensityFunction postSlide(HolderGetter<DensityFunction> functions, DensityFunction post) {
        DensityFunction slide = slideMoon(functions, -64, 192);

        return DensityFunctions.add(postProcess(slide, 4, 8), post);
    }

    private static DensityFunction slideMoon(HolderGetter<DensityFunction> functions, int minY, int height) {
        return slide(getFunction(functions, BASE_3D_NOISE_MOON), minY, height, 24, 0, 0.9375F, -8, 24, 2.5F);
    }

    private static DensityFunction slide(DensityFunction caves, int minY, int height, int topStartY, int topEndY, float topTarget, int bottomStartY, int bottomEndY, float bottomTarget) {
        DensityFunction noiseValue = caves;
        DensityFunction topFactor = DensityFunctions.yClampedGradient(minY + height - topStartY, minY + height - topEndY, 1.0F, 0.0F);
        noiseValue = DensityFunctions.lerp(topFactor, topTarget, noiseValue);
        DensityFunction bottomFactor = DensityFunctions.yClampedGradient(minY + bottomStartY, minY + bottomEndY, 0.0F, 1.0F);
        return DensityFunctions.lerp(bottomFactor, bottomTarget, noiseValue);
    }

    private static DensityFunction createOreVeinDensity(
            int minY, int maxY, DensityFunction y, DensityFunction toggle, DensityFunction baseVeinMask, boolean whenTogglePositive
    ) {
        DensityFunction noVein = DensityFunctions.constant(-1.0F);
        DensityFunction distanceFromEdge = DensityFunctions.min(DensityFunctions.constant(maxY).sub(y), y.sub(minY));
        DensityFunction edgeRoundoff = DensityFunctions.clampedMap(distanceFromEdge, 0.0F, 20.0F, -0.2F, 0.0F);
        DensityFunction veininess = whenTogglePositive ? toggle : toggle.negate();
        return DensityFunctions.rangeChoice(
                y,
                minY,
                maxY,
                DensityFunctions.rangeChoice(
                        baseVeinMask,
                        0.0F,
                        1000000.0F,
                        DensityFunctions.rangeChoice(veininess.sub(0.4F).add(edgeRoundoff), 0.0F, 1000000.0F, DensityFunctions.constant(0.7F), noVein),
                        noVein
                ),
                noVein
        );
    }

    private static ResourceKey<DensityFunction> vanillaKey(String key) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace(key));
    }

    private static ResourceKey<DensityFunction> key(String key) {
        return Constants.key(Registries.DENSITY_FUNCTION, key);
    }
}
