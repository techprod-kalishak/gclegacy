/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

public class NetworkedEnergyHandler extends SimpleEnergyHandler {
    public static final MapCodec<NetworkedEnergyHandler> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("capacity").forGetter(NetworkedEnergyHandler::getCapacityAsInt),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_insert").forGetter(networkedEnergyHandler -> networkedEnergyHandler.maxInsert),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_extract").forGetter(networkedEnergyHandler -> networkedEnergyHandler.maxExtract),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("energy").forGetter(NetworkedEnergyHandler::getAmountAsInt)
    ).apply(instance, NetworkedEnergyHandler::new));
    public static final StreamCodec<ByteBuf, NetworkedEnergyHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, NetworkedEnergyHandler::getCapacityAsInt,
            ByteBufCodecs.INT, networkedEnergyHandler -> networkedEnergyHandler.maxInsert,
            ByteBufCodecs.INT, networkedEnergyHandler -> networkedEnergyHandler.maxExtract,
            ByteBufCodecs.INT, NetworkedEnergyHandler::getAmountAsInt,
            NetworkedEnergyHandler::new
    );

    public NetworkedEnergyHandler(int capacity) {
        super(capacity);
    }

    public NetworkedEnergyHandler(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public NetworkedEnergyHandler(int capacity, int maxInsert, int maxExtract) {
        super(capacity, maxInsert, maxExtract);
    }

    public NetworkedEnergyHandler(int capacity, int maxInsert, int maxExtract, int energy) {
        super(capacity, maxInsert, maxExtract, energy);
    }
}
