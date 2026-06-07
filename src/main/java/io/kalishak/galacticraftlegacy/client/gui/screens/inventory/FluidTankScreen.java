/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidStack;

import java.awt.*;

public interface FluidTankScreen extends MachineScreen {
    Identifier OXYGEN_BAR_SPRITE = Constants.id("container/oxygen_bar");
    Identifier OXYGEN_INDICATOR_SPRITE = Constants.id("container/oxygen_indicator");
    int OXYGEN_INDICATOR_WIDTH = 10;

    default Bounds getTankBounds() {
        return Bounds.ZERO;
    }

    default Bounds getTankIndicatorBounds() {
        return Bounds.ZERO;
    }

    @Override
    default void extractSprites(GuiGraphicsExtractor graphics, int leftOffset, int topOffset, int energyCapacity) {
        MachineScreen.super.extractSprites(graphics,leftOffset,topOffset,energyCapacity);
        int amount = getFluidStack().getAmount();

        if (amount > 0) {
            Bounds tankIndicator = MachineScreen.offset(getTankIndicatorBounds(), leftOffset, topOffset);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, OXYGEN_INDICATOR_SPRITE, OXYGEN_INDICATOR_WIDTH, INDICATOR_HEIGHT, 0, 0, tankIndicator.x(), tankIndicator.y(), OXYGEN_INDICATOR_WIDTH, INDICATOR_HEIGHT);

            Bounds tankBounds = MachineScreen.offset(getTankBounds(), leftOffset, topOffset);
            int length = Mth.ceil((float) amount / (float) getCapacity() * tankBounds.x());

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, OXYGEN_BAR_SPRITE, BAR_WIDTH, BAR_HEIGHT, 0, 0, tankBounds.x(), tankBounds.y(), length, BAR_HEIGHT);
        }
    }

    FluidStack getFluidStack();
    int getCapacity();
}
