/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * @param warmth warmth on spawn
 * @param craterChance chance of creating a crater on impact
 * @param minDistanceBetween minimum amount of chunks between one and the second meteor
 * @param maxDistanceBetween maximum amount of chunks between one and the second meteor
 */
public record FallenMeteorConfiguration(HotContent warmth, FloatProvider craterChance, IntProvider minDistanceBetween, IntProvider maxDistanceBetween) implements FeatureConfiguration {
    public static final Codec<FallenMeteorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            HotContent.MAP_CODEC.forGetter(FallenMeteorConfiguration::warmth),
            FloatProviders.CODEC.fieldOf("crater_chance").forGetter(FallenMeteorConfiguration::craterChance),
            IntProviders.codec(4, 32).fieldOf("min_distance_between").forGetter(FallenMeteorConfiguration::minDistanceBetween),
            IntProviders.codec(4, 32).fieldOf("max_distance_between").forGetter(FallenMeteorConfiguration::maxDistanceBetween)
    ).apply(instance, FallenMeteorConfiguration::new));

    public static final FallenMeteorConfiguration DEFAULT = new FallenMeteorConfiguration(
            HotContent.DEFAULT,
            ConstantFloat.of(1.0F),
            ConstantInt.of(4),
            UniformInt.of(8, 12)
    );

    public static class Builder {
        private HotContent warmth = HotContent.DEFAULT;
        private FloatProvider craterChance = ConstantFloat.of(1.0F);
        private IntProvider minDistanceBetween = ConstantInt.of(4);
        private IntProvider maxDistanceBetween = UniformInt.of(8, 12);

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder warmth(int initialWarmth) {
            this.warmth = new HotContent(initialWarmth);
            return this;
        }

        public Builder noCrater() {
            this.craterChance = ConstantFloat.ZERO;
            return this;
        }

        public Builder craterChance(float chance) {
            this.craterChance = ConstantFloat.of(chance);
            return this;
        }

        public Builder craterChance(float min, float max) {
            this.craterChance = UniformFloat.of(min, max);
            return this;
        }

        public Builder minDistanceBetween(int lowerDistance, int upperDistance) {
            this.minDistanceBetween = UniformInt.of(lowerDistance, upperDistance);
            return this;
        }

        public Builder maxDistanceBetween(int lowerDistance, int upperDistance) {
            this.maxDistanceBetween = UniformInt.of(lowerDistance, upperDistance);
            return this;
        }

        public FallenMeteorConfiguration build() {
            return new FallenMeteorConfiguration(this.warmth, this.craterChance, this.minDistanceBetween, this.maxDistanceBetween);
        }
    }
}
