/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.item;

import io.kalishak.galacticraftlegacy.world.inventory.container.memory.MemorableContainer;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class MemorableResourceHandler extends ItemStacksResourceHandler {
    private final MemorableContainer memorableContainer;

    public MemorableResourceHandler(MemorableContainer memorableContainer, int size) {
        super(size);
        this.memorableContainer = memorableContainer;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        int inserted = super.insert(index, resource, amount, transaction);

        if (inserted > 0) {
            this.memorableContainer.updateOverrideStatus();
        }

        return inserted;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        int extracted = super.extract(index, resource, amount, transaction);

        if (extracted > 0) {
            this.memorableContainer.updateOverrideStatus();
        }

        return extracted;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        if (index >= this.memorableContainer.getMemorySize()) {
            return false;
        }

        boolean overrideMemory = this.memorableContainer.isMemoryOverridden();
        ItemResource target = overrideMemory ? getResource(index) : this.memorableContainer.getMemories().get(index);

        if (!target.isEmpty() && target.matches(resource.toStack())) {
            return true;
        }

        return super.isValid(index, resource);
    }
}
