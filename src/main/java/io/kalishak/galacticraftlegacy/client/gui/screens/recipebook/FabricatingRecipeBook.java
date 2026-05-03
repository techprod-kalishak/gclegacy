/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.recipebook;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.inventory.machine.CircuitFabricatorMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.CircutFabricatorRecipeDisplay;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.CircuitFabricatorBlockEntity;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class FabricatingRecipeBook extends RecipeBookComponent<CircuitFabricatorMenu> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            Constants.id("recipe_book/circuit_fabricator_filter_enabled"),
            Constants.id("recipe_book/circuit_fabricator_filter_disabled"),
            Constants.id("recipe_book/circuit_fabricator_filter_enabled_highlighted"),
            Constants.id("recipe_book/circuit_fabricator_filter_disabled_highlighted")
    );

    public FabricatingRecipeBook(CircuitFabricatorMenu menu, List<TabInfo> tabInfos) {
        super(menu, tabInfos);
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index > 0 && slot.index < 5;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection possibleRecipes, StackedItemContents stackedItemContents) {
        possibleRecipes.selectRecipes(stackedItemContents, recipeDisplay -> recipeDisplay instanceof CircutFabricatorRecipeDisplay);
    }

    @Override
    protected Component getRecipeFilterName() {
        return Component.empty();
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
        ghostSlots.setResult(this.menu.getSlot(CircuitFabricatorBlockEntity.SLOT_OUTPUT), contextMap, recipeDisplay.result());

        if (recipeDisplay instanceof CircutFabricatorRecipeDisplay fabricating) {
            for (int i = CircuitFabricatorBlockEntity.SLOT_DIAMOND; i < CircuitFabricatorBlockEntity.SLOT_OUTPUT; i++) {
                ghostSlots.setInput(this.menu.getSlot(i), contextMap, CircutFabricatorRecipeDisplay.ofIndex(fabricating, i));
            }

            if (!this.menu.getSlot(CircuitFabricatorBlockEntity.SLOT_BATTERY).hasItem()) {
                ghostSlots.setInput(this.menu.getSlot(CircuitFabricatorBlockEntity.SLOT_BATTERY), contextMap, fabricating.battery());
            }
        }
    }
}
