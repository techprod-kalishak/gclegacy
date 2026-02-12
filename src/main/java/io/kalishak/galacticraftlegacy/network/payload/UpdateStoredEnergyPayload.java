package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateStoredEnergyPayload(int containerId, int newAmount) implements CustomPacketPayload {
    public static final Type<UpdateStoredEnergyPayload> TYPE = new Type<>(Constants.id("update_stored_energy"));
    public static final StreamCodec<ByteBuf, UpdateStoredEnergyPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, UpdateStoredEnergyPayload::containerId,
            ByteBufCodecs.INT, UpdateStoredEnergyPayload::newAmount,
            UpdateStoredEnergyPayload::new
    );

    @Override
    public Type<UpdateStoredEnergyPayload> type() {
        return TYPE;
    }
}
