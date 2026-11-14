package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ToggleMachineButtonPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ToggleMachineButtonPayload> TYPE = new Type<>(Constants.id("toggle_machine_button"));
    public static final StreamCodec<ByteBuf, ToggleMachineButtonPayload> STREAM_CODEC = BlockPos.STREAM_CODEC.map(ToggleMachineButtonPayload::new, ToggleMachineButtonPayload::pos);

    @Override
    public Type<ToggleMachineButtonPayload> type() {
        return TYPE;
    }
}
