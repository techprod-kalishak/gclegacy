/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.ElectricFurnaceRecipeBookComponent;
import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ElectricFurnaceBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;

import java.awt.*;
import java.util.List;

public class ElectricFurnaceScreen extends AbstractMachineScreen<ElectricFurnaceBlockEntity, ElectricFurnaceMenu> {
    private static final Identifier BURN_PROGRESS_SPRITE = Constants.id("container/electric_furnace/burn_progress");
    private static final Identifier TEXTURES = Constants.id("textures/gui/container/electric_furnace.png");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.FURNACE),
            new RecipeBookComponent.TabInfo(Items.PORKCHOP, RecipeBookCategories.FURNACE_FOOD),
            new RecipeBookComponent.TabInfo(Items.STONE, RecipeBookCategories.FURNACE_BLOCKS),
            new RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.EMERALD, RecipeBookCategories.FURNACE_MISC)
    );

    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, new ElectricFurnaceRecipeBookComponent(menu, TABS), playerInventory, title, TEXTURES);
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 20, this.height / 2 - 56);
    }

    @Override
    public Rectangle getEnergyIndicatorBounds() {
        return new Rectangle(27, 53, 11, 10);
    }

    @Override
    public Rectangle getEnergyBarBounds() {
        return new Rectangle(40, 53, 54, 7);
    }

    @Override
    public void extractSprites(GuiGraphicsExtractor guiGraphics, int leftOffset, int topOffset, int energyCapacity) {
        super.extractSprites(guiGraphics, leftOffset, topOffset, energyCapacity);

        int processProgress = Mth.ceil(this.menu.getBurnProgress() * 24.0F);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BURN_PROGRESS_SPRITE, 24, 16, 0, 0, leftOffset + 78, topOffset + 24, processProgress, 16);
    }
}
