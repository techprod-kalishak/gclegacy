/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.transfer.capability.item.ResourceContainerWrapper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.List;

public interface CraftingContainerWrapper extends ResourceContainerWrapper, CraftingContainer {
    @Override
    default List<ItemStack> getItems() {
        return ResourcefulHelper.orderedHandlerCopy(getHandler(), ItemStack.EMPTY, ItemResource::toStack);
    }
}
