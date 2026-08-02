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
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingRecipe;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class VehicleCraftingRecipeBuilder implements RecipeBuilder {
    private final Holder<VehicleCraftingDataRecipe> recipeHolder;
    private final VehicleCraftingBookCategory craftingCategory;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String groupName;

    private VehicleCraftingRecipeBuilder(Holder<VehicleCraftingDataRecipe> recipeHolder, VehicleCraftingBookCategory craftingCategory) {
        this.recipeHolder = recipeHolder;
        this.craftingCategory = craftingCategory;
    }

    public static VehicleCraftingRecipeBuilder generic(HolderGetter<VehicleCraftingDataRecipe> holderGetter, ResourceKey<VehicleCraftingDataRecipe> key, VehicleCraftingBookCategory craftingCategory) {
        return new VehicleCraftingRecipeBuilder(holderGetter.getOrThrow(key), craftingCategory);
    }

    public static VehicleCraftingRecipeBuilder rocket(HolderGetter<VehicleCraftingDataRecipe> holderGetter, ResourceKey<VehicleCraftingDataRecipe> key) {
        return new VehicleCraftingRecipeBuilder(holderGetter.getOrThrow(key), VehicleCraftingBookCategory.ROCKET).group("rocket");
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

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return Constants.key(Registries.RECIPE, this.recipeHolder.value().resultItem().typeHolder().getRegisteredName());
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        VehicleCraftingRecipe recipe = new VehicleCraftingRecipe(
                RecipeBuilder.createCraftingCommonInfo(true),
                this.recipeHolder,
                new VehicleCraftingRecipe.VehicleCraftingBookInfo(this.craftingCategory, Objects.requireNonNullElse(this.groupName, ""))
        );

        output.accept(id, recipe, this.advancementBuilder.build(output, id, RecipeCategory.MISC));
    }
}
