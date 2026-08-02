/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.recipebook;

import io.kalishak.galacticraftlegacy.world.inventory.magnetic_crafting.MagneticCraftingMenu;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;

import java.util.List;

public class MagneticRecipeBookComponent extends RecipeBookComponent<MagneticCraftingMenu> {
    private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
            Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
            Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"),
            Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted")
    );
    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");
    private static final List<TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.CRAFTING),
            new RecipeBookComponent.TabInfo(Items.IRON_AXE, Items.GOLDEN_SWORD, RecipeBookCategories.CRAFTING_EQUIPMENT),
            new RecipeBookComponent.TabInfo(Items.BRICKS, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS),
            new RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.APPLE, RecipeBookCategories.CRAFTING_MISC),
            new RecipeBookComponent.TabInfo(Items.REDSTONE, RecipeBookCategories.CRAFTING_REDSTONE)
    );

    public MagneticRecipeBookComponent(MagneticCraftingMenu menu) {
        super(menu, TABS);
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return this.menu.slots.getFirst() == slot || this.menu.getInputSlots().contains(slot);
    }

    private boolean canDisplay(RecipeDisplay display) {
        return switch (display) {
            case ShapedCraftingRecipeDisplay shaped -> 3 >= shaped.width() && 3 >= shaped.height();
            case ShapelessCraftingRecipeDisplay shapeless -> 9 >= shapeless.ingredients().size();
            default -> false;
        };
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
        ghostSlots.setResult(this.menu.slots.getFirst(), context, recipe.result());

        switch (recipe) {
            case ShapedCraftingRecipeDisplay shaped -> {
                List<Slot> inputSlots = this.menu.getInputSlots();
                PlaceRecipeHelper.placeRecipe(
                        3,
                        3,
                        shaped.width(),
                        shaped.height(),
                        shaped.ingredients(),
                        (ingredient, gridIndex, _, _) -> {
                            Slot slot = inputSlots.get(gridIndex);
                            ghostSlots.setInput(slot, context, ingredient);
                        }
                );
            }

            case ShapelessCraftingRecipeDisplay shapeless -> {
                List<Slot> inputSlots = this.menu.getInputSlots();
                int slotCount = Math.min(shapeless.ingredients().size(), inputSlots.size());

                for (int i = 0; i < slotCount; i++) {
                    ghostSlots.setInput(inputSlots.get(i), context, shapeless.ingredients().get(i));
                }
            }

            default -> {}
        }
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_BUTTON_SPRITES;
    }

    @Override
    protected Component getRecipeFilterName() {
        return ONLY_CRAFTABLES_TOOLTIP;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
        collection.selectRecipes(stackedContents, this::canDisplay);
    }
}
