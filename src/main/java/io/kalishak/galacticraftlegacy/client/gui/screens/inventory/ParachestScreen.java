package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.inventory.ParachestMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class ParachestScreen extends AbstractContainerScreen<ParachestMenu> implements FluidTankScreen {
    private static final Identifier CONTAINER_BACKGROUND = Constants.texture("textures/gui/container/parachest_54.png");
    private final int containerRows;
    private FluidStack tank;

    public ParachestScreen(ParachestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.containerRows = menu.getRowCount();
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void updateTankContents(FluidStack content, int tankIndex) {
        this.tank = content.copy();
    }

    @Override
    public FluidStack getTankContents(int tankIndex) {
        return this.tank;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, i, j, 0.0F, 0.0F, this.imageWidth, this.containerRows * 18 + 17, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, i, j + this.containerRows * 18 + 17, 0.0F, 126.0F, this.imageWidth, 96, 256, 256);
    }
}
