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
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record MoveVehiclePagePayload(int containerId, int currentPage, Action action, BlockPos blockPos) implements CustomPacketPayload {
    public static final Type<MoveVehiclePagePayload> TYPE = new Type<>(Constants.id("move_vehicle_page"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MoveVehiclePagePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MoveVehiclePagePayload::containerId,
            ByteBufCodecs.VAR_INT, MoveVehiclePagePayload::currentPage,
            Action.STREAM_CODEC, MoveVehiclePagePayload::action,
            BlockPos.STREAM_CODEC, MoveVehiclePagePayload::blockPos,
            MoveVehiclePagePayload::new
    );

    @Override
    public Type<MoveVehiclePagePayload> type() {
        return TYPE;
    }

    public enum Action {
        NEXT,
        PREVIOUS;

        static final StreamCodec<RegistryFriendlyByteBuf, Action> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Action.class);

        public int apply(int currentIndex) {
            return this == NEXT ? currentIndex + 1 : currentIndex - 1;
        }
    }
}
