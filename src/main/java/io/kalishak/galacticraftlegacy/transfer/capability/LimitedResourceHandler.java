package io.kalishak.galacticraftlegacy.transfer.capability;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public abstract class LimitedResourceHandler<S, R extends Resource> extends StacksResourceHandler<S, R> {
    protected final int maxExtractRate;
    protected final int maxInsertRate;

    protected LimitedResourceHandler(int size, S emptyStack, Codec<S> stackCodec, int maxExtractRate, int maxInsertRate) {
        super(size, emptyStack, stackCodec);
        this.maxExtractRate = maxExtractRate;
        this.maxInsertRate = maxInsertRate;
    }

    protected LimitedResourceHandler(NonNullList<S> stacks, S emptyStack, Codec<S> stackCodec, int maxExtractRate, int maxInsertRate) {
        super(stacks, emptyStack, stackCodec);
        this.maxExtractRate = maxExtractRate;
        this.maxInsertRate = maxInsertRate;
    }

    @Override
    public int extract(int index, R resource, int amount, TransactionContext transaction) {
        return super.extract(index, resource, Math.min(this.maxExtractRate, amount), transaction);
    }

    @Override
    public int insert(int index, R resource, int amount, TransactionContext transaction) {
        return super.insert(index, resource, Math.min(this.maxInsertRate, amount), transaction);
    }
}
