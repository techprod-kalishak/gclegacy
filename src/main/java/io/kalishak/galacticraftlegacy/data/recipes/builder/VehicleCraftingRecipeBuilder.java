/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.recipes.builder;

import io.kalishak.galacticraftlegacy.advancements.SchematicUnlockedTrigger;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.item.crafting.VehicleCraftingBookCategory;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingEntry;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingRecipe;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VehicleCraftingRecipeBuilder implements RecipeBuilder {
    private final ResourceKey<VehicleCraftingDataRecipe> recipeDataKey;
    private final HolderGetter<VehicleCraftingDataRecipe> recipes;
    private final VehicleCraftingBookCategory craftingCategory;
    private final List<Ingredient> ingredients;
    private final Holder<Item> result;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String groupName;

    private VehicleCraftingRecipeBuilder(HolderGetter<VehicleCraftingDataRecipe> recipes, ResourceKey<VehicleCraftingDataRecipe> recipeDataKey, VehicleCraftingBookCategory craftingCategory, List<Ingredient> ingredients, Holder<Item> result) {
        this.recipes = recipes;
        this.recipeDataKey = recipeDataKey;
        this.craftingCategory = craftingCategory;
        this.ingredients = ingredients;
        this.result = result;
    }

    public static VehicleCraftingRecipeBuilder generic(HolderGetter<VehicleCraftingDataRecipe> recipes, ResourceKey<VehicleCraftingDataRecipe> recipeDataKey, VehicleCraftingBookCategory craftingCategory, Holder<Item> result) {
        return new VehicleCraftingRecipeBuilder(recipes, recipeDataKey, craftingCategory, new ArrayList<>(), result);
    }

    public static VehicleCraftingRecipeBuilder rocket(HolderGetter<VehicleCraftingDataRecipe> recipes, ResourceKey<VehicleCraftingDataRecipe> recipeDataKey, Holder<Item> result) {
        return new VehicleCraftingRecipeBuilder(recipes, recipeDataKey, VehicleCraftingBookCategory.ROCKET, new ArrayList<>(), result).group("rocket");
    }

    @Override
    public VehicleCraftingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    public VehicleCraftingRecipeBuilder hasSchematic(ResourceKey<SchematicVariant> schematicKey) {
        Criterion<?> criterion = SchematicUnlockedTrigger.TriggerInstance.playerUnlockedSchematic(schematicKey);
        return unlockedBy("has_schematic_" + schematicKey.identifier().getPath(), criterion);
    }

    @Override
    public VehicleCraftingRecipeBuilder group(@Nullable String groupName) {
        this.groupName = groupName;
        return this;
    }

    public VehicleCraftingRecipeBuilder withIngredient(HolderSet<Item> ingredient) {
        this.ingredients.add(Ingredient.of(ingredient));
        return this;
    }

    public VehicleCraftingRecipeBuilder withIngredient(ItemLike ingredient) {
        this.ingredients.add(Ingredient.of(ingredient));
        return this;
    }

    public VehicleCraftingRecipeBuilder withIngredients(HolderSet<Item> ingredient, int count) {
        for (int i = 0; i < count; i++) {
            this.ingredients.add(Ingredient.of(ingredient));
        }

        return this;
    }

    public VehicleCraftingRecipeBuilder withIngredients(ItemLike ingredient, int count) {
        for (int i = 0; i < count; i++) {
            this.ingredients.add(Ingredient.of(ingredient));
        }

        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return ResourceKey.create(Registries.RECIPE, Identifier.parse(this.result.getRegisteredName()));
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        VehicleCraftingRecipe recipe = new VehicleCraftingRecipe(
                RecipeBuilder.createCraftingCommonInfo(true),
                this.recipeDataKey,
                new VehicleCraftingRecipe.VehicleCraftingBookInfo(this.craftingCategory, Objects.requireNonNullElse(this.groupName, "")),
                this.ingredients,
                new ItemStackTemplate(this.result)
        );

        output.accept(id, recipe, this.advancementBuilder.build(output, id, RecipeCategory.MISC));
    }
}
