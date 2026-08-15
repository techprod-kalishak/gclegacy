/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record VehicleCraftingEntry(Holder<VehicleCraftingSlotType> slotType, int slotIndex, int slotOffsetX, int slotOffsetY) {
    public static final Codec<VehicleCraftingEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VehicleCraftingSlotType.CODEC.fieldOf("slot_type").forGetter(VehicleCraftingEntry::slotType),
            Codec.INT.fieldOf("slot_index").forGetter(VehicleCraftingEntry::slotIndex),
            Codec.INT.fieldOf("slot_offset_x").forGetter(VehicleCraftingEntry::slotOffsetX),
            Codec.INT.fieldOf("slot_offset_y").forGetter(VehicleCraftingEntry::slotOffsetY)
    ).apply(instance, VehicleCraftingEntry::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, VehicleCraftingEntry> STREAM_CODEC = StreamCodec.composite(
            VehicleCraftingSlotType.STREAM_CODEC, VehicleCraftingEntry::slotType,
            ByteBufCodecs.VAR_INT, VehicleCraftingEntry::slotIndex,
            ByteBufCodecs.VAR_INT, VehicleCraftingEntry::slotOffsetX,
            ByteBufCodecs.VAR_INT, VehicleCraftingEntry::slotOffsetY,
            VehicleCraftingEntry::new
    );
}
