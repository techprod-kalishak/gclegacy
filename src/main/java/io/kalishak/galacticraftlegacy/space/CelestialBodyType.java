package io.kalishak.galacticraftlegacy.space;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface CelestialBodyType {
    DeferredRegister<CelestialBodyType> REGISTRY = DeferredRegister.create(GalacticraftRegistries.Keys.CELESTIAL_BODY_TYPE, Galacticraft.MODID);

    Holder<CelestialBodyType> SOLAR_SYSTEM = REGISTRY.register("solar_system", () -> CelestialBodyType.simple(Galacticraft.id("solar_system")));
    Holder<CelestialBodyType> STAR = REGISTRY.register("star", () -> CelestialBodyType.simple(Galacticraft.id("star")));
    Holder<CelestialBodyType> PLANET = REGISTRY.register("planet", () -> CelestialBodyType.simple(Galacticraft.id("planet")));
    Holder<CelestialBodyType> MOON = REGISTRY.register("moon", () -> CelestialBodyType.simple(Galacticraft.id("moon")));
    Holder<CelestialBodyType> SATELLITE = REGISTRY.register("satellite", () -> CelestialBodyType.simple(Galacticraft.id("satellite")));

    static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    static CelestialBodyType simple(Identifier name) {
        return new CelestialBodyType() {
            @Override
            public String toString() {
                return name.toString();
            }
        };
    }
}
