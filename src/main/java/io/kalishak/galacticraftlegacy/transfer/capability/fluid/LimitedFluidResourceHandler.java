/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.fluid;

import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class LimitedFluidResourceHandler extends SingleTankResourceHandler {
    protected final int maxExtractRate;
    protected final int maxInsertRate;

    public LimitedFluidResourceHandler(int capacity, int maxExtractRate, int maxInsertRate) {
        super(capacity);
        this.maxExtractRate = maxExtractRate;
        this.maxInsertRate = maxInsertRate;
    }

    public LimitedFluidResourceHandler(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
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
