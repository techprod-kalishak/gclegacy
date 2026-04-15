/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ChangeCameraModePayload(boolean resetCamera) implements CustomPacketPayload {
    public static final Type<ChangeCameraModePayload> TYPE = new Type<>(Constants.id("change_camera_mode"));
    public static final StreamCodec<ByteBuf, ChangeCameraModePayload> STREAM_CODEC = ByteBufCodecs.BOOL.map(ChangeCameraModePayload::new, ChangeCameraModePayload::resetCamera);

    @Override
    public Type<ChangeCameraModePayload> type() {
        return TYPE;
    }
}
