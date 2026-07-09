/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.world.inventory.machine.OxygenCollectorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class OxygenCollectorScreen extends AbstractContainerScreen<OxygenCollectorMenu> implements FluidTankScreen {
    public static final Identifier TEXTURES = Constants.id("textures/gui/container/oxygen_collector.png");

    public OxygenCollectorScreen(OxygenCollectorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 180);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        extractSprites(guiGraphics, i, j, this.menu.getEnergyCapacity());
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        super.extractTooltip(guiGraphics, mouseX, mouseY);

        if (isHovering(getEnergyBarBounds(), BAR_WIDTH, BAR_HEIGHT, mouseX, mouseY)) {
            Constants.energy(getEnergyStored(), this.menu.getEnergyCapacity(), ClientConfig.ENERGY_UNIT, component -> guiGraphics.setTooltipForNextFrame(
                    this.font,
                    component,
                    mouseX,
                    mouseY
            ));
        }
    }

    protected boolean isHovering(Bounds area, int width, int height, double x, double y) {
        return isHovering(area.x(), area.y(), width, height, x, y);
    }

    @Override
    public int getEnergyStored() {
        return getMenu().getMachine().getExistingData(GalacticraftAttachments.SYNC_ENERGY_STORAGE)
                .map(SyncedEnergyHandler::storedEnergy)
                .orElse(0);
    }

    @Override
    public float getEnergyProgress() {
        return 0;
    }

    @Override
    public FluidStack getFluidStack() {
        return getMenu().getMachine().getExistingData(GalacticraftAttachments.SYNC_FLUID_STACK)
                .orElse(FluidStack.EMPTY);
    }

    @Override
    public int getCapacity() {
        return getMenu().getTankCapacity();
    }

    @Override
    public Bounds getEnergyBarBounds() {
        return new Bounds(113, 33);
    }

    @Override
    public Bounds getEnergyIndicatorBounds() {
        return new Bounds(100, 33);
    }

    @Override
    public Bounds getTankBounds() {
        return new Bounds(113, 20);
    }

    @Override
    public Bounds getTankIndicatorBounds() {
        return new Bounds(101, 20);
    }
}
