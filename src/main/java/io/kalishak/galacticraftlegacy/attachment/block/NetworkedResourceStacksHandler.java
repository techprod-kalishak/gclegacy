/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.block;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.Resource;

@SuppressWarnings("unused")
public abstract class NetworkedResourceStacksHandler<S, R extends Resource> extends StacksResourceHandler<S, R> {
    NetworkedResourceStacksHandler(NonNullList<S> stacks, S emptyStack, Codec<S> stackCodec) {
        super(stacks, emptyStack, stackCodec);
    }

    public static Item createItemStacks(NonNullList<ItemStack> stacks) {
        return new Item(stacks);
    }

    public static Fluid createFluidStacks(NonNullList<FluidStack> stacks) {
        return new Fluid(stacks);
    }

    public static class Item extends NetworkedResourceStacksHandler<ItemStack, ItemResource> {
        Item(NonNullList<ItemStack> stacks) {
            super(stacks, ItemStack.EMPTY, ItemStack.CODEC);
        }

        @Override
        protected ItemResource getResourceFrom(ItemStack itemStack) {
            return ItemResource.of(itemStack);
        }

        @Override
        protected int getAmountFrom(ItemStack itemStack) {
            return itemStack.getCount();
        }

        @Override
        protected ItemStack getStackFrom(ItemResource itemResource, int i) {
            return itemResource.toStack(i);
        }

        @Override
        protected ItemStack copyOf(ItemStack itemStack) {
            return itemStack.copy();
        }

        @Override
        protected int getCapacity(int i, ItemResource itemResource) {
            return itemResource.getOrDefault(DataComponents.MAX_STACK_SIZE, i);
        }
    }

    public static class Fluid extends NetworkedResourceStacksHandler<FluidStack, FluidResource> {
        Fluid(NonNullList<FluidStack> stacks) {
            super(stacks, FluidStack.EMPTY, FluidStack.CODEC);
        }

        @Override
        protected FluidResource getResourceFrom(FluidStack fluidStack) {
            return FluidResource.of(fluidStack);
        }

        @Override
        protected int getAmountFrom(FluidStack fluidStack) {
            return fluidStack.getAmount();
        }

        @Override
        protected FluidStack getStackFrom(FluidResource fluidResource, int i) {
            return fluidResource.toStack(i);
        }

        @Override
        protected FluidStack copyOf(FluidStack fluidStack) {
            return fluidStack.copy();
        }

        @Override
        protected int getCapacity(int i, FluidResource fluidResource) {
            return i;
        }
    }
}
