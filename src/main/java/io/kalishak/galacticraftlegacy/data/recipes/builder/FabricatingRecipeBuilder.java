package io.kalishak.galacticraftlegacy.data.recipes.builder;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CircuitRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class FabricatingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final Ingredient ingredient;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private @Nullable String groupName;

    private FabricatingRecipeBuilder(RecipeCategory category, ItemStackTemplate result, Ingredient ingredient) {
        this.category = category;
        this.result = result;
        this.ingredient = ingredient;
    }

    public static FabricatingRecipeBuilder classic(Holder<Item> result, int count, Ingredient mainIngredient) {
        return new FabricatingRecipeBuilder(RecipeCategory.MISC, new ItemStackTemplate(result, count), mainIngredient);
    }

    @Override
    public FabricatingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public FabricatingRecipeBuilder group(@Nullable String groupName) {
        this.groupName = groupName;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return Constants.key(Registries.RECIPE, "basic_wafer");
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
        ensureValid(resourceKey);
        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        CircuitRecipe recipe = new CircuitRecipe(this.groupName == null ? "" : this.groupName, this.ingredient, this.result, true);
        output.accept(resourceKey, recipe, advancementBuilder.build(resourceKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceKey<Recipe<?>> recipe) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipe.identifier());
        }
    }
}
