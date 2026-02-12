package io.kalishak.galacticraftlegacy.space.environment;

import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public class AtmosphereInfoImpl implements AtmosphereInfo {
    private final boolean isCorrosive;
    private final boolean hasPrecipitation;
    private final Object2DoubleMap<ResourceKey<Fluid>> gasComposition;
    private final float temperature;

    AtmosphereInfoImpl(boolean isCorrosive, boolean hasPrecipitation, Object2DoubleMap<ResourceKey<Fluid>> gasComposition, float temperature) {
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
    public Object2DoubleMap<ResourceKey<Fluid>> getGasComposition() {
        return this.gasComposition;
    }

    @Override
    public float getTemperature() {
        return this.temperature;
    }

    public static class Builder {
        private boolean isCorrosive = false;
        private boolean hasPrecipitation = false;
        private final Object2DoubleMap<ResourceKey<Fluid>> gasComposition = new Object2DoubleArrayMap<>();
        private float temperature = 273.15F;

        public Builder() {}

        public Builder isCorrosive(boolean isCorrosive) {
            this.isCorrosive = isCorrosive;
            return this;
        }

        public Builder hasPrecipitation(boolean hasPrecipitation) {
            this.hasPrecipitation = hasPrecipitation;
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
            return new AtmosphereInfoImpl(this.isCorrosive, this.hasPrecipitation, this.gasComposition, this.temperature);
        }
    }
}
