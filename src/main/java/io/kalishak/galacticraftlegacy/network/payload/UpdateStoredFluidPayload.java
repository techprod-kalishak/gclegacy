package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.fluids.FluidStack;

public record UpdateStoredFluidPayload(int containerId, FluidStack content, int tankIndex) implements CustomPacketPayload {
    public static final Type<UpdateStoredFluidPayload> TYPE = new Type<>(Constants.id("update_stored_oxygen"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateStoredFluidPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, UpdateStoredFluidPayload::containerId,
            FluidStack.OPTIONAL_STREAM_CODEC, UpdateStoredFluidPayload::content,
            ByteBufCodecs.VAR_INT, UpdateStoredFluidPayload::tankIndex,
            UpdateStoredFluidPayload::new
    );

    @Override
    public Type<UpdateStoredFluidPayload> type() {
        return TYPE;
    }
}
