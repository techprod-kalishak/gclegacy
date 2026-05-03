/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.recipebook;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.inventory.machine.AbstractCompressorMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.CompressorRecipeDisplay;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractCompressorBlockEntity;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class CompressorRecipeBookComponent extends RecipeBookComponent<AbstractCompressorMenu<?>> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            Constants.id("recipe_book/compressor_filter_enabled"),
            Constants.id("recipe_book/compressor_filter_disabled"),
            Constants.id("recipe_book/compressor_filter_enabled_highlighted"),
            Constants.id("recipe_book/compressor_filter_disabled_highlighted")
    );

    public CompressorRecipeBookComponent(AbstractCompressorMenu<?> menu, List<TabInfo> tabInfos) {
        super(menu, tabInfos);
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index < AbstractCompressorBlockEntity.RESULT_SLOT;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
        collection.selectRecipes(stackedContents, recipeDisplay -> recipeDisplay instanceof CompressorRecipeDisplay);
    }

    @Override
    protected Component getRecipeFilterName() {
        return CommonComponents.EMPTY;
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
        ghostSlots.setResult(this.menu.getSlot(AbstractCompressorBlockEntity.RESULT_SLOT), context, recipe.result());

        if (recipe instanceof CompressorRecipeDisplay compressorRecipeDisplay) {
            List<Slot> craftingSlots = this.menu.slots.subList(0, 8);
            PlaceRecipeHelper.placeRecipe(
                    3,
                    3,
                    3,
                    3,
                    compressorRecipeDisplay.ingredients(),
                    (ingredient, gridIndex, _, _) -> ghostSlots.setInput(craftingSlots.get(gridIndex), context, ingredient)
            );
        }
    }
}
