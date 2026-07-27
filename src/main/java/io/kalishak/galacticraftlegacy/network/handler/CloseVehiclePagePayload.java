/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CloseVehiclePagePayload(int containerId) implements CustomPacketPayload {
    public static final Type<CloseVehiclePagePayload> TYPE = new Type<>(Constants.id("close_vehicle_page"));
    public static final StreamCodec<ByteBuf, CloseVehiclePagePayload> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(CloseVehiclePagePayload::new, CloseVehiclePagePayload::containerId);

    @Override
    public Type<CloseVehiclePagePayload> type() {
        return TYPE;
    }
}
