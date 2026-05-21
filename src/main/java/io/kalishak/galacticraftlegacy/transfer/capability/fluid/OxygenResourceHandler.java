package io.kalishak.galacticraftlegacy.transfer.capability.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;

public class OxygenResourceHandler extends SingleTankResourceHandler {
    public static long lastOxygenRequestTick;
    private final int maxExtractRate;
    private final int maxInsertRate;
    protected @NonNull FluidStack fluidStack = FluidStack.EMPTY;

    public OxygenResourceHandler(int maxExtractRate, int maxInsertRate) {
        this.maxExtractRate = maxExtractRate;
        this.maxInsertRate = maxInsertRate;
    }

    @Override
    public FluidStack getFluidStack() {
        return this.fluidStack;
    }

    @Override
    public void setFluidStack(FluidStack stack) {
        this.fluidStack = stack;
    }

    @Override
    protected int getCapacity(FluidResource resource) {
        return 16000;
    }

    public int getCapacity() {
        return getCapacity(FluidResource.EMPTY);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return super.extract(index, resource, Math.min(this.maxExtractRate, amount), transaction);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        int inserted = super.insert(index, resource, Math.min(this.maxInsertRate, amount), transaction);

        if (inserted > 0) OxygenResourceHandler.lastOxygenRequestTick = 20;

        return inserted;
    }
}
