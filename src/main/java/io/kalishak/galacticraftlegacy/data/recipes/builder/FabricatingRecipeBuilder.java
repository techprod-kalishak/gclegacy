/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.recipes.builder;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.item.crafting.FabricatingBookCategory;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CircuitRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class FabricatingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory craftingCategory;
    private final FabricatingBookCategory fabricatingCategory;
    private final ItemStackTemplate result;
    private final Ingredient ingredient;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String groupName;

    private FabricatingRecipeBuilder(RecipeCategory craftingCategory, FabricatingBookCategory fabricatingCategory, ItemStackTemplate result, Ingredient ingredient) {
        this.craftingCategory = craftingCategory;
        this.fabricatingCategory = fabricatingCategory;
        this.result = result;
        this.ingredient = ingredient;
    }

    public static FabricatingRecipeBuilder classic(Holder<Item> result, int count, Ingredient mainIngredient, FabricatingBookCategory fabricatingCategory) {
        return new FabricatingRecipeBuilder(RecipeCategory.MISC, fabricatingCategory, new ItemStackTemplate(result, count), mainIngredient);
    }

    @Override
    public FabricatingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public FabricatingRecipeBuilder group(@Nullable String groupName) {
        this.groupName = groupName;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return Constants.key(Registries.RECIPE, this.result.typeHolder().getRegisteredName());
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        CircuitRecipe recipe = new CircuitRecipe(
                RecipeBuilder.createCraftingCommonInfo(true),
                new CircuitRecipe.FabricatingBookInfo(this.fabricatingCategory, Objects.requireNonNullElse(this.groupName, "")),
                this.ingredient,
                this.result
        );

        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.craftingCategory));
    }
}
