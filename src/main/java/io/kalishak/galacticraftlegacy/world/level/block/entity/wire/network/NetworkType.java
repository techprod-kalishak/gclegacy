package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public enum NetworkType implements SerializableEnum {
    POWER("power", 0),
    FLUID("fluid", 1),
    ITEM("item", 2),
    SOLAR("solar", 3),
    NONE("none", 4);

    public static final Codec<NetworkType> CODEC = SerializableEnum.codec(NetworkType.class);
    public static final StreamCodec<ByteBuf, NetworkType> STREAM_CODEC = SerializableEnum.streamCodec(NetworkType.class);
    private final String name;
    private final int id;

    NetworkType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    @Override
    public int getIndex() {
        return this.id;
    }
}
