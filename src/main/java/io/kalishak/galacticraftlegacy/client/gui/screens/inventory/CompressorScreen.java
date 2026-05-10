/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.CompressorRecipeBookComponent;
import io.kalishak.galacticraftlegacy.world.inventory.machine.CompressorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class CompressorScreen extends AbstractRecipeBookScreen<CompressorMenu> {
    private static final Identifier TEXTURES = Constants.id("textures/gui/container/compressor.png");
    private static final Identifier LIT_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/furnace/lit_progress");
    private static final Identifier PROGRESS_SPRITE = Constants.id("container/compressor/compressing");

    public CompressorScreen(CompressorMenu menu, Inventory inventory, Component title) {
        super(menu, new CompressorRecipeBookComponent(menu, false), inventory, title);
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
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = this.leftPos;
        int yo = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        if (this.menu.isLit()) {
            int burnSpriteWidth = 52;
            int burnProgressWidth = Mth.ceil(this.menu.getCompressingProgress() * (float) burnSpriteWidth);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, burnSpriteWidth, 30, 0, 0, xo + 77, yo + 30, burnProgressWidth, 30);

            int litSpriteHeight = 14;
            int litProgressHeight = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    LIT_PROGRESS_SPRITE,
                    14,
                    litProgressHeight,
                    0,
                    litSpriteHeight - litProgressHeight,
                    xo + 80,
                    yo + 28 + litSpriteHeight - litProgressHeight,
                    14,
                    litProgressHeight
            );
        }
    }
}
