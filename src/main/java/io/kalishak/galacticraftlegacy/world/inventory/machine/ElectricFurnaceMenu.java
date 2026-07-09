/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.inventory.slot.CapabilityHandlerSlot;
import io.kalishak.galacticraftlegacy.world.inventory.slot.ResultResourceHandlerSlot;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.HeatingRecipe;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ElectricFurnaceBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.List;

public class ElectricFurnaceMenu extends AbstractElectricFurnaceMenu<HeatingRecipe, ElectricFurnaceBlockEntity> {
    public ElectricFurnaceMenu(int containerId, Inventory playerInventory, ElectricFurnaceBlockEntity machine, ContainerData containerData) {
        super(GalacticraftMenuType.ELECTRIC_FURNACE.get(), containerId, playerInventory, machine, containerData, GalacticraftRecipeType.HEATING);

        addSlot(new ResourceHandlerSlot(this.resourceHandler, machine::setItem, 0, 56, 25));
        addSlot(new CapabilityHandlerSlot<>(this.resourceHandler, machine::setItem, Capabilities.Energy.ITEM, 1, 8, 49));
        addSlot(new ResultResourceHandlerSlot(playerInventory.player, this.resourceHandler, machine::awardUsedRecipes, 2, 109, 25));
        addStandardInventorySlots(playerInventory, 8, 84);
    }

    public ElectricFurnaceMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(), playerInventory.player.level(), data), new SimpleContainerData(2));
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return EnumExtensions.RECIPE_BOOK_TYPE_HEATING.getValue();
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
            public boolean recipeMatches(RecipeHolder<HeatingRecipe> recipe) {
                return recipe.value().matches(new SingleRecipeInput(getSlot(0).getItem()), ElectricFurnaceMenu.this.level);
            }
        }, 1, 1, List.of(getSlot(0)), craftingSlots, playerInventory, (RecipeHolder<HeatingRecipe>) recipe, useMaxItems, isCreative);
    }
}
