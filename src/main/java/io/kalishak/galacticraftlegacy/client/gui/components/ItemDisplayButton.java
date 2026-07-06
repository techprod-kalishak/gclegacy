/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.components;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.GearInventoryScreen;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemDisplayButton extends Button {
    private static final Identifier TAB_SELECTED = Constants.id("container/inventory/tab_selected");
    private static final Identifier TAB_UNSELECTED = Constants.id("container/inventory/tab_unselected");

    private final Minecraft minecraft;
    private final ItemStack itemStack;
    private final boolean decorations;
    private final boolean tooltip;

    public ItemDisplayButton(Minecraft minecraft, int x, int y, int width, int height, Component message, ItemStack itemStack, boolean decorations, boolean tooltip, Button.OnPress onPress, Button.CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
        this.minecraft = minecraft;
        this.itemStack = itemStack;
        this.decorations = decorations;
        this.tooltip = tooltip;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        Identifier sprite = isFocused() ? TAB_SELECTED : TAB_UNSELECTED;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), 26, 24);
        graphics.item(itemStack, getX() + (getWidth() / 2) - 8, getY() + (getHeight() / 2) - 7, 0);

        if (this.decorations) {
            graphics.itemDecorations(this.minecraft.font, this.itemStack, getX(), getY(), null);
        }

        if (this.tooltip && isHoveredOrFocused()) {
            renderTooltip(graphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean isFocused() {
        return (this.minecraft.screen instanceof GearInventoryScreen && this.itemStack.is(GalacticraftItems.OXYGEN_MASK)) || (this.minecraft.screen instanceof InventoryScreen && this.itemStack.is(Items.CRAFTING_TABLE));
    }

    protected void renderTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        guiGraphics.setTooltipForNextFrame(this.minecraft.font, this.itemStack, mouseX, mouseY);
    }
}
