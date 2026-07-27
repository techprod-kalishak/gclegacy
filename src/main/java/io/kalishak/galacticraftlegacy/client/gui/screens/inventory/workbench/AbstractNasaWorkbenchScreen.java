/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.world.inventory.workbench.AbstractNasaWorkbenchMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractNasaWorkbenchScreen<T extends AbstractNasaWorkbenchMenu> extends AbstractContainerScreen<T> implements NasaWorkbenchScreenPage {
    protected final Identifier backgroundTextures;

    public AbstractNasaWorkbenchScreen(T menu, Inventory inventory, Component title, Identifier backgroundTextures) {
        super(menu, inventory, title, 176, 220);
        this.backgroundTextures = backgroundTextures;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.backgroundTextures, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void init() {
        super.init();
        clearWidgets();
        Button nextButton = addRenderableWidget(createNextButton(this.width, this.height));
        nextButton.active = activateNextButton();
        Button previousButton = addRenderableWidget(createPreviousButton(this.width, this.height));
        previousButton.active = activateBackButton();
    }

    protected abstract boolean activateBackButton();
    protected abstract boolean activateNextButton();
}
