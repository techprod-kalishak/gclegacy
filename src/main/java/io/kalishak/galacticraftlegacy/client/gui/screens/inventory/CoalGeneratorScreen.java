package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.inventory.CoalGeneratorMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.CoalGeneratorBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class CoalGeneratorScreen extends AbstractContainerScreen<CoalGeneratorMenu> {
    public static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/gui/container/coal_generator.png");
    public static final Identifier LIT_SPRITE = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/coal_generator/lit_progress");

    public CoalGeneratorScreen(CoalGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderGeneration(guiGraphics);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        if (this.menu.isLit()) {
            int l = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, LIT_SPRITE, 14, 14, 0, 14 - l, i + 56, j + 36 + 14 - l, 14, l);
        }
    }

    private void renderGeneration(GuiGraphics guiGraphics) {
        int i = this.leftPos;
        int j = this.topPos;

        boolean warmingUp = this.menu.getHeatLevel() < CoalGeneratorBlockEntity.MIN_ENERGY_PER_HEAT;
        Component generate = warmingUp ? Component.translatable("container.coal_generator.not_generating") : Component.translatable("container.coal_generator.generating");

        guiGraphics.drawString(
                this.font,
                generate,
                (i + 155 + 26 + this.imageWidth - this.font.width(generate)) / 2,
                j + 46,
                -12566464,
                false
        );

        if (warmingUp) {
            guiGraphics.drawString(
                    this.font,
                    Component.translatable("container.coal_generator.heat_level", String.format("%.1f", this.menu.getHeatLevel() / CoalGeneratorBlockEntity.MIN_ENERGY_PER_HEAT * 100) + "%"),
                    (i + 155 + 26 + this.imageWidth - this.font.width(generate)) / 2,
                    j + 58,
                    -12566464,
                    false
            );
        }
    }
}
