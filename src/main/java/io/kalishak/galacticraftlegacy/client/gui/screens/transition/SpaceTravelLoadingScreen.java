/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.transition;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;

public class SpaceTravelLoadingScreen extends LevelLoadingScreen {
    public static final Identifier SPACE_LOCATION = Constants.id("textures/environment/space.png");

    public SpaceTravelLoadingScreen(LevelLoadTracker loadTracker, Reason reason) {
        super(loadTracker, reason);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        for(Renderable renderable : this.renderables) {
            renderable.extractRenderState(graphics, mouseX, mouseY, a);
        }

        int xCenter = this.width / 2;
        int yCenter = this.height / 2;
        int textTop = yCenter - 50;

        graphics.centeredText(this.font, GalacticraftComponents.SPACE_TRAVEL_TEXT, xCenter, textTop, -1);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        AbstractTexture spaceTexture = textureManager.getTexture(SPACE_LOCATION);
        TextureSetup textureSetup = TextureSetup.singleTexture(spaceTexture.getTextureView(), spaceTexture.getSampler());
        graphics.fill(RenderPipelines.GUI_TEXTURED, textureSetup, 0, 0, this.width, this.height);
    }
}
