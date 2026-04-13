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
        DEFAULT("default", 0, false, 2),
        SMALL_STORAGE("small_storage", 1, false, 20),
        MEDIUM_STORAGE("medium_storage", 2, false, 38),
        LARGE_STORAGE("large_storage", 3, false, 56),
        PREFUELED("prefueled", 4, true, 2);

        public static final Codec<Type> CODEC = SerializableEnum.codec(Type.class);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = SerializableEnum.streamCodec(Type.class);
        private final String name;
        private final int index;
        private final boolean prefueled;
        private final int inventorySize;

        Type(String name, int index, boolean prefueled, int inventorySize) {
            this.name = name;
            this.index = index;
            this.prefueled = prefueled;
            this.inventorySize = inventorySize;
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
            return this.prefueled;
        }

        public int getInventorySize() {
            return this.inventorySize;
        }
    }
}
