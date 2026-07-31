/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.slot.VehicleCraftingSlotResourceHandler;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.function.Consumer;

public record VehicleCraftingPage(Holder<SchematicVariant> schematic, Holder<VehicleCraftingDataRecipe> vehicleRecipe, int inventoryHeight, int imageHeight, Identifier background) {
    public static final Codec<VehicleCraftingPage> DIRECT_CODEC =  RecordCodecBuilder.create(instance -> instance.group(
            SchematicVariant.CODEC.fieldOf("schematic").forGetter(VehicleCraftingPage::schematic),
            VehicleCraftingDataRecipe.CODEC.fieldOf("vehicle_recipe").forGetter(VehicleCraftingPage::vehicleRecipe),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("inventory_height", 138).forGetter(VehicleCraftingPage::inventoryHeight),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("image_height", 166).forGetter(VehicleCraftingPage::imageHeight),
            Identifier.CODEC.fieldOf("background").forGetter(VehicleCraftingPage::background)
    ).apply(instance, VehicleCraftingPage::new));
    public static final Codec<Holder<VehicleCraftingPage>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_PAGE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<VehicleCraftingPage>> STREAM_CODEC = ByteBufCodecs.holderRegistry(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_PAGE);

    public int getInputSlotSize() {
        return this.vehicleRecipe.value().inputSlots().size();
    }

    public static void populateContainer(VehicleCraftingDataRecipe dataRecipe, Consumer<Slot> slotAdder, ResourceHandler<ItemResource> resourceHandler, IndexModifier<ItemResource> indexModifier, Level level, BlockPos blockPos) {
        NonNullList<VehicleCraftingEntry> entries = dataRecipe.inputSlots();
        VehicleCraftingEntry output = dataRecipe.outputSlot();

        for (VehicleCraftingEntry entry : entries) {
            slotAdder.accept(new VehicleCraftingSlotResourceHandler(level, blockPos, resourceHandler, indexModifier, entry.slotIndex(), entry.slotOffsetX(), entry.slotOffsetY()));
        }

        slotAdder.accept(new ResourceHandlerSlot(resourceHandler, ResourcefulHelper::notPlaceable, output.slotIndex(), output.slotOffsetX(), output.slotOffsetY()) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
    }
}
