package io.kalishak.galacticraftlegacy.world.inventory;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

public interface WorldlyTank extends Tank {
    int[] getSlotsForFace(Direction direction);

    boolean canFillFluidThroughFace(int tank, FluidStack fluidStack, @Nullable Direction direction);

    boolean canDrainFluidThroughFace(int tank, FluidStack fluidStack, Direction direction);
}
