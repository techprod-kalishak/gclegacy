/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public record MoveVehiclePagePayload(int containerId, Optional<ResourceKey<SchematicVariant>> currentSchematic, Schematics.Picker action, BlockPos blockPos) implements CustomPacketPayload {
    public static final Type<MoveVehiclePagePayload> TYPE = new Type<>(Constants.id("move_vehicle_page"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MoveVehiclePagePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MoveVehiclePagePayload::containerId,
            ResourceKey.streamCodec(GalacticraftRegistries.Keys.SCHEMATIC).apply(ByteBufCodecs::optional), MoveVehiclePagePayload::currentSchematic,
            Schematics.Picker.STREAM_CODEC, MoveVehiclePagePayload::action,
            BlockPos.STREAM_CODEC, MoveVehiclePagePayload::blockPos,
            MoveVehiclePagePayload::new
    );

    @Override
    public Type<MoveVehiclePagePayload> type() {
        return TYPE;
    }
}
