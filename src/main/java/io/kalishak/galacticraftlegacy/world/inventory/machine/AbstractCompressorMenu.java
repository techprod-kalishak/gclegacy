/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractCompressorBlockEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public abstract class AbstractCompressorMenu<Compressor extends AbstractCompressorBlockEntity> extends RecipeBookMenu {
    protected final Compressor compressor;
    protected final ContainerData dataAccess;

    public AbstractCompressorMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, Compressor compressor, ContainerData dataAccess) {
        super(menuType, containerId);
        this.compressor = compressor;
        this.dataAccess = dataAccess;

        addDataSlots(dataAccess);
    }

    protected void addCompressorGrid(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> setter, int left, int top) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                addSlot(new ResourceHandlerSlot(handler, setter, x + y, left + x * 18, top + y * 18));
            }
        }
    }

    public float getCompressingProgress() {
        int current = this.dataAccess.get(0);
        int total = this.dataAccess.get(1);
        return total != 0 && current != 0 ? Mth.clamp((float)current / total, 0.0F, 1.0F) : 0.0F;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this.compressor, player);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
        this.compressor.fillStackedContents(stackedContents);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return EnumExtensions.RECIPE_BOOK_TYPE_COMPRESSING.getValue();
    }
}
