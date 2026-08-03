/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricArcFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ArcHeatingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricArcFurnaceBlockEntity extends AbstractElectricFurnaceBlockEntity<ArcHeatingRecipe> {
    public ElectricArcFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(GalacticraftBlockEntityType.ELECTRIC_ARC_FURNACE.get(), pos, state, GalacticraftRecipeType.ARC_HEATING.get());
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOUBLE_DOWN;
        }

        return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new ElectricArcFurnaceMenu(containerId, playerInventory, this, this.dataAccess);
    }
}
