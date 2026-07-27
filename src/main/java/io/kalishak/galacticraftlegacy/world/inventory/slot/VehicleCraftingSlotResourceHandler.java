/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.slot;

import io.kalishak.galacticraftlegacy.world.level.GalacticraftParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class VehicleCraftingSlotResourceHandler extends ResourceHandlerSlot {
    private final Level level;
    private final BlockPos blockPos;

    public VehicleCraftingSlotResourceHandler(Level level, BlockPos blockPos, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int handlerSlot, int xPosition, int yPosition) {
        super(handler, slotModifier, handlerSlot, xPosition, yPosition);
        this.level = level;
        this.blockPos = blockPos;
    }

    @Override
    public void setByPlayer(ItemStack itemStack) {
        super.setByPlayer(itemStack);

        this.level.addParticle(GalacticraftParticleTypes.SPARKS.get(), this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ(), 0.5D, 0.5D, 0.5D);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
