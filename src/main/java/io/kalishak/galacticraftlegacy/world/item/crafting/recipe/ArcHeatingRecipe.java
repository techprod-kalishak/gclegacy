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

public class ArcHeatingRecipe extends ElectricCookingRecipe {
    public static final MapCodec<ArcHeatingRecipe> MAP_CODEC = cookingMapCodec(ArcHeatingRecipe::new, 100);
    public static final StreamCodec<RegistryFriendlyByteBuf, ArcHeatingRecipe> STREAM_CODEC = cookingStreamCodec(ArcHeatingRecipe::new);

    public ArcHeatingRecipe(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo cookingBookInfo, Ingredient ingredient, ItemStackTemplate result, int cookingTime) {
        super(commonInfo, cookingBookInfo, ingredient, result, cookingTime);
    }

    @Override
    protected Item furnaceIcon() {
        return GalacticraftItems.ELECTRIC_FURNACE.get();
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
    public RecipeBookCategory recipeBookCategory() {
        return switch (category()) {
            case BLOCKS -> GalacticraftRecipeBookCategories.ARC_HEATING_BLOCKS.get();
            case FOOD -> GalacticraftRecipeBookCategories.ARC_HEATING_FOOD.get();
            case MISC -> GalacticraftRecipeBookCategories.ARC_HEATING_MISC.get();
        };
    }
}
