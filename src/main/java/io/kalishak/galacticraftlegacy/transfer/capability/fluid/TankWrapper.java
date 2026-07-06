/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.fluid;

import io.kalishak.galacticraftlegacy.world.inventory.container.Tank;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.RootCommitJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TankWrapper implements ResourceHandler<FluidResource> {
    private final Tank tank;
    private final List<TankSlot> tankWrappers = new ArrayList<>();
    private final RootCommitJournal setChangedJournal;

    public TankWrapper(Tank tank) {
        this.tank = tank;
        this.setChangedJournal = new RootCommitJournal(this::onRootCommit);
    }

    private void onRootCommit() {
        this.tank.onTankChange(0, FluidStack.EMPTY);
    }

    private TankSlot getTankWrapper(int index) {
        Objects.checkIndex(index, size());
        return this.tankWrappers.get(index);
    }

    @Override
    public int size() {
        return this.tank.getTanks();
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return getTankWrapper(index).insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return getTankWrapper(index).extract(index, resource, amount, transaction);
    }

    @Override
    public FluidResource getResource(int index) {
        return getTankWrapper(index).getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return getTankWrapper(index).getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return getTankWrapper(index).getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return getTankWrapper(index).isValid(index, resource);
    }

    private class TankSlot extends SingleTankResourceHandler {
        private final int index;

        private TankSlot(int index, int capacity) {
            super(capacity);
            this.index = index;
        }

        @Override
        public @NonNull FluidStack getFluidStack() {
            return TankWrapper.this.tank.getFluid(this.index);
        }

        @Override
        public void setFluidStack(@NonNull FluidStack stack) {
            TankWrapper.this.tank.setFluid(this.index, stack);
        }

        @Override
        protected boolean isValid(FluidResource resource) {
            return TankWrapper.this.tank.canFillFluid(this.index, resource.toStack(getCapacity()));
        }

        @Override
        public int getCapacity() {
            return TankWrapper.this.tank.getMaxFluidAmount();
        }

        @Override
        public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
            int inserted = super.insert(index, resource, amount, transaction);

            if (inserted > 0) {
                TankWrapper.this.tank.onTankChange(index, resource.toStack(amount));
            }

            return inserted;
        }

        @Override
        public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
            int extracted = super.extract(index, resource, amount, transaction);

            if (extracted > 0) {
                TankWrapper.this.tank.onTankChange(index, resource.toStack(amount));
            }

            return extracted;
        }

        @Override
        public void updateSnapshots(TransactionContext transaction) {
            super.updateSnapshots(transaction);
            TankWrapper.this.setChangedJournal.updateSnapshots(transaction);
        }

        @Override
        protected void onRootCommit(FluidStack originalState) {
            FluidStack currentFluid = getFluidStack();

            if (originalState != null) {
                if (!originalState.isEmpty() && originalState.is(currentFluid.getFluid())) {
                    ((PatchedDataComponentMap) originalState.getComponents()).restorePatch(currentFluid.getComponentsPatch());
                    originalState.setAmount(currentFluid.getAmount());
                    setFluidStack(originalState);
                } else {
                    originalState.setAmount(0);
                }
            }
        }
    }
}
