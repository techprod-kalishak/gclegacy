/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric;

import io.kalishak.galacticraftlegacy.world.inventory.machine.SolarPanelMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.EnergyProducer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public abstract class AbstractSolarPanelBlockEntity extends AbstractMachineBlockEntity implements EnergyProducer {
    protected AbstractSolarPanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static <T extends AbstractSolarPanelBlockEntity> void registerCapabilities(BlockEntityType<T> type, RegisterCapabilitiesEvent event) {
        registerEnergyCapability(type, event);
        registerItemCapability(type, event);
    }

    @Override
    protected int getSize() {
        return 1;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SolarPanelMenu(containerId, inventory);
    }
}
