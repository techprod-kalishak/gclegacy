/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class ArcHeatingRecipe extends AbstractSmeltingRecipe {
    public ArcHeatingRecipe(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo cookingBookInfo, Ingredient ingredient, ItemStackTemplate result, int cookingTime) {
        super(commonInfo, cookingBookInfo, ingredient, result, cookingTime);
    }

    @Override
    protected Holder<Item> icon() {
        return GalacticraftItems.ELECTRIC_FURNACE;
    }

    @Override
    public RecipeSerializer<ArcHeatingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.ARC_HEATING.get();
    }

    @Override
    public RecipeType<ArcHeatingRecipe> getType() {
        return GalacticraftRecipeType.ARC_HEATING.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of();
    }
}
