/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.CompressorRecipeBookComponent;
import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricCompressorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class ElectricCompressorScreen extends AbstractRecipeBookScreen<ElectricCompressorMenu> implements MachineScreen {
    private static final Identifier PROGRESS_SPRITE = Constants.id("container/compressor/compressing");
    public static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/gui/container/electric_compressor.png");

    public ElectricCompressorScreen(ElectricCompressorMenu menu, Inventory inventory, Component title) {
        super(menu, new CompressorRecipeBookComponent(menu, true), inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 96, this.height / 2 - 21);
    }

    @Override
    public int getEnergyStored() {
        return this.menu.getMachine().getExistingData(GalacticraftAttachments.SYNC_ENERGY_STORAGE).map(SyncedEnergyHandler::storedEnergy).orElse(0);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = this.leftPos;
        int yo = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        extractSprites(graphics, this.leftPos, this.topPos, this.menu.getEnergyCapacity());

        float progress = this.menu.getCompressingProgress();
        if (progress > 0.0F) {
            int burnProgressWidth = Mth.ceil(this.menu.getCompressingProgress() * 52.0F);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, 52, 30, 0, 0, xo + 77, yo + 31, burnProgressWidth, 30);
        }
    }

    @Override
    public Rectangle getEnergyBarBounds() {
        return new Rectangle(18, 96, 56, 7);
    }

    @Override
    public Rectangle getEnergyIndicatorBounds() {
        return new Rectangle(4, 95, 11, 10);
    }
}
