package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

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
        this.instances = instances;
    }

    public CelestialBodyInstance get(Holder<Celestial> body) {
        return this.instances.get(body.getKey());
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
        if (!this.instances.containsKey(body.getKey())) {
            this.instances.put(body.getKey(), new CelestialBodyInstance(body));
        }

        instance.accept(this.instances.get(body.getKey()));
    }
}
