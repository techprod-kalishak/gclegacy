/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public enum VehicleComponentType implements SerializableEnum {
    PLATE("plate", 0),
    NOSE_CONE("nose_cone", 1),
    ENGINE("engine", 2),
    FINS("fins", 3),
    BOOSTER("booster", 4),
    STORAGE("storage", 5);

    public static final Codec<VehicleComponentType> CODEC = SerializableEnum.codec(VehicleComponentType.class);
    public static final StreamCodec<ByteBuf, VehicleComponentType> STREAM_CODEC = SerializableEnum.streamCodec(VehicleComponentType.class);
    private final String name;
    private final int id;

    VehicleComponentType(String name, int id) {
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
