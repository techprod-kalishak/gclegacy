/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.recipes.builder;

import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ArcHeatingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ElectricCookingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.HeatingRecipe;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class ElectricCookingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory craftingCategory;
    private final CookingBookCategory cookingCategory;
    private final ItemStackTemplate result;
    private final Ingredient ingredient;
    private final int cookingTime;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String group;
    private final ElectricCookingRecipe.Factory<?> factory;

    private ElectricCookingRecipeBuilder(
            RecipeCategory craftingCategory,
            CookingBookCategory cookingCategory,
            ItemStackTemplate result,
            Ingredient ingredient,
            int cookingTime,
            ElectricCookingRecipe.Factory<?> factory
    ) {
        this.craftingCategory = craftingCategory;
        this.cookingCategory = cookingCategory;
        this.result = result;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
        this.factory = factory;
    }

    private ElectricCookingRecipeBuilder(
            RecipeCategory craftingCategory,
            CookingBookCategory cookingCategory,
            ItemLike result,
            Ingredient ingredient,
            int cookingTime,
            ElectricCookingRecipe.Factory<?> factory
    ) {
        this(craftingCategory, cookingCategory, new ItemStackTemplate(result.asItem()), ingredient, cookingTime, factory);
    }

    public static <T extends ElectricCookingRecipe> ElectricCookingRecipeBuilder generic(
            Ingredient ingredient,
            RecipeCategory craftingCategory,
            CookingBookCategory cookingCategory,
            ItemLike result,
            int cookingTime,
            ElectricCookingRecipe.Factory<T> factory
    ) {
        return new ElectricCookingRecipeBuilder(craftingCategory, cookingCategory, result, ingredient, cookingTime, factory);
    }

    public static ElectricCookingRecipeBuilder heating(
            Ingredient ingredient, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, int cookingTime
    ) {
        return new ElectricCookingRecipeBuilder(craftingCategory, cookingCategory, result, ingredient, cookingTime, HeatingRecipe::new);
    }

    public static ElectricCookingRecipeBuilder arcHeating(
            Ingredient ingredient, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, int cookingTime
    ) {
        return new ElectricCookingRecipeBuilder(craftingCategory, cookingCategory, result, ingredient, cookingTime, ArcHeatingRecipe::new);
    }

    @Override
    public ElectricCookingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public ElectricCookingRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        ElectricCookingRecipe recipe = this.factory
                .create(
                        RecipeBuilder.createCraftingCommonInfo(true),
                        new AbstractCookingRecipe.CookingBookInfo(this.cookingCategory, Objects.requireNonNullElse(this.group, "")),
                        this.ingredient,
                        this.result,
                        this.cookingTime
                );
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.craftingCategory));
    }
}
