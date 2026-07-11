/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.slot.CapabilityHandlerSlot;
import io.kalishak.galacticraftlegacy.world.inventory.slot.NotPlaceableResourceHandlerSlot;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.ParachestBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class ParachestMenu extends AbstractContainerMenu {
    private final ResourceHandler<ItemResource> resourceHandler;
    private final ResourceHandler<FluidResource> fluidResourceHandler;
    private final ParachestBlockEntity parachest;
    private final Player player;
    private final int rows;

    public ParachestMenu(int containerId, Inventory playerInventory, ParachestBlockEntity parachest) {
        super(GalacticraftMenuType.PARACHEST.get(), containerId);
        this.resourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, parachest, null);
        this.fluidResourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Fluid.BLOCK, FluidResource.EMPTY, parachest, null);
        this.parachest = parachest;
        this.player = playerInventory.player;
        this.rows = (parachest.getItemsSize() - 3) / 9;

        addSlot(new CapabilityHandlerSlot<>(this.resourceHandler, parachest::setItem, Capabilities.Fluid.ITEM, 0,  75, (this.rows == 0 ? 24 : 26) + this.rows * 18));
        addSlot(new NotPlaceableResourceHandlerSlot(this.resourceHandler, parachest::setItem, 1, 125, (this.rows == 0 ? 24 : 26) + this.rows * 18));
        addSlot(new NotPlaceableResourceHandlerSlot(this.resourceHandler, parachest::setItem, 2, 125 + 18, (this.rows == 0 ? 24 : 26) + this.rows * 18));

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new NotPlaceableResourceHandlerSlot(
                        this.resourceHandler,
                        parachest::setItem,
                        2 + j + i * 9,
                        8 + j * 18,
                        18 + j * 18
                ));
            }
        }

        addStandardInventorySlots(playerInventory, 18, (this.rows == 0 ? 116 : 118));
    }

    public ParachestMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.PARACHEST.get(), playerInventory.player.level(), data));
    }

    public int getRowCount() {
        return this.rows;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        final int slotsCount = this.slots.size();

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < this.resourceHandler.size()) {
                if (!this.moveItemStackTo(itemstack1, slotsCount - 36, slotsCount, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.resourceHandler.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this.parachest, player);
    }
}
