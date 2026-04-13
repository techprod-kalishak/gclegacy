package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.client.gui.ClientResourceHandlerTextUtils;
import io.kalishak.galacticraftlegacy.world.inventory.AbstractMachineMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractMachineBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public abstract class AbstractMachineScreen<BE extends AbstractMachineBlockEntity, M extends AbstractMachineMenu<BE>> extends AbstractRecipeBookScreen<M> implements MachineScreen {
    protected final Identifier backgroundTexture;

    public AbstractMachineScreen(M menu, RecipeBookComponent<?> recipeBookComponent, Inventory playerInventory, Component title, Identifier backgroundTexture) {
        super(menu, recipeBookComponent, playerInventory, title);
        this.backgroundTexture = backgroundTexture;
    }

    @Override
    public int getEnergyStored() {
        return getMenu().getMachine().getExistingData(GalacticraftAttachments.SYNC_ENERGY_STORAGE)
                .map(SyncedEnergyHandler::storedEnergy)
                .orElse(0);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.backgroundTexture, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        renderSprites(guiGraphics, i, j, this.menu.getEnergyCapacity());
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        if (isHovering(getEnergyBarBounds(), x, y)) {
            guiGraphics.setTooltipForNextFrame(
                    this.font,
                    ClientResourceHandlerTextUtils.energyComponentWithCapacity(getEnergyStored(), this.menu.getEnergyCapacity(), style -> style.withColor(ChatFormatting.GRAY)),
                    x,
                    y
            );
        }
    }

    protected boolean isHovering(Rectangle area, double x, double y) {
        return isHovering(area.x, area.y, area.width, area.height, x, y);
    }
}
