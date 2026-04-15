/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.codec;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.function.IntFunction;

public interface SerializableEnum extends StringRepresentable {

    @Override
    @NonNull String getSerializedName();

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
