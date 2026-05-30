/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public abstract class LimitedStacksResourceHandler<S, R extends Resource> extends StacksResourceHandler<S, R> {
    protected final int maxTransfer;

    protected LimitedStacksResourceHandler(NonNullList<S> stacks, S emptyStack, Codec<S> stackCodec, int maxTransfer) {
        super(stacks, emptyStack, stackCodec);
        this.maxTransfer = maxTransfer;
    }

    @Override
    public final int insert(int index, R resource, int amount, TransactionContext transaction) {
        return super.insert(index, resource, Math.min(this.maxTransfer, amount), transaction);
    }

    @Override
    public final int extract(int index, R resource, int amount, TransactionContext transaction) {
        return super.extract(index, resource, Math.min(this.maxTransfer, amount), transaction);
    }
}
