/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.inventory.slot.CapabilityHandlerSlot;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.OxygenCollectorBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class OxygenCollectorMenu extends AbstractOxygenMachineMenu<OxygenCollectorBlockEntity> {
    public OxygenCollectorMenu(int containerId, Inventory playerInventory, OxygenCollectorBlockEntity machine) {
        super(GalacticraftMenuType.OXYGEN_COLLECTOR.get(), containerId, playerInventory, machine);
        addStandardInventorySlots(playerInventory, 8, 99);
    }

    public OxygenCollectorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(), playerInventory.player.level(), data));
    }

    @Override
    public void addSlots(ResourceHandler<ItemResource> resourceHandler) {
        addSlot(new CapabilityHandlerSlot<>(resourceHandler, this.machine::set, Capabilities.Energy.ITEM, 0, 32, 22));
    }
}
