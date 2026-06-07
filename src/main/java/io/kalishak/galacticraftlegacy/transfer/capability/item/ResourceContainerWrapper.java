/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.item;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public interface ResourceContainerWrapper extends Container {
    ResourceHandler<ItemResource> getHandler();
    void set(int index, ItemResource resource, int count);

    @Override
    default int getContainerSize() {
        return getHandler().size();
    }

    @Override
    default boolean isEmpty() {
        return ResourceHandlerUtil.isEmpty(getHandler());
    }

    @Override
    default ItemStack getItem(int slot) {
        return slot > getContainerSize() ? ItemStack.EMPTY : ItemUtil.getStack(getHandler(), slot);
    }

    @Override
    default ItemStack removeItem(int slot, int count) {
        try (Transaction transaction = Transaction.open(null)) {
            ItemResource resourceInSlot = getHandler().getResource(slot);

            if (!resourceInSlot.isEmpty()) {
                int extracted = getHandler().extract(resourceInSlot, count, transaction);

                if (extracted > 0) {
                    setChanged();
                    return resourceInSlot.toStack(extracted);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        ItemResource resourceInSlot = getHandler().getResource(slot);

        if (!resourceInSlot.isEmpty()) {
            int amount = getHandler().getAmountAsInt(slot);
            this.set(slot, ItemResource.EMPTY, 0);
            return resourceInSlot.toStack(amount);
        }

        return ItemStack.EMPTY;
    }

    @Override
    default void setItem(int slot, ItemStack itemStack) {
        set(slot, ItemResource.of(itemStack), itemStack.getCount());
        setChanged();
    }

    @Override
    default void setChanged() {

    }

    @Override
    default boolean stillValid(Player player) {
        return true;
    }

    @Override
    default void clearContent() {
        ResourcefulHelper.clear(getHandler(), this::set, ItemResource.EMPTY);
    }
}
