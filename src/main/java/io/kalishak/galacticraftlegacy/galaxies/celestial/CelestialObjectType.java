/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies.celestial;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public record CelestialObjectType(String descriptionPrefix, boolean allowChildren) {
    public static final DeferredRegister<CelestialObjectType> REGISTRY = DeferredRegister.create(GalacticraftRegistries.Keys.CELESTIAL_OBJECT_TYPE, Galacticraft.MODID);

    public static final Holder<CelestialObjectType> SOLAR_SYSTEM = REGISTRY.register("solar_system", () -> CelestialObjectType.simple("solar_system"));
    public static final Holder<CelestialObjectType> STAR = REGISTRY.register("star", () -> CelestialObjectType.simple("star"));
    public static final Holder<CelestialObjectType> PLANET = REGISTRY.register("planet", () -> CelestialObjectType.simple("planet"));
    public static final Holder<CelestialObjectType> MOON = REGISTRY.register("moon", () -> new CelestialObjectType("moon", false));
    public static final Holder<CelestialObjectType> SATELLITE = REGISTRY.register("satellite", () -> new CelestialObjectType("satellite", false));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    static CelestialObjectType simple(String name) {
        return new CelestialObjectType(name, true);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CelestialObjectType other) {
            return other.descriptionPrefix.equals(this.descriptionPrefix);
        }

        return false;
    }
}
