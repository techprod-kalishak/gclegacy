package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class WorldCelestials extends SavedData {
    public static final Codec<WorldCelestials> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(
                    ResourceKey.codec(GalacticraftRegistries.Keys.CELESTIAL),
                    CelestialBodyInstance.OPTIONAL_CODEC
            ).fieldOf("instances").forGetter(worldCelestials -> worldCelestials.instances)
    ).apply(instance, WorldCelestials::new));
    public static final SavedDataType<WorldCelestials> ID = new SavedDataType<>(Constants.id("world_celestials"), WorldCelestials::new, CODEC);
    private final Map<ResourceKey<Celestial>, CelestialBodyInstance> instances;

    public WorldCelestials() {
        this.instances = new HashMap<>();
    }

    public WorldCelestials(Map<ResourceKey<Celestial>, CelestialBodyInstance> instances) {
        this.instances = new HashMap<>(instances);
    }

    public static WorldCelestials get(MinecraftServer server) {
        WorldCelestials worldCelestials = server.getDataStorage().get(WorldCelestials.ID);

        if (worldCelestials == null) {
            worldCelestials = new WorldCelestials();
            server.getDataStorage().set(WorldCelestials.ID, worldCelestials);
        }

        return worldCelestials;
    }

    public static WorldCelestials get(SavedDataStorage savedDataStorage) {
        WorldCelestials worldCelestials = savedDataStorage.get(WorldCelestials.ID);

        if (worldCelestials == null) {
            worldCelestials = new WorldCelestials();
            savedDataStorage.set(WorldCelestials.ID, worldCelestials);
        }

        return worldCelestials;
    }

    public @Nullable CelestialBodyInstance get(Holder<Celestial> body) {
        return this.instances.get(body.key());
    }

    public CelestialBodyInstance getOrDefault(Holder<Celestial> body) {
        if (!this.instances.containsKey(body.key())) {
            this.instances.put(body.key(), new CelestialBodyInstance(body));
        }

        return this.instances.get(body.key());
    }

    public CelestialBodyInstance getFromLevel(Level level) {
        Holder<DimensionType> type = level.dimensionTypeRegistration();

        Optional<CelestialBodyInstance> celestial = this.instances.values().stream()
                .filter(CelestialBodyInstance::hasTargetDimension)
                .filter(instance -> instance.getTargetDimension().is(type))
                .findFirst();

        return celestial.orElse(CelestialBodyInstance.EMPTY);
    }

    public void modify(Holder<Celestial> body, Consumer<CelestialBodyInstance> instance) {
        if (!this.instances.containsKey(body.key())) {
            this.instances.put(body.key(), new CelestialBodyInstance(body));
        }

        instance.accept(this.instances.get(body.key()));
    }
}
