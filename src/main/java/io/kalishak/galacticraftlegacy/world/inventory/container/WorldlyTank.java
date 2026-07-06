/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

public interface WorldlyTank extends Tank {
    int[] getSlotsForFace(Direction direction);

    boolean canFillFluidThroughFace(int tank, FluidStack fluidStack, @Nullable Direction direction);

    boolean canDrainFluidThroughFace(int tank, FluidStack fluidStack, Direction direction);
}
