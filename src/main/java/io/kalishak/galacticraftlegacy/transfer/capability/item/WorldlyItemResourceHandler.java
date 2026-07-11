/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.item;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class WorldlyItemResourceHandler extends DelegatingResourceHandler<ItemResource> {
    private final WorldlyContainer container;
    private final @Nullable Direction side;

    public WorldlyItemResourceHandler(ResourceHandler<ItemResource> delegate, WorldlyContainer container, @Nullable Direction side) {
        super(delegate);
        this.container = container;
        this.side = side;
    }

    public WorldlyItemResourceHandler(Supplier<ResourceHandler<ItemResource>> delegate, WorldlyContainer container, @Nullable Direction side) {
        super(delegate);
        this.container = container;
        this.side = side;
    }

    int convertSlot(int slot) {
        if (slot < 0) {
            throw new IndexOutOfBoundsException("Cannot access container with negative slot index: " + slot);
        }
        if (this.side == null) {
            return slot;
        }

        int[] slots = this.container.getSlotsForFace(side);
        if (slot >= slots.length) {
            throw new IndexOutOfBoundsException("Cannot access worldly container on side " + side + " : out of bounds slot index " + slot + " with size " + slots.length);
        }

        return slots[slot];
    }

    @Override
    public int size() {
        return this.side == null ? this.container.getContainerSize() : this.container.getSlotsForFace(this.side).length;
    }

    @Override
    public ItemResource getResource(int index) {
        return super.getResource(convertSlot(index));
    }

    @Override
    public long getAmountAsLong(int index) {
        return super.getAmountAsLong(convertSlot(index));
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return super.getCapacityAsLong(convertSlot(index), resource);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return super.isValid(convertSlot(index), resource);
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (!this.container.canPlaceItemThroughFace(convertSlot(index), resource.toStack(), this.side)) {
            return 0;
        }

        return super.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (this.side != null && !this.container.canTakeItemThroughFace(convertSlot(index), resource.toStack(), this.side)) {
            return 0;
        }

        return super.extract(index, resource, amount, transaction);
    }
}
