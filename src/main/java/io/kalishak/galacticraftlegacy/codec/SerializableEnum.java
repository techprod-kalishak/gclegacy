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
import org.jspecify.annotations.Nullable;

import java.util.function.IntFunction;

public interface SerializableEnum extends StringRepresentable {

    @Override
    @NonNull String getSerializedName();

    int getIndex();

    static <Serializable extends SerializableEnum> Codec<Serializable> codec(Class<Serializable> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalStateException(enumClass + " is not an enum");
        }

        return StringRepresentable.fromValues(enumClass::getEnumConstants);
    }

    static <Serializable extends SerializableEnum> StreamCodec<ByteBuf, Serializable> streamCodec(Class<Serializable> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalStateException(enumClass + " is not an enum");
        }

        IntFunction<Serializable> byId = ByIdMap.continuous(SerializableEnum::getIndex, enumClass.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.ZERO);

        return ByteBufCodecs.idMapper(byId, SerializableEnum::getIndex);
    }

    static @Nullable <Serializable extends Enum<Serializable> & SerializableEnum> Serializable fromIndexMap(Class<Serializable> enumClass, int index) {
        if (!enumClass.isEnum()) {
            throw new IllegalStateException(enumClass + " is not an enum");
        }

        Serializable[] enums = enumClass.getEnumConstants();

        if (index > enums.length) {
            throw new IllegalStateException("Provided index " + index + " is out of bound for length " + enums.length);
        }

        for (Serializable serializable : enums) {
            if (serializable.getIndex() == index) {
                return serializable;
            }
        }

        return null;
    }
}
