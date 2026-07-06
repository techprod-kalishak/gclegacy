/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container;

import net.minecraft.world.Clearable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public interface Tank extends Clearable {
    int getTanks();

    FluidStack getFluid(int index);

    FluidStack removeFluid(int index, int amount);

    FluidStack removeNoUpdate(int index);

    void setFluid(int index, FluidStack fluidStack);

    default int getMaxFluidAmount() {
        return 8 * FluidType.BUCKET_VOLUME;
    }

    void onTankChange(int index, FluidStack previousContents);

    default boolean canFillFluid(int index, FluidStack fluidStack) {
        return true;
    }

    default boolean canDrainFluid(Tank into, int index, FluidStack fluidStack) {
        return true;
    }
}
