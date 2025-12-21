package io.kalishak.galacticraftlegacy.codec;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public interface SerializableEnum extends StringRepresentable {
    @Override
    @NotNull
    String getSerializedName();

    int getIndex();

    static <Serializable extends SerializableEnum> Codec<Serializable> codec(Class<Serializable> clazz) {
        if (!clazz.isEnum()) {
            throw new IllegalStateException(clazz + " is not an enum");
        }

        return StringRepresentable.fromValues(clazz::getEnumConstants);
    }

    static <Serializable extends SerializableEnum> StreamCodec<ByteBuf, Serializable> streamCodec(Class<Serializable> clazz) {
        if (!clazz.isEnum()) {
            throw new IllegalStateException(clazz + " is not an enum");
        }

        IntFunction<Serializable> byId = ByIdMap.continuous(SerializableEnum::getIndex, clazz.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.ZERO);

        return ByteBufCodecs.idMapper(byId, SerializableEnum::getIndex);
    }
}
