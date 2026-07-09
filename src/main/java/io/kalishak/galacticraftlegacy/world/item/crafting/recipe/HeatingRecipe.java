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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;

public class HeatingRecipe extends ElectricCookingRecipe {
    public static final MapCodec<HeatingRecipe> MAP_CODEC = cookingMapCodec(HeatingRecipe::new, 200);
    public static final StreamCodec<RegistryFriendlyByteBuf, HeatingRecipe> STREAM_CODEC = cookingStreamCodec(HeatingRecipe::new);

    public HeatingRecipe(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo cookingBookInfo, Ingredient ingredient, ItemStackTemplate result, int cookingTime) {
        super(commonInfo, cookingBookInfo, ingredient, result, cookingTime);
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
    public RecipeBookCategory recipeBookCategory() {
        return switch (category()) {
            case BLOCKS -> GalacticraftRecipeBookCategories.HEATING_BLOCKS.get();
            case FOOD -> GalacticraftRecipeBookCategories.HEATING_FOOD.get();
            case MISC -> GalacticraftRecipeBookCategories.HEATING_MISC.get();
        };
    }
}
