/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractOxygenBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

public abstract class AbstractOxygenMachineMenu<M extends AbstractOxygenBlockEntity> extends AbstractContainerMenu {
    protected final M machine;
    protected final ResourceHandler<ItemResource> resourceHandler;
    protected final EnergyHandler energyHandler;
    protected final ResourceHandler<FluidResource> fluidResourceHandler;
    protected final Player player;

    protected AbstractOxygenMachineMenu(MenuType<? extends AbstractOxygenMachineMenu<M>> menuType, int containerId, Inventory playerInventory, M machine) {
        super(menuType, containerId);
        this.machine = machine;
        this.resourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, machine, null);
        this.energyHandler = ResourcefulHelper.getEnergyHandler(machine, null);
        this.fluidResourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Fluid.BLOCK, FluidResource.EMPTY, machine, null);
        this.player = playerInventory.player;

        addSlots(this.resourceHandler);
    }

    public int getEnergyCapacity() {
        return this.energyHandler.getCapacityAsInt();
    }

    public int getTankCapacity() {
        return this.fluidResourceHandler.getCapacityAsInt(0, FluidResource.of(GalacticraftFluids.OXYGEN));
    }

    public M getMachine() {
        return this.machine;
    }

    public void addSlots(ResourceHandler<ItemResource> resourceHandler) {

    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack swappedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack newStack = slot.getItem();
            swappedStack = newStack.copy();

            if (slotIndex != 0) {
                if (AbstractMachineRecipeBookMenu.isBattery(newStack)) {
                    if (!this.moveItemStackTo(newStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 1 && slotIndex < 28) {
                    if (!this.moveItemStackTo(newStack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 28 && slotIndex < 37 && !this.moveItemStackTo(newStack, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(newStack, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (newStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (newStack.getCount() == swappedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, newStack);
        }

        return swappedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this.machine, player);
    }
}
