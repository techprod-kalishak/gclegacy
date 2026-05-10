/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.inventory.slot.CapabilityHandlerSlot;
import io.kalishak.galacticraftlegacy.world.inventory.slot.ResultResourceHandlerSlot;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ElectricCompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AlloyCompressor;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ElectricCompressorBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.List;

public class ElectricCompressorMenu extends AbstractCompressorMenu {
    public ElectricCompressorMenu(int containerId, Inventory playerInventory, ElectricCompressorBlockEntity compressor, ContainerData dataAccess) {
        super(GalacticraftMenuType.ELECTRIC_COMPRESSOR.get(), containerId, playerInventory, compressor, dataAccess);

        addCompressorGrid(this.compressorInventory, compressor::set, 19, 18);
        addSlot(new CapabilityHandlerSlot<>(this.compressorInventory, compressor::set, Capabilities.Energy.ITEM, AlloyCompressor.FUEL_SLOT, 55, 75));
        addSlot(new ResultResourceHandlerSlot(playerInventory.player, this.compressorInventory, _ -> {}, AlloyCompressor.RESULT_SLOT_START, 138, 30));
        addSlot(new ResultResourceHandlerSlot(playerInventory.player, this.compressorInventory, _ -> {}, AlloyCompressor.RESULT_SLOT_END, 138, 48));

        addStandardInventorySlots(playerInventory, 8, 117);
    }

    public ElectricCompressorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.ELECTRIC_COMPRESSOR.get(), playerInventory.player.level(), data), new SimpleContainerData(4));
    }

    public int getEnergyCapacity() {
        return ResourcefulHelper.getEnergyHandler((BlockEntity) this.compressor, null).getCapacityAsInt();
    }

    public ElectricCompressorBlockEntity getMachine() {
        return (ElectricCompressorBlockEntity) this.compressor;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, RecipeHolder<?> recipe, ServerLevel level, Inventory inventory) {
        final List<Slot> slotsToClear = List.of(
                getSlot(0), //CRAFTING_SLOT_START
                getSlot(1),
                getSlot(2),
                getSlot(3),
                getSlot(4),
                getSlot(5),
                getSlot(6),
                getSlot(7),
                getSlot(8), //CRAFTING_SLOT_END
                getSlot(AlloyCompressor.RESULT_SLOT_START)
        );

        return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
                ElectricCompressorMenu.this.fillCraftSlotsStackedContents(stackedContents);
            }

            @Override
            public void clearCraftingContent() {
                slotsToClear.forEach(s -> s.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<ElectricCompressingRecipe> recipe) {
                return recipe.value().matches(new CompressingRecipeInput(3, 3, () -> ElectricCompressorMenu.this.compressorInventory), level);
            }
        }, 3, 3, this.slots.subList(AlloyCompressor.CRAFTING_SLOT_START, AlloyCompressor.CRAFTING_SLOT_END), slotsToClear, inventory, (RecipeHolder<ElectricCompressingRecipe>) recipe, useMaxItems, allowDroppingItemsToClear);
    }
}
