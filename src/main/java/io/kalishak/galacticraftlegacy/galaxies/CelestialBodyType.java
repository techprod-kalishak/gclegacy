package io.kalishak.galacticraftlegacy.galaxies;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface CelestialBodyType {
    DeferredRegister<CelestialBodyType> REGISTRY = DeferredRegister.create(GalacticraftRegistries.Keys.CELESTIAL_BODY_TYPE, Galacticraft.MODID);

    Holder<CelestialBodyType> SOLAR_SYSTEM = REGISTRY.register("solar_system", () -> CelestialBodyType.simple(Constants.id("solar_system")));
    Holder<CelestialBodyType> STAR = REGISTRY.register("star", () -> CelestialBodyType.simple(Constants.id("star")));
    Holder<CelestialBodyType> PLANET = REGISTRY.register("planet", () -> CelestialBodyType.simple(Constants.id("planet")));
    Holder<CelestialBodyType> MOON = REGISTRY.register("moon", () -> CelestialBodyType.simple(Constants.id("moon")));
    Holder<CelestialBodyType> SATELLITE = REGISTRY.register("satellite", () -> CelestialBodyType.simple(Constants.id("satellite")));

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
