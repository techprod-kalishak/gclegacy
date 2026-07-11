/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.List;

public interface CraftingStorage extends StackedContentsCompatible {
    int getWidth();

    int getHeight();

    ResourceHandler<ItemResource> getResourceHandler();

    void setItem(int slot, ItemStack stack);

    ItemStack getItem(int slot);

    ItemStack removeItem(int slot, int count);

    default List<ItemStack> getItems() {
        return ResourcefulHelper.orderedHandlerCopy(getResourceHandler(), ItemStack.EMPTY, ItemResource::toStack);
    }

    default CraftingInput asCraftInput() {
        return this.asPositionedCraftInput().input();
    }

    default CraftingInput.Positioned asPositionedCraftInput() {
        return CraftingInput.ofPositioned(getWidth(), getHeight(), getItems());
    }
}
