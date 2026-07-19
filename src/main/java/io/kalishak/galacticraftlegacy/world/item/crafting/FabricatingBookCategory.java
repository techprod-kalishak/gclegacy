/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum FabricatingBookCategory implements SerializableEnum {
    BASIC(0, "basic"),
    ADVANCED(1, "advanced"),
    SOLAR(2, "solar"),
    MISC(3, "misc");

    public static final Codec<FabricatingBookCategory> CODEC = SerializableEnum.codec(FabricatingBookCategory.class);
    public static final StreamCodec<ByteBuf, FabricatingBookCategory> STREAM_CODEC = SerializableEnum.streamCodec(FabricatingBookCategory.class);
    private final int id;
    private final String name;

    FabricatingBookCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getIndex() {
        return this.id;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
