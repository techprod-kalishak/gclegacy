/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.inventory.machine.CoalGeneratorMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.CoalGeneratorBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class CoalGeneratorScreen extends AbstractContainerScreen<CoalGeneratorMenu> {
    public static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/gui/container/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        extractTooltip(graphics, mouseX, mouseY);
        extractGeneration(graphics);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    private void extractGeneration(GuiGraphicsExtractor guiGraphics) {
        int i = this.leftPos;
        int j = this.topPos;

        boolean warmingUp = this.menu.getHeatLevel() <= 0 || this.menu.getHeatLevel() < CoalGeneratorBlockEntity.MIN_ENERGY_PER_HEAT;
        Component generate = warmingUp ? GalacticraftComponents.COAL_GENERATOR_NOT_GENERATING.asComponent() : GalacticraftComponents.COAL_GENERATOR_GENERATING.asComponent();
        Component status;

        guiGraphics.text(
                this.font,
                generate,
                i + 122 - this.font.width(generate) / 2,
                j + 33,
                -12566464,
                false
        );

        if (warmingUp) {
            status = GalacticraftComponents.COAL_GENERATOR_HEAT_LEVEL.asComponent().append(Mth.floor(this.menu.getHeatLevel() / CoalGeneratorBlockEntity.MIN_ENERGY_PER_HEAT * 100) + "%");

            guiGraphics.text(
                    this.font,
                    status,
                    i + 122 - this.font.width(status) / 2,
                    j + 45,
                    -12566464,
                    false
            );
        } else if (this.menu.getHeatLevel() < CoalGeneratorBlockEntity.MAX_ENERGY_PER_HEAT) {
            status = GalacticraftComponents.energyComponent((int) Math.floor(this.menu.getHeatLevel() - CoalGeneratorBlockEntity.MIN_ENERGY_PER_HEAT), true);
            guiGraphics.text(
                    this.font,
                    status,
                    i + 122 - this.font.width(status) / 2,
                    j + 45,
                    -12566464,
                    false
            );
        }
    }
}
