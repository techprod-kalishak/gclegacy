package io.kalishak.galacticraftlegacy.registry.deferred;

import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBody;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialObjectType;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DeferredGalaxyRegister extends DeferredRegister<CelestialBody> {
    private DeferredGalaxyRegister(String namespace) {
        super(GalacticraftRegistries.Keys.CELESTIAL_BODY, namespace);
    }

    public static DeferredGalaxyRegister createGalaxy(String namespace) {
        return new DeferredGalaxyRegister(namespace);
    }

    public DeferredHolder<CelestialBody, CelestialBody> registerCelestialBody(String name, Holder<CelestialObjectType> objectType, UnaryOperator<CelestialBody.Builder> builder) {
        return register(name, () -> builder.apply(CelestialBody.Builder.of(objectType::value))
                .create(
                        ResourceKey.create(
                                GalacticraftRegistries.Keys.CELESTIAL_BODY,
                                Identifier.fromNamespaceAndPath(getNamespace(), name))
                )
        );
    }
}
