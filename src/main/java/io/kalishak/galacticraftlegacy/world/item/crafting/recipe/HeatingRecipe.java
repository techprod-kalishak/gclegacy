/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.ElectricFurnaceRecipeDisplay;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public class HeatingRecipe extends AbstractCookingRecipe {
    public static final MapCodec<HeatingRecipe> MAP_CODEC = cookingMapCodec(HeatingRecipe::new, 200);
    public static final StreamCodec<RegistryFriendlyByteBuf, HeatingRecipe> STREAM_CODEC = cookingStreamCodec(HeatingRecipe::new);

    public HeatingRecipe(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo cookingBookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
        super(commonInfo, cookingBookInfo, ingredient, result, experience, cookingTime);
    }

    @Override
    protected Item furnaceIcon() {
        return GalacticraftItems.ELECTRIC_FURNACE.get();
    }

    @Override
    public RecipeSerializer<HeatingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.HEATING.get();
    }

    @Override
    public RecipeType<HeatingRecipe> getType() {
        return GalacticraftRecipeType.HEATING.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ElectricFurnaceRecipeDisplay(
                        input().display(),
                        new SlotDisplay.ItemSlotDisplay(GalacticraftItems.BATTERY),
                        new SlotDisplay.ItemStackSlotDisplay(result()),
                        new SlotDisplay.ItemSlotDisplay(furnaceIcon()),
                        cookingTime()
                )
        );
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return switch (category()) {
            case BLOCKS -> GalacticraftRecipeBookCategories.HEATING_BLOCKS.get();
            case FOOD -> GalacticraftRecipeBookCategories.HEATING_FOOD.get();
            case MISC -> GalacticraftRecipeBookCategories.HEATING_MISC.get();
        };
    }
}
