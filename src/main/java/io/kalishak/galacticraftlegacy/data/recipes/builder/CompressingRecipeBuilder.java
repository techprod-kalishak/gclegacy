/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.recipes.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.AnvilCompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ElectricCompressingRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CompressingRecipeBuilder implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final RecipeCategory craftingCategory;
    private final ItemStackTemplate result;
    private final int compressingTime;
    private final float experience;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> keys = Maps.newLinkedHashMap();
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new  RecipeUnlockAdvancementBuilder();
    private final CompressingRecipe.Factory<?> factory;
    private @Nullable String group;

    private CompressingRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result, int compressingTime, float experience, CompressingRecipe.Factory<?> factory) {
        this.items = items;
        this.craftingCategory = category;
        this.result = result;
        this.compressingTime = compressingTime;
        this.experience = experience;
        this.factory = factory;
    }

    private CompressingRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count, int compressingTime, float experience, CompressingRecipe.Factory<?> factory) {
        this(items, category, new ItemStackTemplate(result.asItem(), count), compressingTime, experience, factory);
    }

    public static CompressingRecipeBuilder generic(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count, int compressingTime, float experience, CompressingRecipe.Factory<?> factory) {
        return new CompressingRecipeBuilder(items, category, item, count, compressingTime, experience, factory);
    }

    public static CompressingRecipeBuilder classic(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count, int compressingTime, float experience) {
        return new CompressingRecipeBuilder(items, category, item, count, compressingTime, experience, AnvilCompressingRecipe::new);
    }

    public static CompressingRecipeBuilder electric(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count, int compressingTime) {
        return new CompressingRecipeBuilder(items, category, item, count, compressingTime, 0.0F, (commonInfo, group, pattern, result, time, _) -> new ElectricCompressingRecipe(commonInfo, group, pattern, result, time));
    }

    public CompressingRecipeBuilder define(Character symbol, Ingredient ingredient) {
        if (this.keys.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.keys.put(symbol, ingredient);
            return this;
        }
    }

    public CompressingRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        return define(symbol, Ingredient.of(this.items.getOrThrow(tag)));
    }

    public CompressingRecipeBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(item));
    }

    public CompressingRecipeBuilder pattern(String row) {
        if (!this.rows.isEmpty() && row.length() != this.rows.getFirst().length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(row);
            return this;
        }
    }

    @Override
    public CompressingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public CompressingRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return Constants.key(Registries.RECIPE, this.result.typeHolder().unwrapKey().orElseThrow().identifier().getPath());
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        ShapedRecipePattern pattern = ShapedRecipePattern.of(this.keys, this.rows);
        CompressingRecipe recipe = this.factory.create(
                RecipeBuilder.createCraftingCommonInfo(true),
                Objects.requireNonNullElse(this.group, ""),
                pattern,
                this.result,
                this.compressingTime,
                this.experience
        );
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.craftingCategory));
    }
}
