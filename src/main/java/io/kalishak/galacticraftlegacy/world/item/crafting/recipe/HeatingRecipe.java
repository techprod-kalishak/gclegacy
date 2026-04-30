/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.ElectricFurnaceRecipeDisplay;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public class HeatingRecipe extends AbstractSmeltingRecipe {
    public HeatingRecipe(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo cookingBookInfo, Ingredient ingredient, ItemStackTemplate result, int cookingTime) {
        super(commonInfo, cookingBookInfo, ingredient, result, cookingTime);
    }

    @Override
    protected Holder<Item> icon() {
        return GalacticraftItems.ELECTRIC_FURNACE;
    }

    @Override
    public RecipeSerializer<? extends HeatingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.HEATING.get();
    }

    @Override
    public RecipeType<? extends HeatingRecipe> getType() {
        return GalacticraftRecipeType.HEATING.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ElectricFurnaceRecipeDisplay(
                        ingredient().display(),
                        new SlotDisplay.ItemSlotDisplay(GalacticraftItems.BATTERY),
                        new SlotDisplay.ItemStackSlotDisplay(result()),
                        new SlotDisplay.ItemSlotDisplay(icon()),
                        this.cookingTime
                )
        );
    }
}
