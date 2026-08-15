/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.Item;

public record VehicleCraftingSlotType(HolderSet<Item> acceptedItems) {
    public static final Codec<VehicleCraftingSlotType> DIRECT_CODEC = HolderSetCodec.create(Registries.ITEM, Item.CODEC, true).xmap(VehicleCraftingSlotType::new, VehicleCraftingSlotType::acceptedItems);
    public static final Codec<Holder<VehicleCraftingSlotType>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<VehicleCraftingSlotType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE);
}
