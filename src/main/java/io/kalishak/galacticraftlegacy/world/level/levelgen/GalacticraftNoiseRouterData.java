/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.levelgen.synth.GradientNoise;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftNoiseRouterData {
    private static final DeferredRegister<MapCodec<? extends DensityFunction>> REGISTRY = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, Galacticraft.MODID);

    public static final ResourceKey<DensityFunction> GRADIENT = Constants.key(Registries.DENSITY_FUNCTION, "gradient");

    private static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<GradientNoise>> GRADIENT_HOLDER = REGISTRY.register(
            "gradient",
            () -> GradientNoise.DATA_CODEC
    );

    public static void bootstrap(BootstrapContext<DensityFunction> context) {
        context.register(GRADIENT, GradientNoise.createUnseeded(0.25D, 0.25D, 0.25D, 4, 0.25D));
    }

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
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

    public static NoiseRouter moon(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noises) {
        DensityFunction shiftX = getFunction(densityFunctions, vanillaKey("shift_x"));
        DensityFunction shiftZ = getFunction(densityFunctions, vanillaKey("shift_z"));
        DensityFunction temperature = DensityFunctions.shiftedNoise2d(
                shiftX, shiftZ, 0.25, noises.getOrThrow(Noises.TEMPERATURE)
        );
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d(
                shiftX, shiftZ, 0.25, noises.getOrThrow(Noises.VEGETATION)
        );
        DensityFunction depth = getFunction(densityFunctions, vanillaKey("overworld/depth"));

        return new NoiseRouter(
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                temperature,
                vegetation,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                depth,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                postProcess(slide(getFunction(densityFunctions, vanillaKey("overworld/sloped_cheese")), 8, 128, 80, 64, -0.03, 0, 12, 0.1)),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero()
        );
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> functions, ResourceKey<DensityFunction> name) {
        return new DensityFunctions.HolderHolder(functions.getOrThrow(name));
    }

    private static ResourceKey<DensityFunction> vanillaKey(String key) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace(key));
    }

    private static DensityFunction postProcess(DensityFunction slide) {
        DensityFunction blended = DensityFunctions.blendDensity(slide);
        return DensityFunctions.mul(DensityFunctions.interpolated(blended), DensityFunctions.constant(0.64)).squeeze();
    }

    private static DensityFunction slide(DensityFunction caves, int minY, int height, int topStartY, int topEndY, double topTarget, int bottomStartY, int bottomEndY, double bottomTarget) {
        DensityFunction topFactor = DensityFunctions.yClampedGradient(minY + height - topStartY, minY + height - topEndY, 1.0, 0.0);
        DensityFunction noiseValue = DensityFunctions.lerp(topFactor, topTarget, caves);
        DensityFunction bottomFactor = DensityFunctions.yClampedGradient(minY + bottomStartY, minY + bottomEndY, 0.0, 1.0);
        return DensityFunctions.lerp(bottomFactor, bottomTarget, noiseValue);
    }
}
