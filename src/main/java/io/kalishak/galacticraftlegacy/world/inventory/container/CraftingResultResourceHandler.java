/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container;

import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class CraftingResultResourceHandler extends RangedResourceHandler<ItemResource> implements RecipeCraftingHolder {
    private @Nullable RecipeHolder<?> recipeUsed;

    public CraftingResultResourceHandler(Supplier<ResourceHandler<ItemResource>> delegate, int start, int end) {
        super(delegate, start, end);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeHolder) {
        this.recipeUsed = recipeHolder;
    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return this.recipeUsed;
    }
}
