/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.VoidingResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.function.Supplier;

public class CompressingRecipeInput extends DelegatingResourceHandler<ItemResource> implements ResourceHandlerInput {
    public static final CompressingRecipeInput EMPTY = new CompressingRecipeInput(0, 0, () -> new VoidingResourceHandler<>(ItemResource.EMPTY));
    private final int width;
    private final int height;
    private final StackedItemContents stackedItemContents = new StackedItemContents();
    private final int ingredientCount;

    public CompressingRecipeInput(int width, int height, ResourceHandler<ItemResource> delegate) {
        this(width, height, () -> delegate);
    }

    public CompressingRecipeInput(int width, int height, Supplier<ResourceHandler<ItemResource>> delegate) {
        super(delegate);
        this.width = width;
        this.height = height;
        int i = 0;

        for (; i < delegate.get().size(); i++) {
            ItemStack stack = ItemUtil.getStack(delegate.get(), i);

            if (!stack.isEmpty()) {
                this.stackedItemContents.accountStack(stack, 1);
            }
        }

        this.ingredientCount = i;
    }

    public static Positioned ofPositioned(int width, int height, ResourceHandler<ItemResource> items) {
        if (width != 0 && height != 0) {
            int i = width - 1;
            int j = 0;
            int k = height - 1;
            int l = 0;

            for (int i1 = 0; i1 < height; i1++) {
                boolean flag = true;

                for (int j1 = 0; j1 < width; j1++) {
                    ItemResource resource = items.getResource(j1 + i1 * width);

                    if (!resource.isEmpty()) {
                        i = Math.min(i, j1);
                        j = Math.max(j, j1);
                        flag = false;
                    }
                }

                if (!flag) {
                    k = Math.min(k, i1);
                    l = Math.max(l, i1);
                }
            }

            int i2 = j - i + 1;
            int j2 = l - k + 1;
            if (i2 <= 0 || j2 <= 0) {
                return Positioned.EMPTY;
            } else if (i2 == width && j2 == height) {
                return new Positioned(new CompressingRecipeInput(width, height, items), i, k);
            } else {
                ResourceHandler<ItemResource> backedHandler = new ItemStacksResourceHandler(i2 * j2);

                try (Transaction tx = Transaction.open(null)) {
                    for (int k2 = 0; k2 < j2; k2++) {
                        for (int k1 = 0; k1 < i2; k1++) {
                            int l1 = k1 + i + (k2 + k) * width;

                            ItemResource resource = items.getResource(l1);

                            if (resource.isEmpty() || backedHandler.insert(items.getResource(l1), items.getAmountAsInt(l1), tx) != 0) {
                                break;
                            }
                        }
                    }

                    tx.commit();
                }

                return new Positioned(new CompressingRecipeInput(i2, j2, () -> backedHandler), i, k);
            }
        } else {
            return Positioned.EMPTY;
        }
    }

    @Override
    public ItemStack getItem(int index) {
        return ItemUtil.getStack(this, index);
    }

    public ItemStack getItem(int row, int column) {
        return getItem(row + column * this.width);
    }

    @Override
    public boolean isEmpty() {
        return this.ingredientCount == 0;
    }

    public StackedItemContents stackedItemContents() {
        return this.stackedItemContents;
    }

    public int ingredientCount() {
        return this.ingredientCount;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            return other instanceof CompressingRecipeInput compressingRecipeInput
                    && this.width == compressingRecipeInput.width
                    && this.height == compressingRecipeInput.height
                    && this.ingredientCount == compressingRecipeInput.ingredientCount
                    && ResourcefulHelper.handlerMatches(this.delegate.get(), compressingRecipeInput.delegate.get(), ItemUtil::getStack, ItemStack::isSameItemSameComponents);
        }
    }

    @Override
    public int hashCode() {
        int i = ResourcefulHelper.hashResourceHandler(this.delegate.get(), ItemUtil::getStack, ItemStack::hashItemAndComponents);
        i = 31 * i + this.width;
        return 31 * i + this.height;
    }

    public record Positioned(CompressingRecipeInput input, int left, int top) {
        public static final Positioned EMPTY = new Positioned(CompressingRecipeInput.EMPTY, 0, 0);
    }
}
