/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.levelgen.CraterSize;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations.CraterConfiguration;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations.CrudeOilPoolConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftFeatures {
    private static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, Galacticraft.MODID);

    public static final DeferredHolder<Feature<?>, CrudeOilPoolFeature> CRUDE_OIL_POOL = REGISTRY.register(
            "crude_oil_pool",
            CrudeOilPoolFeature::new
    );
    public static final DeferredHolder<Feature<?>, CraterFeature> CRATER = REGISTRY.register(
            "crater",
            CraterFeature::new
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> cxt) {
        AsteroidsFeatures.bootstrap(cxt);
        MarsFeatures.bootstrap(cxt);
        MoonFeatures.bootstrap(cxt);
        OverworldFeatures.bootstrap(cxt);
        VenusFeatures.bootstrap(cxt);
    }

    static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return Constants.key(Registries.CONFIGURED_FEATURE, name);
    }

    static void craterSimple(BootstrapContext<ConfiguredFeature<?, ?>> cxt, ResourceKey<ConfiguredFeature<?, ?>> key, CraterSize size, int uniformCount) {
        cxt.register(
                key,
                new ConfiguredFeature<>(
                        GalacticraftFeatures.CRATER.get(),
                        new CraterConfiguration(size, ConstantInt.of(uniformCount))
                )
        );
    }
}
