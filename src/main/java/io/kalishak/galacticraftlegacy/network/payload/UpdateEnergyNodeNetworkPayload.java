/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateEnergyNodeNetworkPayload(BlockPos pos, int amount) implements CustomPacketPayload {
    public static final Type<UpdateEnergyNodeNetworkPayload> TYPE = new Type<>(Constants.id("update_energy_node_network"));
    public static final StreamCodec<ByteBuf, UpdateEnergyNodeNetworkPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateEnergyNodeNetworkPayload::pos,
            ByteBufCodecs.INT, UpdateEnergyNodeNetworkPayload::amount,
            UpdateEnergyNodeNetworkPayload::new
    );

    @Override
    public Type<UpdateEnergyNodeNetworkPayload> type() {
        return TYPE;
    }
}
