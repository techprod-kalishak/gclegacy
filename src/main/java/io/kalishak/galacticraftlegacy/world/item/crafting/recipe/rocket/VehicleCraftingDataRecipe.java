/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record VehicleCraftingDataRecipe(NonNullList<VehicleCraftingEntry> inputSlots, VehicleCraftingEntry outputSlot, ItemStackTemplate resultItem) {
    public static final Codec<VehicleCraftingDataRecipe> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            NonNullList.codecOf(VehicleCraftingEntry.CODEC).fieldOf("input_slots").forGetter(VehicleCraftingDataRecipe::inputSlots),
            VehicleCraftingEntry.CODEC.fieldOf("output_slot").forGetter(VehicleCraftingDataRecipe::outputSlot),
            ItemStackTemplate.CODEC.fieldOf("item").forGetter(VehicleCraftingDataRecipe::resultItem)
    ).apply(instance, VehicleCraftingDataRecipe::new));
    public static final Codec<Holder<VehicleCraftingDataRecipe>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<VehicleCraftingDataRecipe>> STREAM_CODEC = ByteBufCodecs.holderRegistry(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA);

    public static class Builder {
        private final HolderGetter<VehicleCraftingSlotType> slotTypes;
        private int size = 0;
        private final List<VehicleCraftingEntry> inputSlots = new ArrayList<>();
        private VehicleCraftingEntry outputSlot;
        private Holder<Item> resultItem;

        private Builder(HolderGetter<VehicleCraftingSlotType> getter) {
            this.slotTypes = getter;
        }

        public static Builder builder(HolderGetter<VehicleCraftingSlotType> slotTypes) {
            return new Builder(slotTypes);
        }

        public Builder inputSlot(ResourceKey<VehicleCraftingSlotType> slotType, int xOffset, int yOffset, @NonNull Ingredient input) {
            if (this.outputSlot != null) {
                throw new IllegalStateException("Cannot add input slots after output was defined!");
            }

            this.inputSlots.add(new VehicleCraftingEntry(
                    this.slotTypes.getOrThrow(slotType),
                    this.size++,
                    xOffset,
                    yOffset,
                    Optional.of(input)
            ));

            return this;
        }

        public Builder inputSlot(ResourceKey<VehicleCraftingSlotType> slotType, int xOffset, int yOffset, ItemLike input) {
            return inputSlot(slotType, xOffset, yOffset, Ingredient.of(input));
        }

        public Builder outputSlot(int xOffset, int yOffset, Holder<Item> resultItem) {
            this.outputSlot = new VehicleCraftingEntry(
                    this.slotTypes.getOrThrow(VehicleCraftingSlotTypes.RESULT),
                    this.size,
                    xOffset,
                    yOffset,
                    Optional.empty()
            );
            this.resultItem = resultItem;

            return this;
        }

        public VehicleCraftingDataRecipe build() {
            if (this.inputSlots.isEmpty() || this.outputSlot == null) {
                throw new IllegalStateException("Cannot build recipe without valid input and output!");
            }


            return new VehicleCraftingDataRecipe(
                    NonNullList.copyOf(this.inputSlots),
                    this.outputSlot,
                    new ItemStackTemplate(this.resultItem)
            );
        }
    }
}
