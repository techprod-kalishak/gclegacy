package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public record UpdateResourceNetworkPayload(BlockPos pos, FluidResource fluidResource, int amount) implements NetworkGridPacket {
    public static final Type<UpdateResourceNetworkPayload> TYPE = new Type<>(Constants.id("update_fluid_network"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateResourceNetworkPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateResourceNetworkPayload::pos,
            FluidResource.STREAM_CODEC, UpdateResourceNetworkPayload::fluidResource,
            ByteBufCodecs.INT, UpdateResourceNetworkPayload::amount,
            UpdateResourceNetworkPayload::new
    );

    @Override
    public Type<UpdateResourceNetworkPayload> type() {
        return TYPE;
    }
}
