/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Objects;

public abstract class LimitedStackResourceHandler<S, R extends Resource> extends SnapshotJournal<S> implements ResourceHandler<R>, ValueIOSerializable {
    protected final int maxTransfer;
    protected final Codec<S> codec;

    protected LimitedStackResourceHandler(int maxTransfer, Codec<S> codec) {
        this.maxTransfer = maxTransfer;
        this.codec = codec;
    }

    protected abstract String getIOTagName();

    protected abstract S getStack();
    protected abstract void setStack(S stack);

    protected abstract int getCapacity(R resource);

    protected void notifyChange() {

    }

    @Override
    public final int size() {
        return 1;
    }

    protected abstract boolean isEmpty(S stack);
    protected abstract boolean matches(R resource, S stack);
    protected abstract int getAmount(S stack);
    protected abstract S toStack(R resource, int amount);
    protected abstract R toResource(S stack);
    protected abstract S grow(S stack, int amount);
    protected abstract S copy(S stack);

    @Override
    public int insert(int index, R resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        S currentStack = getStack();

        if ((isEmpty(currentStack) || matches(resource, currentStack)) && isValid(0, resource)) {
            int inserted = Math.min(this.maxTransfer, Math.min(amount, getCapacity(resource) - getAmount(currentStack)));

            if (inserted > 0) {
                updateSnapshots(transaction);
                currentStack = getStack();

                if (isEmpty(currentStack)) {
                    currentStack = toStack(resource, inserted);
                } else {
                    grow(currentStack, inserted);
                }

                setStack(currentStack);
                notifyChange();
                return inserted;
            }
        }

        return 0;
    }

    @Override
    public int extract(int index, R resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        S currentStack = getStack();

        if (matches(resource, currentStack)) {
            int extracted = Math.min(getAmount(currentStack), amount);

            if (extracted > 0) {
                updateSnapshots(transaction);
                currentStack = getStack();
                grow(currentStack, -extracted);
                setStack(currentStack);
                notifyChange();
                return extracted;
            }
        }

        return 0;
    }

    @Override
    public R getResource(int index) {
        Objects.checkIndex(index, size());
        return toResource(getStack());
    }

    @Override
    public long getAmountAsLong(int index) {
        Objects.checkIndex(index, size());
        return getAmount(getStack());
    }

    @Override
    public long getCapacityAsLong(int index, R resource) {
        Objects.checkIndex(index, size());
        return resource.isEmpty() || isValid(0, resource) ? getCapacity(resource) : 0;
    }

    @Override
    public boolean isValid(int index, R resource) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmpty(resource);

        return true;
    }

    @Override
    protected S createSnapshot() {
        S og = getStack();
        setStack(copy(og));

        return og;
    }

    @Override
    protected void revertToSnapshot(S snapshot) {
        setStack(snapshot);
    }

    @Override
    public void serialize(ValueOutput output) {
        if (!isEmpty(getStack())) {
            output.store(getIOTagName(), this.codec, getStack());
        }
    }

    @Override
    public void deserialize(ValueInput input) {
        input.read(getIOTagName(), this.codec).ifPresent(this::setStack);
    }
}
