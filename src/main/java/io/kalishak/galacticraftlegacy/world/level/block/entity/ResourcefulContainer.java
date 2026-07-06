/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;

public interface ResourcefulContainer extends Container {
    @NonNull NonNullList<ItemStack> getItems();
    void setItems(@NonNull NonNullList<ItemStack> items);

    default ResourceHandler<ItemResource> getResourceHandler() {
        return new ItemStacksResourceHandler(getItems());
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getItems()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    default ItemStack getItem(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeItem(int slot, int amount) {
        ItemStack inSlot = getItem(slot);

        if (!inSlot.isEmpty()) {
            try (Transaction transaction = Transaction.open(null)) {
                int extracted = getResourceHandler().extract(slot, ItemResource.of(inSlot), amount, transaction);

                if (extracted > 0) {
                    transaction.commit();

                    inSlot.shrink(amount);
                    setChanged();
                    return inSlot;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = getItem(slot).copy();
        setItem(slot, ItemStack.EMPTY);

        return removed;
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        getItems().set(slot, stack);

        ((ItemStacksResourceHandler) getResourceHandler()).set(slot, ItemResource.of(stack), stack.getCount());
    }

    @Override
    default void setChanged() {

    }

    @Override
    default boolean stillValid(Player player) {
        if (this instanceof BlockEntity blockEntity) {
            return Container.stillValidBlockEntity(blockEntity, player);
        }

        return true;
    }

    @Override
    default void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }
}
