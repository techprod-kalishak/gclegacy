/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

public record UpdateFluidNodeNetworkPayload(BlockPos pos, FluidStack stack) implements NetworkGridPacket {
    public static final Type<UpdateFluidNodeNetworkPayload> TYPE = new Type<>(Constants.id("update_fluid_network"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateFluidNodeNetworkPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateFluidNodeNetworkPayload::pos,
            FluidStack.OPTIONAL_STREAM_CODEC, UpdateFluidNodeNetworkPayload::stack,
            UpdateFluidNodeNetworkPayload::new
    );

    @Override
    public Type<UpdateFluidNodeNetworkPayload> type() {
        return TYPE;
    }
}
