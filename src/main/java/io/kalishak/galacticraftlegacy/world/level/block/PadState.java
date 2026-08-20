/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public enum PadState implements SerializableEnum {
    NONE("none", 0),
    CENTER("center", 1);

    public static final Codec<PadState> CODEC = SerializableEnum.codec(PadState.class);
    public static final StreamCodec<ByteBuf, PadState> STREAM_CODEC = SerializableEnum.streamCodec(PadState.class);
    private final String name;
    private final int index;

    PadState(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    @Override
    public int getIndex() {
        return this.index;
    }
}
