/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface AlloyCompressor extends MenuProvider, StackedContentsCompatible, RecipeCraftingHolder, WorldlyContainer {
    int CRAFTING_SLOT_START = 0;
    int CRAFTING_SLOT_END = 8;
    int FUEL_SLOT = 9;
    int RESULT_SLOT_START = 10;
    int RESULT_SLOT_END = 11;
    int DATA_SLOT_COMPRESSING_TIMER = 0;
    int DATA_SLOT_COMPRESSING_TIME_TOTAL = 1;
    int DATA_SLOT_LIT_TIMER = 2;
    int DATA_SLOT_LIT_TIME_TOTAL = 3;
    int INVENTORY_SIZE_BASIC = 11;
    int INVENTORY_SIZE_ADVANCED = 12;
    int[] SLOTS_FOR_INPUT = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    int[] SLOTS_FOR_FUEL = new int[] { 9 };

    int[] getSlotsForOutput();

    List<ItemStack> getCraftingItems();

    void set(int index, ItemResource resource, int amount);

    @Override
    default void setRecipeUsed(@Nullable RecipeHolder<?> recipeHolder) {
    }

    @Override
    default @Nullable RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    default int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.UP) {
            return SLOTS_FOR_INPUT;
        } else if (direction == Direction.DOWN) {
            return getSlotsForOutput();
        }

        return SLOTS_FOR_FUEL;
    }

    @Override
    default boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    default boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return true;
    }

    static boolean canCompress(NonNullList<ItemStack> items, int maxStackSize, ItemStack recipeResult) {
        ItemStack resultItemStack = items.get(10);

        if (resultItemStack.isEmpty()) {
            return true;
        } else if (!ItemStack.isSameItemSameComponents(resultItemStack, recipeResult)) {
            return false;
        } else {
            int resultCount = resultItemStack.getCount() + recipeResult.count();
            int maxResultCount = Math.min(maxStackSize, recipeResult.getMaxStackSize());
            return resultCount <= maxResultCount;
        }
    }

    static void compress(NonNullList<ItemStack> items, ItemStack result) {
        for (ItemStack stack : items) {
            stack.shrink(1);
        }

        result.grow(1);
    }
}
