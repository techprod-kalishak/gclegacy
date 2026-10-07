package io.kalishak.galacticraftlegacy.registry.deferred;

import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBody;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialObjectType;
import io.kalishak.galacticraftlegacy.galaxies.celestial.SolarSystem;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class DeferredGalaxyRegister extends DeferredRegister<CelestialBody> {
    private DeferredGalaxyRegister(String namespace) {
        super(GalacticraftRegistries.Keys.CELESTIAL_BODY, namespace);
    }

    public static DeferredGalaxyRegister createGalaxy(String namespace) {
        return new DeferredGalaxyRegister(namespace);
    }

    public <C extends CelestialBody> DeferredHolder<CelestialBody, C> registerCelestialBody(String name, Holder<CelestialObjectType> objectType, BiFunction<CelestialBody.Builder, ResourceKey<CelestialBody>, C> factory, UnaryOperator<CelestialBody.Builder> builder) {
        return register(name, () -> factory.apply(
                builder.apply(CelestialBody.Builder.of(objectType)),
                ResourceKey.create(GalacticraftRegistries.Keys.CELESTIAL_BODY, Identifier.fromNamespaceAndPath(getNamespace(), name)))
        );
    }

    public DeferredHolder<CelestialBody, CelestialBody> registerSimpleCelestialBody(String name, Holder<CelestialObjectType> objectType, UnaryOperator<CelestialBody.Builder> builder) {
        return registerCelestialBody(name, objectType, CelestialBody.Builder::create, builder);
    }

    public DeferredHolder<CelestialBody, SolarSystem> registerSolarSystem(String name, Holder<CelestialObjectType> objectType, Supplier<ResourceKey<CelestialBody>> starKey, UnaryOperator<CelestialBody.Builder> builder) {
        return register(name, () -> new SolarSystem(
                builder.apply(CelestialBody.Builder.of(objectType)),
                starKey.get(),
                ResourceKey.create(GalacticraftRegistries.Keys.CELESTIAL_BODY, Identifier.fromNamespaceAndPath(getNamespace(), name)))
        );
    }
}
