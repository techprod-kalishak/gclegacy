package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateStoredEnergyPayload(int previousAmount, int deltas) implements CustomPacketPayload {
    public static final Type<UpdateStoredEnergyPayload> TYPE = new Type<>(Galacticraft.id("update_stored_energy"));
    public static final StreamCodec<ByteBuf, UpdateStoredEnergyPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateStoredEnergyPayload::previousAmount,
            ByteBufCodecs.INT, UpdateStoredEnergyPayload::deltas,
            UpdateStoredEnergyPayload::new
    );

    @Override
    public Type<UpdateStoredEnergyPayload> type() {
        return TYPE;
    }
}
