/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.Nullable;

public abstract class MachineRecipe<I extends RecipeInput> implements Recipe<I> {
    protected final Recipe.CommonInfo commonInfo;
    protected final ItemStackTemplate result;
    protected @Nullable PlacementInfo placementInfo;

    protected MachineRecipe(Recipe.CommonInfo commonInfo, ItemStackTemplate result) {
        this.commonInfo = commonInfo;
        this.result = result;
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public abstract RecipeSerializer<? extends MachineRecipe<I>> getSerializer();

    @Override
    public abstract RecipeType<? extends MachineRecipe<I>> getType();

    protected ItemStackTemplate result() {
        return this.result;
    }

    @Override
    public ItemStack assemble(I input) {
        return this.result.create();
    }
}
