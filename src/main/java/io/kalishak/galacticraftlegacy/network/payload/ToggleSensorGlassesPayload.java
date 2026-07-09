package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ToggleSensorGlassesPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ToggleSensorGlassesPayload> TYPE = new Type<>(Constants.id("toggle_sensor_glasses"));
    public static final StreamCodec<ByteBuf, ToggleSensorGlassesPayload> STREAM_CODEC = StreamCodec.unit(new ToggleSensorGlassesPayload());

    @Override
    public Type<ToggleSensorGlassesPayload> type() {
        return TYPE;
    }
}
