package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

public record VehicleCraftingSlotType(String id) {
    public static final Codec<VehicleCraftingSlotType> DIRECT_CODEC = Codec.STRING.xmap(VehicleCraftingSlotType::new, VehicleCraftingSlotType::id);
    public static final Codec<Holder<VehicleCraftingSlotType>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<VehicleCraftingSlotType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE);
}
