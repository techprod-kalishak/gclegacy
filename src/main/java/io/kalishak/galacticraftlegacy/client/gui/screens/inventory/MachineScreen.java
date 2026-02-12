package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.awt.*;

public interface MachineScreen {
    Identifier ENERGY_BAR_SPRITE = Constants.id("container/power_bar");
    Identifier ENERGY_INDICATOR = Constants.id("container/power_indicator");

    void updateEnergy(int newAmount);

    int getEnergyStored();

    default Rectangle getEnergyBarBounds() {
        return new Rectangle();
    }

    default Rectangle getEnergyIndicatorBounds() {
        return new Rectangle();
    }

    default void renderSprites(GuiGraphics guiGraphics, int leftOffset, int topOffset, int energyCapacity) {
        if (getEnergyStored() > 0) {
            Rectangle energyIndicator = offset(getEnergyIndicatorBounds(), leftOffset, topOffset);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_INDICATOR, energyIndicator.width, energyIndicator.height, 0, 0, energyIndicator.x, energyIndicator.y, energyIndicator.width, energyIndicator.height);

            Rectangle energyBar = offset(getEnergyBarBounds(), leftOffset, topOffset);
            int length = Mth.ceil((float) getEnergyStored() / (float) energyCapacity * energyBar.width);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_BAR_SPRITE, energyBar.width, energyBar.height, 0, 0, energyBar.x, energyBar.y, length, energyBar.height);
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
