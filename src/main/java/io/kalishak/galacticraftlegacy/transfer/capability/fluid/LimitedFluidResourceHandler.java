/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;

public class LimitedFluidResourceHandler extends SingleTankResourceHandler {
    private final int maxExtractRate;
    private final int maxInsertRate;
    private final int capacity;
    protected @NonNull FluidStack fluidStack = FluidStack.EMPTY;

    public LimitedFluidResourceHandler(int maxExtractRate, int maxInsertRate, int capacity) {
        this.maxExtractRate = maxExtractRate;
        this.maxInsertRate = maxInsertRate;
        this.capacity = capacity;
    }

    public LimitedFluidResourceHandler(int maxTransfer, int capacity) {
        this(maxTransfer, maxTransfer, capacity);
    }

    @Override
    public @NonNull FluidStack getFluidStack() {
        return this.fluidStack;
    }

    @Override
    public void setFluidStack(@NonNull FluidStack stack) {
        this.fluidStack = stack;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return super.insert(index, resource, Math.min(amount, this.maxInsertRate), transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return super.extract(index, resource, Math.min(amount, this.maxExtractRate), transaction);
    }
}
