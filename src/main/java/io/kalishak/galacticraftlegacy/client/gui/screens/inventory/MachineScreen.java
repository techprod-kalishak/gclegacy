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

import java.awt.*;

public interface MachineScreen {
    Identifier ENERGY_BAR_SPRITE = Constants.id("container/power_bar");
    int BAR_WIDTH = 54;
    int BAR_HEIGHT = 7;
    Identifier ENERGY_INDICATOR_SPRITE = Constants.id("container/power_indicator");
    int INDICATOR_WIDTH = 11;
    int INDICATOR_HEIGHT = 10;

    int getEnergyStored();

    default Bounds getEnergyBarBounds() {
        return Bounds.ZERO;
    }

    default Bounds getEnergyIndicatorBounds() {
        return Bounds.ZERO;
    }

    /**
     * We will use Rectangle#width and Rectangle#height for x and y respectively
     */
    default void extractSprites(GuiGraphicsExtractor graphics, int leftOffset, int topOffset, int energyCapacity) {
        if (getEnergyStored() > 0) {
            Bounds energyIndicator = offset(getEnergyIndicatorBounds(), leftOffset, topOffset);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_INDICATOR_SPRITE, INDICATOR_WIDTH, INDICATOR_HEIGHT, 0, 0, energyIndicator.x(), energyIndicator.y(), INDICATOR_WIDTH, INDICATOR_HEIGHT);

            Bounds energyBar = offset(getEnergyBarBounds(), leftOffset, topOffset);
            int length = Mth.ceil((float) getEnergyStored() / (float) energyCapacity * energyBar.x());
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_BAR_SPRITE, BAR_WIDTH, BAR_HEIGHT, 0, 0, energyBar.x(), energyBar.y(), length, BAR_HEIGHT);
        }
    }

    static Bounds offset(Bounds bounds, int leftPos, int topPos) {
        return new Bounds(leftPos + bounds.x, topPos + bounds.y);
    }

    record Bounds(int x, int y) {
        public static final Bounds ZERO = new Bounds(0, 0);
    }
}
