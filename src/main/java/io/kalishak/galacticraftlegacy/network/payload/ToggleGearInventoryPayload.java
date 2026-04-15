/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ToggleGearInventoryPayload(boolean open) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ToggleGearInventoryPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Galacticraft.MODID, "toggle_gear_inventory"));
    public static final StreamCodec<ByteBuf, ToggleGearInventoryPayload> STREAM_CODEC = ByteBufCodecs.BOOL.map(ToggleGearInventoryPayload::new, ToggleGearInventoryPayload::open);

    @Override
    public Type<ToggleGearInventoryPayload> type() {
        return TYPE;
    }
}
