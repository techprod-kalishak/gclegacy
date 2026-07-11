/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.ElectricFurnaceRecipeDisplay;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public abstract class ElectricCookingRecipe extends SingleItemRecipe {
    protected final AbstractCookingRecipe.CookingBookInfo bookInfo;
    private final int cookingTime;

    public ElectricCookingRecipe(CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, int cookingTime) {
        super(commonInfo, ingredient, result);
        this.bookInfo = bookInfo;
        this.cookingTime = cookingTime;
    }

    public static <T extends ElectricCookingRecipe> MapCodec<T> cookingMapCodec(ElectricCookingRecipe.Factory<T> factory, int defaultCookingTime) {
        return RecordCodecBuilder.mapCodec(
                i -> i.group(
                                Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                                AbstractCookingRecipe.CookingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                                Ingredient.CODEC.fieldOf("ingredient").forGetter(ElectricCookingRecipe::input),
                                ItemStackTemplate.CODEC.fieldOf("result").forGetter(ElectricCookingRecipe::result),
                                Codec.INT.fieldOf("cookingtime").orElse(defaultCookingTime).forGetter(ElectricCookingRecipe::cookingTime)
                        )
                        .apply(i, factory::create)
        );
    }

    public static <T extends ElectricCookingRecipe> StreamCodec<RegistryFriendlyByteBuf, T> cookingStreamCodec(ElectricCookingRecipe.Factory<T> factory) {
        return StreamCodec.composite(
                Recipe.CommonInfo.STREAM_CODEC,
                o -> o.commonInfo,
                AbstractCookingRecipe.CookingBookInfo.STREAM_CODEC,
                o -> o.bookInfo,
                Ingredient.CONTENTS_STREAM_CODEC,
                ElectricCookingRecipe::input,
                ItemStackTemplate.STREAM_CODEC,
                ElectricCookingRecipe::result,
                ByteBufCodecs.INT,
                ElectricCookingRecipe::cookingTime,
                factory::create
        );
    }

    public int cookingTime() {
        return this.cookingTime;
    }

    public CookingBookCategory category() {
        return this.bookInfo.category();
    }

    @Override
    public String group() {
        return this.bookInfo.group();
    }

    protected abstract Item furnaceIcon();

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ElectricFurnaceRecipeDisplay(
                        input().display(),
                        new SlotDisplay.ItemSlotDisplay(GalacticraftItems.BATTERY),
                        new SlotDisplay.ItemStackSlotDisplay(result()),
                        new SlotDisplay.ItemSlotDisplay(furnaceIcon()),
                        cookingTime()
                )
        );
    }

    @FunctionalInterface
    public interface Factory<R extends ElectricCookingRecipe> {
        R create(
                Recipe.CommonInfo commonInfo,
                AbstractCookingRecipe.CookingBookInfo cookingBookInfo,
                Ingredient ingredient,
                ItemStackTemplate result,
                int cookingTime
        );
    }
}
