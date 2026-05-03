/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.inventory.slot.FuelHandlerSlot;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.AnvilCompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.CompressorBlockEntity;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.List;

public class CompressorMenu extends AbstractCompressorMenu<CompressorBlockEntity> {
    public CompressorMenu(int containerId, Inventory playerInventory, CompressorBlockEntity compressor, ContainerData dataAccess) {
        super(GalacticraftMenuType.COMPRESSOR.get(), containerId,  playerInventory, compressor, dataAccess);

        ResourceHandler<ItemResource> resourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, compressor, null);
        addCompressorGrid(resourceHandler, compressor::set, 19, 18);
        addSlot(new ResourceHandlerSlot(resourceHandler, ResourcefulHelper::notPlaceable, 9, 138, 38));
        addSlot(new FuelHandlerSlot(resourceHandler, compressor::set, 10, 55, 75));

        addStandardInventorySlots(playerInventory, 8, 110);
    }

    public CompressorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.COMPRESSOR.get(), playerInventory.player.level(), data), new SimpleContainerData(4));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    public float getLitProgress() {
        int litDuration = this.dataAccess.get(3);
        if (litDuration == 0) {
            litDuration = 200;
        }

        return Mth.clamp((float) this.dataAccess.get(2) / litDuration, 0.0F, 1.0F);
    }

    public boolean isLit() {
        return this.dataAccess.get(2) > 0;
    }

    @Override
    public PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, RecipeHolder<?> recipe, ServerLevel level, Inventory inventory) {
        final List<Slot> slotsToClear = this.slots.subList(0, 9);
        return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
                CompressorMenu.this.fillCraftSlotsStackedContents(stackedContents);
            }

            @Override
            public void clearCraftingContent() {
                slotsToClear.forEach(s -> s.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<AnvilCompressingRecipe> recipe) {
                return recipe.value().matches(new CompressingRecipeInput(3, 3, () -> ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, CompressorMenu.this.compressor, null)), level);
            }
        }, 3, 3, this.slots.subList(0, 8), slotsToClear, inventory, (RecipeHolder<AnvilCompressingRecipe>) recipe, useMaxItems, allowDroppingItemsToClear);
    }
}
