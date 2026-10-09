/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.galaxies.celestial.*;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftCelestialTypes {
    private static final DeferredRegister<MapCodec<? extends Celestial>> REGISTRY = DeferredRegister.create(GalacticraftRegistries.CELESTIAL_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<MapCodec<? extends Celestial>, MapCodec<SimpleCelestialBody>> SIMPLE = REGISTRY.register(
            "simple",
            () -> SimpleCelestialBody.MAP_CODEC
    );
    public static final DeferredHolder<MapCodec<? extends Celestial>, MapCodec<Star>> STAR = REGISTRY.register(
            "star",
            () -> Star.MAP_CODEC
    );
    public static final DeferredHolder<MapCodec<? extends Celestial>, MapCodec<SolarSystem>> SOLAR_SYSTEM = REGISTRY.register(
            "solar_system",
            () -> SolarSystem.MAP_CODEC
    );
    public static final DeferredHolder<MapCodec<? extends Celestial>, MapCodec<Satellite>> SATELLITE = REGISTRY.register(
            "satellite",
            () -> Satellite.MAP_CODEC
    );


    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
