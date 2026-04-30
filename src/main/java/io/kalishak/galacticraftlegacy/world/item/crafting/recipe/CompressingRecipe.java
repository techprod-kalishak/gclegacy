/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.world.item.crafting.StaticRecipePattern;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.CompressorRecipeDisplay;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class CompressingRecipe implements Recipe<CompressingRecipeInput> {
    public final StaticRecipePattern pattern;
    protected final CommonInfo commonInfo;
    protected final String group;
    protected final ItemStackTemplate result;
    protected final int compressingTime;
    protected @Nullable PlacementInfo placementInfo;

    protected CompressingRecipe(CommonInfo commonInfo, String group, StaticRecipePattern pattern, ItemStackTemplate result, int compressingTime) {
        this.commonInfo = commonInfo;
        this.group = group;
        this.pattern = pattern;
        this.result = result;
        this.compressingTime = compressingTime;
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public abstract RecipeSerializer<? extends CompressingRecipe> getSerializer();

    @Override
    public abstract RecipeType<? extends CompressingRecipe> getType();

    @Override
    public String group() {
        return this.group;
    }

    protected abstract Holder<Item> icon();

    protected abstract SlotDisplay energySource();

    public int compressingTime() {
        return this.compressingTime;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.createFromOptionals(ingredients());
        }

        return this.placementInfo;
    }

    public List<Optional<Ingredient>> ingredients() {
        return this.pattern.ingredients();
    }

    public ItemStackTemplate result() {
        return this.result;
    }

    @Override
    public boolean matches(CompressingRecipeInput input, Level level) {
        return this.pattern.matches(input);
    }

    @Override
    public ItemStack assemble(CompressingRecipeInput input) {
        return this.result.create();
    }

    public int width() {
        return this.pattern.width();
    }

    public int height() {
        return this.pattern.height();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new CompressorRecipeDisplay(
                        width(),
                        height(),
                        ingredients().stream().map(optionalIngredient -> optionalIngredient.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(),
                        energySource(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(icon()),
                        this instanceof AnvilCompressingRecipe anvilCompressingRecipe ? anvilCompressingRecipe.experience() : 0.0F,
                        compressingTime()
                )
        );
    }

    @FunctionalInterface
    public interface Factory<T extends CompressingRecipe> {
        T create(CommonInfo commonInfo, String group, StaticRecipePattern pattern, ItemStackTemplate result, int compressingTime, float experience);
    }
}
