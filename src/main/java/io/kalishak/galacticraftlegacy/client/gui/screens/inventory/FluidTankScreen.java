package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import net.neoforged.neoforge.fluids.FluidStack;

public interface FluidTankScreen {
    void updateTankContents(FluidStack content, int tankIndex);

    FluidStack getTankContents(int tankIndex);
}
