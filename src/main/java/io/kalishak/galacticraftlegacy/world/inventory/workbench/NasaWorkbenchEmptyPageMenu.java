/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.inventory.slot.ConditionalHandlerSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class NasaWorkbenchEmptyPageMenu extends AbstractNasaWorkbenchMenu {
    public NasaWorkbenchEmptyPageMenu(int containerId, Inventory inventory, Level level, BlockPos blockPos) {
        super(GalacticraftMenuType.NASA_WORKBENCH_EMPTY_PAGE.get(), containerId, inventory, 1, level, blockPos);

        addSlot(new ConditionalHandlerSlot(
                this.resourceHandler,
                itemStack -> itemStack.has(GalacticraftDataComponents.SCHEMATIC),
                0,
                80,
                28
        ));
        addStandardInventorySlots(inventory, 8, 95);
    }

    public NasaWorkbenchEmptyPageMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(containerId, inventory, inventory.player.level(), data.readBlockPos());
    }

    public void onSchematicUnlocked() {
        try (Transaction tx = Transaction.open(null)) {
            if (this.resourceHandler.extract(ItemResource.of(getSlot(0).getItem()), 1, tx) > 0) {
                tx.commit();
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }
}
