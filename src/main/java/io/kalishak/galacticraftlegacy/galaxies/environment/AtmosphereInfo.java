package io.kalishak.galacticraftlegacy.galaxies.environment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.Constants;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;

public class AtmosphereInfo {
    public static final Codec<AtmosphereInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("is_corrosive").forGetter(AtmosphereInfo::isCorrosive),
            Codec.BOOL.fieldOf("has_precipitation").forGetter(AtmosphereInfo::hasPrecipitation),
            Codec.unboundedMap(ResourceKey.codec(Registries.FLUID), Codec.DOUBLE).xmap(map -> {
                Object2DoubleMap<ResourceKey<Fluid>> gasComposition = new Object2DoubleArrayMap<>();
                gasComposition.putAll(map);
                return gasComposition;
            }, HashMap::new
            ).fieldOf("gas_composition").forGetter(AtmosphereInfo::getGasComposition),
            Codec.FLOAT.fieldOf("temperature").forGetter(AtmosphereInfo::getTemperature)
    ).apply(instance, AtmosphereInfo::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, AtmosphereInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, AtmosphereInfo::isCorrosive,
            ByteBufCodecs.BOOL, AtmosphereInfo::hasPrecipitation,
            ByteBufCodecs.map(Object2DoubleArrayMap::new, ResourceKey.streamCodec(Registries.FLUID), ByteBufCodecs.DOUBLE), AtmosphereInfo::getGasComposition,
            ByteBufCodecs.FLOAT, AtmosphereInfo::getTemperature,
            AtmosphereInfo::new
    );
    public static final AtmosphereInfo EARTH = builder()
            .corrosive()
            .rainy()
            .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 78.08)
            .gas(ResourceKey.create(Registries.FLUID, Constants.id("oxygen")), 20.95)
            .gas(ResourceKey.create(Registries.FLUID, Constants.id("argon")), 0.93)
            .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 0.04)
            .build();

    private final boolean isCorrosive;
    private final boolean hasPrecipitation;
    private final Object2DoubleMap<ResourceKey<Fluid>> gasComposition;
    private final float temperature;

    private AtmosphereInfo(boolean isCorrosive, boolean hasPrecipitation, Object2DoubleMap<ResourceKey<Fluid>> gasComposition, float temperature) {
        this.isCorrosive = isCorrosive;
        this.hasPrecipitation = hasPrecipitation;
        this.gasComposition = gasComposition;
        this.temperature = temperature;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isCorrosive() {
        return this.isCorrosive;
    }

    public boolean hasPrecipitation() {
        return this.hasPrecipitation;
    }

    public Object2DoubleMap<ResourceKey<Fluid>> getGasComposition() {
        return this.gasComposition;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public boolean isBreathable() {
        double oxygenLevel = getGasComposition().getOrDefault(ResourceKey.create(Registries.FLUID, Constants.id("oxygen")), 0.0);
        return oxygenLevel >= 19.5 && oxygenLevel <= 23.5 && !isCorrosive();
    }

    public static class Builder {
        private boolean isCorrosive = false;
        private boolean hasPrecipitation = false;
        private final Object2DoubleMap<ResourceKey<Fluid>> gasComposition = new Object2DoubleArrayMap<>();
        private float temperature = 273.15F;

        private Builder() {}

        public Builder corrosive() {
            this.isCorrosive = true;
            return this;
        }

        public Builder rainy() {
            this.hasPrecipitation = true;
            return this;
        }

        public Builder gas(ResourceKey<Fluid> fluid, double amount) {
            this.gasComposition.put(fluid, amount);
            return this;
        }

        public Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        public AtmosphereInfo build() {
            return new AtmosphereInfo(this.isCorrosive, this.hasPrecipitation, this.gasComposition, this.temperature);
        }
    }
}
