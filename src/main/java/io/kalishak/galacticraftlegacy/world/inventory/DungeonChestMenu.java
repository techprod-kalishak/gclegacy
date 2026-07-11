/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.slot.NotPlaceableResourceHandlerSlot;
import io.kalishak.galacticraftlegacy.world.level.block.entity.DungeonChestBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DungeonChestMenu extends AbstractContainerMenu {
    private final DungeonChestBlockEntity blockEntity;

    public DungeonChestMenu(int containerId, Inventory playerInventory, DungeonChestBlockEntity blockEntity) {
        super(GalacticraftMenuType.DUNGEON_CHEST.get(), containerId);
        this.blockEntity = blockEntity;
        //blockEntity.startOpen(playerInventory.player);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new NotPlaceableResourceHandlerSlot(blockEntity.getResourceHandler(), blockEntity::setItem, j + i * 9, 8 + j * 18, 13 + i * 18));
            }
        }

        addStandardInventorySlots(playerInventory, 8, 85);
    }

    public DungeonChestMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.DUNGEON_CHEST.get(), playerInventory.player.level(), buf));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 27) {
                if (!this.moveItemStackTo(itemstack1, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.blockEntity.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        //this.blockEntity.stopOpen(player);
    }
}
