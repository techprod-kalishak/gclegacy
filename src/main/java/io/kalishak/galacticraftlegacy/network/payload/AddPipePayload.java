package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record AddPipePayload(BlockPos blockPos, boolean firstUpdate, int addedAmount) implements CustomPacketPayload {
    public static final Type<AddPipePayload> TYPE = new Type<>(Constants.id("add_pipe"));
    public static final StreamCodec<ByteBuf, AddPipePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, AddPipePayload::blockPos,
            ByteBufCodecs.BOOL, AddPipePayload::firstUpdate,
            ByteBufCodecs.INT, AddPipePayload::addedAmount,
            AddPipePayload::new
    );

    @Override
    public Type<AddPipePayload> type() {
        return TYPE;
    }
}
