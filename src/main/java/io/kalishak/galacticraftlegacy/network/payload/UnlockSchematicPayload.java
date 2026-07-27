/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record UnlockSchematicPayload(int containerId, UUID playerId) implements CustomPacketPayload {
    public static final Type<UnlockSchematicPayload> TYPE = new Type<>(Constants.id("unlock_schematic"));
    public static final StreamCodec<ByteBuf, UnlockSchematicPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, UnlockSchematicPayload::containerId,
            UUIDUtil.STREAM_CODEC, UnlockSchematicPayload::playerId,
            UnlockSchematicPayload::new
    );

    @Override
    public Type<UnlockSchematicPayload> type() {
        return TYPE;
    }
}
