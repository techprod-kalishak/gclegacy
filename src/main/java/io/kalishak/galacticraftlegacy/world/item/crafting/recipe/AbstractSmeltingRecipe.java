/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

import java.util.List;


public abstract class AbstractSmeltingRecipe extends MachineRecipe<SingleRecipeInput> {
    protected final AbstractCookingRecipe.CookingBookInfo cookingCategory;
    protected final Ingredient ingredient;
    protected final int cookingTime;

    protected AbstractSmeltingRecipe(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo cookingCategory, Ingredient ingredient, ItemStackTemplate result, int cookingTime) {
        super(commonInfo, result);
        this.cookingCategory = cookingCategory;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
    }

    @Override
    public abstract RecipeSerializer<? extends AbstractSmeltingRecipe> getSerializer();

    @Override
    public abstract RecipeType<? extends AbstractSmeltingRecipe> getType();

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredient);
        }

        return this.placementInfo;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.ingredient.acceptsItem(input.getItem(0).typeHolder());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return switch (category()) {
            case BLOCKS -> GalacticraftRecipeBookCategories.HEATING_BLOCKS.get();
            case FOOD -> GalacticraftRecipeBookCategories.HEATING_FOOD.get();
            case MISC -> GalacticraftRecipeBookCategories.HEATING_MISC.get();
        };
    }

    protected Ingredient ingredient() {
        return this.ingredient;
    }

    public int cookingTime() {
        return this.cookingTime;
    }

    public CookingBookCategory category() {
        return this.cookingCategory.category();
    }

    @Override
    public String group() {
        return this.cookingCategory.group();
    }

    protected abstract Holder<Item> icon();

    @Override
    public abstract List<RecipeDisplay> display();

    @FunctionalInterface
    public interface Factory<T extends AbstractSmeltingRecipe> {
        T create(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo cookingBookInfo, Ingredient ingredient, ItemStackTemplate result, int cookingTime);
    }

    public static <T extends AbstractSmeltingRecipe> RecipeSerializer<T> recipeSerializer(Factory<T> factory, int defaultCookingTime) {
        return new RecipeSerializer<>(
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        CommonInfo.MAP_CODEC.forGetter(x -> x.commonInfo),
                        AbstractCookingRecipe.CookingBookInfo.MAP_CODEC.forGetter(x -> x.cookingCategory),
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(AbstractSmeltingRecipe::ingredient),
                        ItemStackTemplate.CODEC.fieldOf("result").forGetter(AbstractSmeltingRecipe::result),
                        Codec.INT.fieldOf("cookingtime").orElse(defaultCookingTime).forGetter(AbstractSmeltingRecipe::cookingTime)
                ).apply(instance, factory::create)),
                StreamCodec.composite(
                        CommonInfo.STREAM_CODEC, x -> x.commonInfo,
                        AbstractCookingRecipe.CookingBookInfo.STREAM_CODEC, x -> x.cookingCategory,
                        Ingredient.CONTENTS_STREAM_CODEC, AbstractSmeltingRecipe::ingredient,
                        ItemStackTemplate.STREAM_CODEC, AbstractSmeltingRecipe::result,
                        ByteBufCodecs.INT, AbstractSmeltingRecipe::cookingTime,
                        factory::create
                )
        );
    }
}
