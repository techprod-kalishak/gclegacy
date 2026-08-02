/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.ResourcefulContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public class TransientCraftingResourceHandler extends RangedResourceHandler<ItemResource> implements CraftingContainer, ResourcefulContainer {
    private final AbstractContainerMenu menu;
    private final IndexModifier<ItemResource> indexModifier;

    public TransientCraftingResourceHandler(AbstractContainerMenu menu, Supplier<ResourceHandler<ItemResource>> delegate, IndexModifier<ItemResource> indexModifier, int startIndex, int endIndex) {
        super(delegate, startIndex, endIndex);
        this.menu = menu;
        this.indexModifier = indexModifier;
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public @NonNull NonNullList<ItemStack> getItems() {
        return ResourcefulHelper.nonNullList(getDelegate(), ItemStack.EMPTY, ItemResource::toStack);
    }

    @Override
    public void setItems(@NonNull NonNullList<ItemStack> items) {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = items.get(i);

            if (!stack.isEmpty()) {
                this.indexModifier.set(i, ItemResource.of(stack), stack.getCount());
            }
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ResourcefulContainer.super.setItem(slot, stack);
        this.menu.slotsChanged(this);
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        int extracted = super.extract(index, resource, amount, transaction);

        if (extracted > 0 && !getResource(index).isEmpty()) {
            this.menu.slotsChanged(this);
        }

        return extracted;
    }

    @Override
    public int getContainerSize() {
        return size();
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedItemContents) {
        for (int i = 0; i < size(); i++) {
            stackedItemContents.accountStack(ItemUtil.getStack(getDelegate(), i));
        }
    }
}
