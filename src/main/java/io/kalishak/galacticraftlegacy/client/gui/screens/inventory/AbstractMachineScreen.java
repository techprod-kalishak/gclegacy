/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.world.inventory.machine.AbstractMachineRecipeBookMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric.AbstractMachineBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;

import java.util.Optional;

public abstract class AbstractMachineScreen<BE extends AbstractMachineBlockEntity, M extends AbstractMachineRecipeBookMenu<BE>> extends AbstractRecipeBookScreen<M> implements MachineScreen {
    protected final Identifier backgroundTexture;

    public AbstractMachineScreen(M menu, RecipeBookComponent<?> recipeBookComponent, Inventory playerInventory, Component title, Identifier backgroundTexture) {
        super(menu, recipeBookComponent, playerInventory, title);
        this.backgroundTexture = backgroundTexture;
    }

    public static RecipeBookComponent.TabInfo asTab(ExtendedRecipeBookCategory category) {
        return new RecipeBookComponent.TabInfo(new ItemStack(Items.COMPASS), Optional.empty(), category);
    }

    @Override
    public int getEnergyStored() {
        return getMenu().getMachine().getExistingData(GalacticraftAttachments.SYNC_ENERGY_STORAGE)
                .map(SyncedEnergyHandler::storedEnergy)
                .orElse(0);
    }

    @Override
    public float getEnergyProgress() {
        int stored = getEnergyStored();

        return stored != 0 ? Math.clamp((float) stored / this.menu.getEnergyCapacity(), 0.0F, 1.0F) : 0.0F;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.backgroundTexture, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        extractSprites(guiGraphics, i, j, this.menu.getEnergyCapacity());
    }
}
