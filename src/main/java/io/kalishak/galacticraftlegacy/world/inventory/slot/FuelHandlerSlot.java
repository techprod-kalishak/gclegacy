/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.slot;

import io.kalishak.galacticraftlegacy.world.inventory.machine.CoalGeneratorMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class FuelHandlerSlot extends MutableHandlerSlot {
    public FuelHandlerSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, stack -> CoalGeneratorMenu.isFuel(stack.typeHolder()), index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(stack);
    }
}
