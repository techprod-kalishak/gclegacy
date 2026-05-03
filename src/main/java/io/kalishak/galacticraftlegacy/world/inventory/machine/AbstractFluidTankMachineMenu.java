/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractFluidTankMachineBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.DelegatingEnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

public abstract class AbstractFluidTankMachineMenu<M extends AbstractFluidTankMachineBlockEntity> extends AbstractContainerMenu {
    protected final ContainerData containerData;
    protected final M machine;
    protected final ResourceHandler<ItemResource> resourceHandler;
    protected final EnergyHandler energyHandler;
    protected final ResourceHandler<FluidResource> fluidResourceHandler;
    protected final Player player;

    protected AbstractFluidTankMachineMenu(MenuType<? extends AbstractFluidTankMachineMenu<M>> menuType, int containerId, Inventory playerInventory, M machine, ContainerData containerData) {
        super(menuType, containerId);
        this.containerData = containerData;
        this.machine = machine;
        this.resourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, machine, null);
        this.energyHandler = new DelegatingEnergyHandler(() -> ResourcefulHelper.getEnergyHandler(machine, null));
        this.fluidResourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Fluid.BLOCK, FluidResource.EMPTY, machine, null);
        this.player = playerInventory.player;
        addDataSlots(containerData);
    }

    public int getEnergyCapacity() {
        return this.energyHandler.getCapacityAsInt();
    }
}
