/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ElectricFurnaceBlockEntity extends AbstractElectricFurnaceBlockEntity<SmeltingRecipe> {
    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(), pos, blockState, RecipeType.SMELTING);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerEnergyCapability(GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(), event);
        registerItemCapability(GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(), event);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.galacticraftlegacy.electric_furnace");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new ElectricFurnaceMenu(containerId, playerInventory, this, this.dataAccess);
    }
}
