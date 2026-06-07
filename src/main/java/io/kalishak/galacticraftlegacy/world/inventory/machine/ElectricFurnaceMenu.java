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
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ElectricFurnaceBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.List;

public class ElectricFurnaceMenu extends AbstractMachineRecipeBookMenu<ElectricFurnaceBlockEntity> {
    public ElectricFurnaceMenu(int containerId, Inventory playerInventory, ElectricFurnaceBlockEntity machine, ContainerData containerData) {
        super(GalacticraftMenuType.ARC_FURNACE.get(), containerId, playerInventory, machine, containerData);

        addSlot(new ResourceHandlerSlot(this.resourceHandler, machine::set, 0, 56, 25));
        addSlot(new CapabilityHandlerSlot<>(this.resourceHandler, machine::set, Capabilities.Energy.ITEM, 1, 8, 49));
        addSlot(new ResultResourceHandlerSlot(playerInventory.player, this.resourceHandler, machine::awardUsedRecipes, 2, 109, 25));
        addStandardInventorySlots(playerInventory, 8, 84);
    }

    public ElectricFurnaceMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(), playerInventory.player.level(), data), new SimpleContainerData(2));
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        this.machine.fillStackedContents(stackedItemContents);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.FURNACE;
    }

    public float getBurnProgress() {
        int i = this.containerData.get(0);
        int j = this.containerData.get(1);
        return j != 0 && i != 0 ? Mth.clamp((float)i / j, 0.0F, 1.0F) : 0.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack returnedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            returnedStack = stackInSlot.copy();

            if (index == 2) {
                if (!this.moveItemStackTo(stackInSlot, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stackInSlot, returnedStack);
            } else if (index != 1 && index != 0) {
                if (this.canSmelt(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.moveItemStackTo(stackInSlot, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.moveItemStackTo(stackInSlot, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == returnedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return returnedStack;
    }

    private boolean canSmelt(ItemStack stackInSlot) {
        if (stackInSlot.isEmpty() || !this.machine.hasLevel()) {
            return false;
        }

        return this.machine.getLevel().recipeAccess().propertySet(RecipePropertySet.FURNACE_INPUT).test(stackInSlot);
    }

    private boolean isFuel(ItemStack stack) {
        FurnaceFuel furnaceFuel = stack.typeHolder().getData(NeoForgeDataMaps.FURNACE_FUELS);
        return furnaceFuel != null && furnaceFuel.burnTime() > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PostPlaceAction handlePlacement(boolean useMaxItems, boolean isCreative, RecipeHolder<?> recipe, ServerLevel level, Inventory playerInventory) {
        List<Slot> craftingSlots = List.of(getSlot(0), getSlot(2));

        return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
                ElectricFurnaceMenu.this.fillCraftSlotsStackedContents(stackedItemContents);
            }

            @Override
            public void clearCraftingContent() {
                craftingSlots.forEach(slot -> slot.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<SmeltingRecipe> recipe) {
                return recipe.value().matches(new SingleRecipeInput(getSlot(0).getItem()), level);
            }
        }, 1, 1, List.of(getSlot(0)), craftingSlots, playerInventory, (RecipeHolder<SmeltingRecipe>) recipe, useMaxItems, isCreative);
    }
}
