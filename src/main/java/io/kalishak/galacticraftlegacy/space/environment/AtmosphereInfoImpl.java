package io.kalishak.galacticraftlegacy.space.environment;

import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public class AtmosphereInfoImpl implements AtmosphereInfo {
    private final boolean isCorrosive;
    private final boolean hasPrecipitation;
    private final Double2ObjectOpenHashMap<ResourceKey<Fluid>> gasComposition;
    private final float temperature;

    AtmosphereInfoImpl(boolean isCorrosive, boolean hasPrecipitation, Double2ObjectOpenHashMap<ResourceKey<Fluid>> gasComposition, float temperature) {
        this.isCorrosive = isCorrosive;
        this.hasPrecipitation = hasPrecipitation;
        this.gasComposition = gasComposition;
        this.temperature = temperature;
    }

    @Override
    public boolean isCorrosive() {
        return this.isCorrosive;
    }

    @Override
    public boolean hasPrecipitation() {
        return this.hasPrecipitation;
    }

    @Override
    public Double2ObjectOpenHashMap<ResourceKey<Fluid>> getGasComposition() {
        return this.gasComposition;
    }

    @Override
    public float getTemperature() {
        return this.temperature;
    }

    public static class Builder {
        private boolean isCorrosive;
        private boolean hasPrecipitation = true;
        private final Double2ObjectOpenHashMap<ResourceKey<Fluid>> gasComposition = new Double2ObjectOpenHashMap<>();
        private float temperature;

        public Builder() {}

        public Builder isCorrosive(boolean isCorrosive) {
            this.isCorrosive = isCorrosive;
            return this;
        }

        public Builder hasPrecipitation(boolean hasPrecipitation) {
            this.hasPrecipitation = hasPrecipitation;
            return this;
        }

        public Builder gas(double amount, ResourceKey<Fluid> fluid) {
            this.gasComposition.put(amount, fluid);
            return this;
        }

        public Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        public AtmosphereInfo build() {
            return new AtmosphereInfoImpl(this.isCorrosive, this.hasPrecipitation, this.gasComposition, this.temperature);
        }
    }
}
