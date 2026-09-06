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
import org.jspecify.annotations.NonNull;

public class SingleTankResourceHandler extends SnapshotJournal<FluidStack> implements ResourceHandler<FluidResource>, ValueIOSerializable {
    public static final String VALUE_IO_KEY = "FluidStack";
    private final int capacity;
    private @NonNull FluidStack stack;

    public SingleTankResourceHandler(@NonNull FluidStack stack, int capacity) {
        this.stack = stack;
        this.capacity = capacity;
    }

    public SingleTankResourceHandler(int capacity) {
        this(FluidStack.EMPTY, capacity);
    }

    public @NonNull FluidStack getFluidStack() {
        return this.stack.copy();
    }

    public void setFluidStack(@NonNull FluidStack stack) {
        this.stack = stack.copy();
    }

    protected boolean isValid(FluidResource resource) {
        return true;
    }

    protected void notifyChange() {
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public final int size() {
        return 1;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        FluidStack currentStack = this.stack;

        if ((currentStack.isEmpty() || resource.matches(currentStack)) && isValid(resource)) {
            int inserted = Math.min(amount, getCapacity() - currentStack.getAmount());

            if (inserted > 0) {
                updateSnapshots(transaction);

                if (currentStack.isEmpty()) {
                    currentStack = resource.toStack(inserted);
                } else {
                    currentStack.grow(inserted);
                }

                this.stack = currentStack;
                notifyChange();
                return inserted;
            }
        }

        return 0;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        FluidStack currentStack = this.stack;

        if (resource.matches(currentStack)) {
            int extracted = Math.min(currentStack.getAmount(), amount);

            if (extracted > 0) {
                updateSnapshots(transaction);
                currentStack = this.stack;
                currentStack.shrink(extracted);
                this.stack = currentStack;
                notifyChange();

                return extracted;
            }
        }

        return 0;
    }

    @Override
    public final FluidResource getResource(int index) {
        return FluidResource.of(this.stack);
    }

    public FluidResource getResource() {
        return getResource(0);
    }

    @Override
    public final long getAmountAsLong(int index) {
        return getFluidStack().getAmount();
    }

    public int getAmount() {
        return getAmountAsInt(0);
    }

    @Override
    public final long getCapacityAsLong(int index, FluidResource resource) {
        return resource.isEmpty() || isValid(resource) ? getCapacity() : 0;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return isValid(resource);
    }

    @Override
    protected FluidStack createSnapshot() {
        return this.stack.copy();
    }

    @Override
    protected void revertToSnapshot(FluidStack snapshot) {
        this.stack = snapshot.copy();
    }

    @Override
    public void serialize(ValueOutput output) {
        if (!this.stack.isEmpty()) {
            output.store(VALUE_IO_KEY, FluidStack.CODEC, this.stack);
        }
    }

    @Override
    public void deserialize(ValueInput input) {
        input.read(VALUE_IO_KEY, FluidStack.CODEC).ifPresent(this::setFluidStack);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" + getFluidStack() + "]";
    }
}
