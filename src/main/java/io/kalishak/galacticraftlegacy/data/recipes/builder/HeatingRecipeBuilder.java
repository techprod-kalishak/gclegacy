package io.kalishak.galacticraftlegacy.data.recipes.builder;

import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.AbstractSmeltingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ArcHeatingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.HeatingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeatingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final CookingBookCategory bookCategory;
    private final ItemStack result;
    private final Ingredient ingredient;
    private final int cookingTime;
    private final HeatingRecipe.Factory<?> factory;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private @Nullable String groupName;

    private HeatingRecipeBuilder(RecipeCategory category, CookingBookCategory bookCategory, ItemStack result, Ingredient ingredient, int cookingTime, AbstractSmeltingRecipe.Factory<?> factory) {
        this.category = category;
        this.bookCategory = bookCategory;
        this.result = result;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
        this.factory = factory;
    }

    public static HeatingRecipeBuilder generic(RecipeCategory recipeCategory, ItemLike result, int count, Ingredient ingredient, int cookingTime, HeatingRecipe.Factory<?> factory) {
        return new HeatingRecipeBuilder(recipeCategory, determineSmeltingRecipeCategory(result), new ItemStack(result, count), ingredient, cookingTime, factory);
    }

    public static HeatingRecipeBuilder heating(RecipeCategory recipeCategory, ItemLike result, Ingredient ingredient, int cookingTime) {
        return generic(recipeCategory, result, 1, ingredient, cookingTime, HeatingRecipe::new);
    }

    public static HeatingRecipeBuilder heating(RecipeCategory recipeCategory, ItemLike result, Ingredient ingredient) {
        return heating(recipeCategory, result, ingredient, 100);
    }

    public static HeatingRecipeBuilder arcHeating(RecipeCategory recipeCategory, ItemLike result, Ingredient ingredient, int cookingTime) {
        return generic(recipeCategory, result, 1, ingredient, cookingTime, ArcHeatingRecipe::new);
    }

    public static HeatingRecipeBuilder arcHeating(RecipeCategory recipeCategory, ItemLike result, Ingredient ingredient) {
        return arcHeating(recipeCategory, result, ingredient, 50);
    }

    private static CookingBookCategory determineSmeltingRecipeCategory(ItemLike result) {
        if (result.asItem().components().has(DataComponents.FOOD)) {
            return CookingBookCategory.FOOD;
        } else {
            return result.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
        }
    }

    @Override
    public HeatingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public HeatingRecipeBuilder group(@Nullable String groupName) {
        this.groupName = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
        ensureValid(resourceKey);
        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        AbstractSmeltingRecipe recipe = this.factory.create(this.groupName == null ? "" : this.groupName, this.bookCategory, this.ingredient, this.result, this.cookingTime);
        output.accept(resourceKey, recipe, advancementBuilder.build(resourceKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceKey<Recipe<?>> recipe) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipe.identifier());
        }
    }
}
