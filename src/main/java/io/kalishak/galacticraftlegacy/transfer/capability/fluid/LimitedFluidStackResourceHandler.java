/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.fluid;

import io.kalishak.galacticraftlegacy.transfer.capability.LimitedStackResourceHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public abstract class LimitedFluidStackResourceHandler extends LimitedStackResourceHandler<FluidStack, FluidResource> {
    public LimitedFluidStackResourceHandler(int maxTransfer) {
        super(maxTransfer, FluidStack.CODEC);
    }

    @Override
    protected String getIOTagName() {
        return "FluidStack";
    }

    @Override
    protected boolean isEmpty(FluidStack stack) {
        return stack.isEmpty();
    }

    @Override
    protected boolean matches(FluidResource resource, FluidStack stack) {
        return resource.matches(stack);
    }

    @Override
    protected int getAmount(FluidStack stack) {
        return stack.getAmount();
    }

    @Override
    protected FluidStack toStack(FluidResource resource, int amount) {
        return resource.toStack(amount);
    }

    @Override
    protected FluidResource toResource(FluidStack stack) {
        return FluidResource.of(stack);
    }

    @Override
    protected FluidStack grow(FluidStack stack, int amount) {
        stack.grow(amount);
        return stack;
    }

    @Override
    protected FluidStack copy(FluidStack stack) {
        return stack.copy();
    }
}
