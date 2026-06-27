/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.fluid;

import io.kalishak.galacticraftlegacy.world.inventory.WorldlyTank;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

public class WorldlyTankWrapper implements ResourceHandler<FluidResource> {
    private final WorldlyTank tank;
    private final TankWrapper wrapper;
    private final @Nullable Direction side;

    public WorldlyTankWrapper(WorldlyTank tank, @Nullable Direction side) {
        this.tank = tank;
        this.wrapper = new TankWrapper(tank);
        this.side = side;
    }

    private int convert(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Cannot access tank with negative slot index: " + index);
        }

        if (this.side == null) return index;

        int[] slots = this.tank.getSlotsForFace(this.side);

        if (index >= slots.length) {
            throw new IndexOutOfBoundsException("Cannot access worldly tank on side " + this.side + " : out of bounds slot index " + index + " with size " + slots.length);
        }

        return slots[index];
    }

    @Override
    public int size() {
        return this.side == null ? this.tank.getTanks() : this.tank.getSlotsForFace(this.side).length;
    }

    @Override
    public FluidResource getResource(int index) {
        return this.wrapper.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.wrapper.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return this.wrapper.getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return this.wrapper.isValid(index, resource);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        int newSlot = convert(index);

        if (!this.tank.canFillFluidThroughFace(newSlot, resource.toStack(amount), this.side)) {
            return 0;
        }

        return this.wrapper.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        int newSlot = convert(index);

        if (this.side != null && !this.tank.canDrainFluidThroughFace(newSlot, resource.toStack(amount), this.side)) {
            return 0;
        }

        return this.wrapper.extract(index, resource, amount, transaction);
    }
}
