/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public interface RocketTier {
    Type getRocketType();

    FeatureTier getFeatureTier();

    enum Type implements SerializableEnum {
        DEFAULT("default", 0, 0),
        SMALL_STORAGE("small_storage", 1, 18),
        MEDIUM_STORAGE("medium_storage", 2, 36),
        LARGE_STORAGE("large_storage", 3, 54),
        PREFUELED("prefueled", 4, 54);

        public static final Codec<Type> CODEC = SerializableEnum.codec(Type.class);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = SerializableEnum.streamCodec(Type.class);
        private final String name;
        private final int index;
        private final int additionalSlots;

        Type(String name, int index, int additionalSlots) {
            this.name = name;
            this.index = index;
            this.additionalSlots = additionalSlots;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public boolean isPrefueled() {
            return this == PREFUELED;
        }

        public int getAdditionalSlots() {
            return this.additionalSlots;
        }
    }
}
