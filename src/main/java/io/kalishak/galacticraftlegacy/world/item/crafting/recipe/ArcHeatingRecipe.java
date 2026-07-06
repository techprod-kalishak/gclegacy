/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class ArcHeatingRecipe extends AbstractCookingRecipe {
    public static final MapCodec<ArcHeatingRecipe> MAP_CODEC = cookingMapCodec(ArcHeatingRecipe::new, 50);
    public static final StreamCodec<RegistryFriendlyByteBuf, ArcHeatingRecipe> STREAM_CODEC = cookingStreamCodec(ArcHeatingRecipe::new);

    public ArcHeatingRecipe(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo cookingBookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
        super(commonInfo, cookingBookInfo, ingredient, result, experience, cookingTime);
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
    public List<RecipeDisplay> display() {
        return List.of();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }
}
