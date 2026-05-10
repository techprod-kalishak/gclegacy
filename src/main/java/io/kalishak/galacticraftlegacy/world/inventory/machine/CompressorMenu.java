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
import io.kalishak.galacticraftlegacy.world.inventory.slot.ResultResourceHandlerSlot;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.AnvilCompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AlloyCompressor;
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

import java.util.List;

public class CompressorMenu extends AbstractCompressorMenu {
    public CompressorMenu(int containerId, Inventory playerInventory, CompressorBlockEntity compressor, ContainerData dataAccess) {
        super(GalacticraftMenuType.COMPRESSOR.get(), containerId,  playerInventory, compressor, dataAccess);

        addCompressorGrid(this.compressorInventory, compressor::set, 19, 18);
        addSlot(new ResultResourceHandlerSlot(playerInventory.player, this.compressorInventory, compressor::awardUsedRecipes, AlloyCompressor.RESULT_SLOT_START, 138, 38));
        addSlot(new FuelHandlerSlot(this.compressorInventory, compressor::set, AlloyCompressor.FUEL_SLOT, 55, 75));

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
                CompressorMenu.this.fillCraftSlotsStackedContents(stackedContents);
            }

            @Override
            public void clearCraftingContent() {
                slotsToClear.forEach(s -> s.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<AnvilCompressingRecipe> recipe) {
                return recipe.value().matches(new CompressingRecipeInput(3, 3, () -> CompressorMenu.this.compressorInventory), level);
            }
        }, 3, 3, this.slots.subList(AlloyCompressor.CRAFTING_SLOT_START, AlloyCompressor.CRAFTING_SLOT_END), slotsToClear, inventory, (RecipeHolder<AnvilCompressingRecipe>) recipe, useMaxItems, allowDroppingItemsToClear);
    }
}
