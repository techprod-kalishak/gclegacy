/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public interface AlloyCompressor extends MenuProvider, StackedContentsCompatible, RecipeCraftingHolder {
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

    @Override
    default void setRecipeUsed(@Nullable RecipeHolder<?> recipeHolder) {
    }

    @Override
    default @Nullable RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    static boolean canCompress(ResourceHandler<ItemResource> items, int maxStackSize, ItemStack recipeResult) {
        ItemStack resultItemStack = ItemUtil.getStack(items, 10);

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

    static void compress(ResourceHandler<ItemResource> items, ResourceHandler<ItemResource> ingredients, ItemStack result) {
        try (Transaction transaction = Transaction.open(null)) {
            boolean produce = true;

            for (int i = 0; i < ingredients.size(); i++) {
                ItemResource resource = ingredients.getResource(i);

                if (!resource.isEmpty() && items.extract(resource, 1, transaction) == 0) {
                    produce = false;
                    break;
                }
            }

            if (produce && items.insert(ItemResource.of(result), 1, transaction) > 0) {
                transaction.commit();
            }
        }
    }
}
