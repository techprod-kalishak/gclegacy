/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies.celestial;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface CelestialObjectType {
    DeferredRegister<CelestialObjectType> REGISTRY = DeferredRegister.create(GalacticraftRegistries.Keys.CELESTIAL_OBJECT_TYPE, Galacticraft.MODID);

    Holder<CelestialObjectType> SOLAR_SYSTEM = REGISTRY.register("solar_system", () -> CelestialObjectType.simple(Constants.id("solar_system")));
    Holder<CelestialObjectType> STAR = REGISTRY.register("star", () -> CelestialObjectType.simple(Constants.id("star")));
    Holder<CelestialObjectType> PLANET = REGISTRY.register("planet", () -> CelestialObjectType.simple(Constants.id("planet")));
    Holder<CelestialObjectType> MOON = REGISTRY.register("moon", () -> CelestialObjectType.simple(Constants.id("moon")));
    Holder<CelestialObjectType> SATELLITE = REGISTRY.register("satellite", () -> CelestialObjectType.simple(Constants.id("satellite")));

    static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    static CelestialObjectType simple(Identifier name) {
        return new CelestialObjectType() {
            @Override
            public String toString() {
                return name.toString();
            }
        };
    }
}
