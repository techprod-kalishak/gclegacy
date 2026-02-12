package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.world.inventory.AbstractMachineMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.AbstractMachineBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

import java.awt.*;

public abstract class AbstractFluidMachineScreen<BE extends AbstractMachineBlockEntity, M extends AbstractMachineMenu<BE>> extends AbstractContainerScreen<M> implements MachineScreen, FluidTankScreen {
    protected final Identifier backgroundTexture;
    protected int storedEnergy = 0;
    protected final NonNullList<FluidStack> fluidTanks = NonNullList.withSize(2, FluidStack.EMPTY);

    public AbstractFluidMachineScreen(M menu, Inventory playerInventory, Component title, Identifier backgroundTexture) {
        super(menu, playerInventory, title);
        this.backgroundTexture = backgroundTexture;
    }

    @Override
    public void updateEnergy(int newAmount) {
        this.storedEnergy = newAmount;
    }

    @Override
    public int getEnergyStored() {
        return this.storedEnergy;
    }

    @Override
    public void updateTankContents(FluidStack content, int tankIndex) {
        this.fluidTanks.set(tankIndex, content);
    }

    @Override
    public FluidStack getTankContents(int tankIndex) {
        return this.fluidTanks.get(tankIndex);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.backgroundTexture, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        renderSprites(guiGraphics, i, j, this.menu.getEnergyCapacity());
    }

    @Override
    public void renderSprites(GuiGraphics guiGraphics, int leftOffset, int topOffset, int energyCapacity) {
        MachineScreen.super.renderSprites(guiGraphics, leftOffset, topOffset, energyCapacity);


    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        Rectangle rectangle = getEnergyBarBounds();
        if (isHovering(rectangle.x, rectangle.y, rectangle.width, rectangle.width, x, y)) {
            guiGraphics.setTooltipForNextFrame(
                    this.font,
                    Component.translatable("item.galacticraftlegacy.battery.tooltip", getEnergyStored() + "/" + this.menu.getEnergyCapacity() + " gJ").withStyle(ChatFormatting.GRAY),
                    x,
                    y
            );
        }
    }
}
