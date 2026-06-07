/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.FabricatingRecipeBook;
import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.SearchRecipeBookCategory;
import io.kalishak.galacticraftlegacy.world.inventory.machine.CircuitFabricatorMenu;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.CircuitFabricatorBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class CircuitFabricatorScreen extends AbstractMachineScreen<CircuitFabricatorBlockEntity, CircuitFabricatorMenu> {
    private static final Identifier TEXTURES = Constants.id("textures/gui/container/circuit_fabricator.png");
    private static final Identifier PROCESS_PROGRESS = Constants.id("container/circuit_fabricator/process_progress");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(GalacticraftItems.CIRCUIT_FABRICATOR.toStack(), Optional.of(Items.REDSTONE.getDefaultInstance()), SearchRecipeBookCategory.FABRICATING)
    );

    public CircuitFabricatorScreen(CircuitFabricatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, new FabricatingRecipeBook(menu, TABS), playerInventory, title, TEXTURES);
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.inventoryLabelY = this.imageHeight / 2 - 24;
    }

    @Override
    public Bounds getEnergyBarBounds() {
        return new Bounds(80, 95);
    }

    @Override
    public Bounds getEnergyIndicatorBounds() {
        return new Bounds(136, 94);
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 4, this.height / 2 - 50);
    }

    @Override
    public void extractSprites(GuiGraphicsExtractor guiGraphics, int leftOffset, int topOffset, int energyCapacity) {
        super.extractSprites(guiGraphics, leftOffset, topOffset, energyCapacity);

        if (this.menu.getProgress() > 0.0F) {
            int progressBarLength = Mth.ceil(this.menu.getProgress() * 24.0F);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROCESS_PROGRESS, 51, 10, 0, 0, leftOffset + 88, topOffset + 20, progressBarLength, 10);
        }
    }
}
