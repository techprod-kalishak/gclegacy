/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.recipebook;

import io.kalishak.galacticraftlegacy.world.inventory.machine.AbstractElectricFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.ElectricFurnaceRecipeDisplay;
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

public class ElectricFurnaceRecipeBookComponent extends RecipeBookComponent<AbstractElectricFurnaceMenu<?, ?>> {
    protected final WidgetSprites widgetSprites;
    protected final Component filterName;

    public ElectricFurnaceRecipeBookComponent(AbstractElectricFurnaceMenu<?, ?> menu, WidgetSprites widgetSprites, Component filterName, List<TabInfo> tabInfos) {
        super(menu, tabInfos);
        this.widgetSprites = widgetSprites;
        this.filterName = filterName;
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return this.widgetSprites;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index < 3;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedItemContents) {
        collection.selectRecipes(stackedItemContents, recipeDisplay -> recipeDisplay instanceof ElectricFurnaceRecipeDisplay);
    }

    @Override
    protected Component getRecipeFilterName() {
        return this.filterName;
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
        ghostSlots.setResult(this.menu.getSlot(2), contextMap, recipeDisplay.result());

        if (recipeDisplay instanceof ElectricFurnaceRecipeDisplay electricFurnaceRecipeDisplay) {
            ghostSlots.setInput(this.menu.getSlot(0), contextMap, electricFurnaceRecipeDisplay.ingredient());

            Slot slot = this.menu.slots.get(1);
            if (!slot.hasItem()) {
                ghostSlots.setInput(
                        slot,
                        contextMap,
                        EnergyRecipeBookComponent.getEnergyDisplay(slot, 250, electricFurnaceRecipeDisplay.battery())
                );
            }
        }
    }
}
