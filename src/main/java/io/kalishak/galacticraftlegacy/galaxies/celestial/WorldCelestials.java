package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;

public class WorldCelestials extends SavedData {
    public static final Codec<WorldCelestials> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(
                    ResourceKey.codec(GalacticraftRegistries.Keys.CELESTIAL),
                    CelestialBodyInstance.CODEC
            ).fieldOf("instances").forGetter(worldCelestials -> worldCelestials.instances)
    ).apply(instance, WorldCelestials::new));
    public static final SavedDataType<WorldCelestials> ID = new SavedDataType<>(Constants.id("world_celestials"), WorldCelestials::new, CODEC);
    private final Map<ResourceKey<Celestial>, CelestialBodyInstance> instances;

    public WorldCelestials() {
        this.instances = new HashMap<>();
    }

    public WorldCelestials(Map<ResourceKey<Celestial>, CelestialBodyInstance> instances) {
        this.instances = instances;
    }

    public CelestialBodyInstance get(Holder<Celestial> body) {
        return this.instances.computeIfAbsent(body.getKey(), _ -> new CelestialBodyInstance(body));
    }

    public void set(ResourceKey<Celestial> body, CelestialBodyInstance instance) {
        this.instances.put(body, instance);
    }

    public void set(Holder<Celestial> body, CelestialBodyInstance instance) {
        set(body.getKey(), instance);
    }
}
