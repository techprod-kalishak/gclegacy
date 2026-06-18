/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractMachineBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

public abstract class AbstractMachineRecipeBookMenu<M extends AbstractMachineBlockEntity> extends RecipeBookMenu {
    protected final ContainerData containerData;
    protected final M machine;
    protected final ResourceHandler<ItemResource> resourceHandler;
    protected final EnergyHandler energyHandler;
    protected final Player player;

    protected AbstractMachineRecipeBookMenu(MenuType<? extends AbstractMachineRecipeBookMenu<M>> menuType, int containerId, Inventory playerInventory, M machine, ContainerData containerData) {
        super(menuType, containerId);
        this.containerData = containerData;
        this.machine = machine;
        this.resourceHandler = VanillaContainerWrapper.of(machine);
        this.energyHandler = ResourcefulHelper.getEnergyHandler(machine, null);
        this.player = playerInventory.player;
        addDataSlots(containerData);
    }

    public static boolean isBattery(ItemStack stack) {
        return stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack)) != null;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this.machine, player);
    }

    public int getEnergyCapacity() {
        return this.energyHandler.getCapacityAsInt();
    }

    public M getMachine() {
        return this.machine;
    }
}
