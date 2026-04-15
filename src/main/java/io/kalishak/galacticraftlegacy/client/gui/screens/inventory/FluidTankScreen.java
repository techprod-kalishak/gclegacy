/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import net.neoforged.neoforge.fluids.FluidStack;

public interface FluidTankScreen {
    void updateTankContents(FluidStack content, int tankIndex);

    FluidStack getTankContents(int tankIndex);
}
