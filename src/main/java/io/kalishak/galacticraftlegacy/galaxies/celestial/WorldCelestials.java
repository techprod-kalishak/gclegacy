package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;

public class WorldCelestials extends SavedData {
    public static final SavedDataType<WorldCelestials> ID = new SavedDataType<>(
            Constants.id("world_celestials"),
            server -> new WorldCelestials(server.registryAccess()),
            server -> codec(server.registryAccess())
            );
    private final Map<ResourceKey<CelestialBody>, CelestialBodyInstance> instances;
    private final Registry<CelestialBody> registry;

    public WorldCelestials(RegistryAccess registryAccess) {
        this.registry = registryAccess.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL_BODY);
        this.instances = new HashMap<>();
    }

    public WorldCelestials(RegistryAccess registryAccess, Map<ResourceKey<CelestialBody>, CelestialBodyInstance> instances) {
        this.registry = registryAccess.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL_BODY);
        this.instances = instances;
    }

    public static Codec<WorldCelestials> codec(RegistryAccess registryAccess) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.simpleMap(
                        ResourceKey.codec(GalacticraftRegistries.Keys.CELESTIAL_BODY),
                        CelestialBodyInstance.CODEC,
                        Keyable.forStrings(() -> registryAccess.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL_BODY).keySet().stream().map(Identifier::toString))
                ).fieldOf("instances").forGetter(worldCelestials -> worldCelestials.instances)
        ).apply(instance, instances -> new WorldCelestials(registryAccess, instances)));
    }

    public CelestialBodyInstance get(ResourceKey<CelestialBody> body) {
        return this.instances.computeIfAbsent(body, key -> new CelestialBodyInstance(this.registry.getOrThrow(key)));
    }

    public CelestialBodyInstance get(Holder<CelestialBody> bodyHolder) {
        return get(bodyHolder.getKey());
    }

    public CelestialBodyInstance get(CelestialBody bodyHolder) {
        return this.registry.getResourceKey(bodyHolder)
                .map(this::get)
                .orElseThrow(() -> new IllegalArgumentException("The following celestialbody is not registered: " + bodyHolder.getDescriptionId()));
    }

    public void set(ResourceKey<CelestialBody> body, CelestialBodyInstance instance) {
        this.instances.put(body, instance);
    }

    public void set(Holder<CelestialBody> body, CelestialBodyInstance instance) {
        set(body.getKey(), instance);
    }

    public void set(CelestialBody body, CelestialBodyInstance instance) {
        this.registry.getResourceKey(body).ifPresent(key -> set(key, instance));
    }
}
