/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;

public interface ResourceHandlerInput extends ResourceHandler<ItemResource>, RecipeInput {
    @Override
    default ItemStack getItem(int index) {
        return ItemUtil.getStack(this, index);
    }

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < this.size(); i++) {
            if (!getItem(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
