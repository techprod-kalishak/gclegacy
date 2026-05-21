/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.fluid;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Objects;

public abstract class SingleTankResourceHandler extends SnapshotJournal<FluidStack> implements ResourceHandler<FluidResource>, ValueIOSerializable {
    public static final String VALUE_IO_KEY = "FluidStack";

    public abstract FluidStack getFluidStack();
    public abstract void setFluidStack(FluidStack stack);

    protected boolean isValid(FluidResource resource) {
        return true;
    }

    protected void notifyChange() {

    }

    protected abstract int getCapacity(FluidResource resource);

    @Override
    public final int size() {
        return 1;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        FluidStack currentStack = getFluidStack();

        if ((currentStack.isEmpty() || resource.matches(currentStack)) && isValid(resource)) {
            int inserted = Math.min(amount, getCapacity(resource) - currentStack.getAmount());

            if (inserted > 0) {
                updateSnapshots(transaction);
                currentStack = getFluidStack();

                if (currentStack.isEmpty()) {
                    currentStack = resource.toStack(inserted);
                } else {
                    currentStack.grow(inserted);
                }

                setFluidStack(currentStack);
                notifyChange();
                return inserted;
            }
        }

        return 0;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        FluidStack currentStack = getFluidStack();

        if (resource.matches(currentStack)) {
            int extracted = Math.min(currentStack.getAmount(), amount);

            if (extracted > 0) {
                updateSnapshots(transaction);
                currentStack = getFluidStack();
                currentStack.shrink(extracted);
                setFluidStack(currentStack);
                notifyChange();
                return extracted;
            }
        }

        return 0;
    }

    @Override
    public final FluidResource getResource(int index) {
        Objects.checkIndex(index, size());
        return FluidResource.of(getFluidStack());
    }

    @Override
    public final long getAmountAsLong(int index) {
        Objects.checkIndex(index, size());
        return getFluidStack().getAmount();
    }

    public int getAmount() {
        return (int) getAmountAsLong(0);
    }

    @Override
    public final long getCapacityAsLong(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        return resource.isEmpty() || isValid(resource) ? getCapacity(resource) : 0;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmpty(resource);

        return isValid(resource);
    }

    @Override
    protected FluidStack createSnapshot() {
        FluidStack og = getFluidStack();
        setFluidStack(og.copy());

        return og;
    }

    @Override
    protected void revertToSnapshot(FluidStack snapshot) {
        setFluidStack(snapshot);
    }

    @Override
    public void serialize(ValueOutput output) {
        if (!getFluidStack().isEmpty()) {
            output.store(VALUE_IO_KEY, FluidStack.CODEC, getFluidStack());
        }
    }

    @Override
    public void deserialize(ValueInput input) {
        setFluidStack(input.read(VALUE_IO_KEY, FluidStack.CODEC).orElse(FluidStack.EMPTY));
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" + getFluidStack() + "]";
    }
}
