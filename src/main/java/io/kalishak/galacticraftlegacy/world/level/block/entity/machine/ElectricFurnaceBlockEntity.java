/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.HeatingRecipe;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricFurnaceBlockEntity extends AbstractElectricFurnaceBlockEntity<HeatingRecipe> {
    private static final MachineInstance.Properties PROPERTIES = MachineInstance.Properties.of()
            .inventorySize(3)
            .batterySlotIndex(AbstractElectricFurnaceBlockEntity.SLOT_BATTERY)
            .inputSlot(AbstractElectricFurnaceBlockEntity.SLOT_INPUT)
            .resultSlot(AbstractElectricFurnaceBlockEntity.SLOT_RESULT);
    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(), pos, blockState, GalacticraftRecipeType.HEATING.get());
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        }

        return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new ElectricFurnaceMenu(containerId, playerInventory, this, this.dataAccess);
    }

    @Override
    public Properties getProperties() {
        return PROPERTIES;
    }
}
