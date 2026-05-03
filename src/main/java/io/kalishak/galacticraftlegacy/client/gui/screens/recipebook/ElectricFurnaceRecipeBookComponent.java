/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.recipebook;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricFurnaceMenu;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class ElectricFurnaceRecipeBookComponent extends RecipeBookComponent<ElectricFurnaceMenu> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            Constants.id("recipe_book/electric_furnace_filter_enabled"),
            Constants.id("recipe_book/electric_furnace_filter_disabled"),
            Constants.id("recipe_book/electric_furnace_filter_enabled_highlighted"),
            Constants.id("recipe_book/electric_furnace_filter_disabled_highlighted")
    );
    private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.heatable");

    public ElectricFurnaceRecipeBookComponent(ElectricFurnaceMenu menu, List<TabInfo> tabInfos) {
        super(menu, tabInfos);
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index < 3;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedItemContents) {
        collection.selectRecipes(stackedItemContents, recipeDisplay -> recipeDisplay instanceof FurnaceRecipeDisplay);
    }

    @Override
    protected Component getRecipeFilterName() {
        return FILTER_NAME;
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
        ghostSlots.setResult(this.menu.getSlot(2), contextMap, recipeDisplay.result());

        if (recipeDisplay instanceof FurnaceRecipeDisplay furnaceRecipeDisplay) {
            ghostSlots.setInput(this.menu.getSlot(0), contextMap, furnaceRecipeDisplay.ingredient());

//            Slot slot = this.menu.slots.get(1);
//            if (!slot.hasItem()) {
//                ghostSlots.setInput(slot, contextMap, furnaceRecipeDisplay.battery());
//            }
        }
    }
}
