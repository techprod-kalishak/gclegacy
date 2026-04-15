/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;

import java.util.function.Supplier;

public class SimpleResourceInput extends RangedResourceHandler<ItemResource> implements ResourceHandlerInput {
    public SimpleResourceInput(ResourceHandler<ItemResource> delegate, int start, int end) {
        super(delegate, start, end);
    }

    public SimpleResourceInput(Supplier<ResourceHandler<ItemResource>> delegate, int start, int end) {
        super(delegate, start, end);
    }

    @Override
    public ItemStack getItem(int index) {
        return ItemUtil.getStack(this, index);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.size(); i++) {
            if (!getItem(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
