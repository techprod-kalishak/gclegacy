package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.awt.*;

public interface MachineScreen {
    Identifier ENERGY_BAR_SPRITE = Constants.id("container/power_bar");
    Identifier ENERGY_INDICATOR = Constants.id("container/power_indicator");

    int getEnergyStored();

    default Rectangle getEnergyBarBounds() {
        return new Rectangle();
    }

    default Rectangle getEnergyIndicatorBounds() {
        return new Rectangle();
    }

    default void extractSprites(GuiGraphicsExtractor graphics, int leftOffset, int topOffset, int energyCapacity) {
        if (getEnergyStored() > 0) {
            Rectangle energyIndicator = offset(getEnergyIndicatorBounds(), leftOffset, topOffset);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_INDICATOR, energyIndicator.width, energyIndicator.height, 0, 0, energyIndicator.x, energyIndicator.y, energyIndicator.width, energyIndicator.height);

            Rectangle energyBar = offset(getEnergyBarBounds(), leftOffset, topOffset);
            int length = Mth.ceil((float) getEnergyStored() / (float) energyCapacity * energyBar.width);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_BAR_SPRITE, energyBar.width, energyBar.height, 0, 0, energyBar.x, energyBar.y, length, energyBar.height);
        }
    }

    static Rectangle offset(Rectangle rectangle, int leftPos, int topPos) {
        return new Rectangle(
                leftPos + rectangle.x,
                topPos + rectangle.y,
                rectangle.width,
                rectangle.height
        );
    }
}
