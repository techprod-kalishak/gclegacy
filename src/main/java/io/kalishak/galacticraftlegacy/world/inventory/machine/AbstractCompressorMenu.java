/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AlloyCompressor;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

import java.util.function.Function;

public abstract class AbstractCompressorMenu extends RecipeBookMenu {
    protected final AlloyCompressor compressor;
    protected final ContainerData dataAccess;
    private final ResourceHandler<ItemResource> handler;

    public AbstractCompressorMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, AlloyCompressor compressor, ContainerData dataAccess) {
        super(menuType, containerId);
        this.compressor = compressor;
        this.dataAccess = dataAccess;
        this.handler = VanillaContainerWrapper.of(this.compressor);

        addDataSlots(dataAccess);
    }

    protected void addCompressorGrid(int left, int top) {
        int index = 0;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                addSlot(new ResourceHandlerSlot(this.handler, this.compressor::set, index, left + x * 18, top + y * 18));
                index++;
            }
        }
    }

    protected void addSlot(Function<ResourceHandler<ItemResource>, ? extends Slot> handler) {
        addSlot(handler.apply(this.handler));
    }

    public float getCompressingProgress() {
        int current = this.dataAccess.get(0);
        int total = this.dataAccess.get(1);
        return total != 0 && current != 0 ? Mth.clamp((float)current / total, 0.0F, 1.0F) : 0.0F;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity((BlockEntity) this.compressor, player);
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
