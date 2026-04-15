/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.slot.FuelHandlerSlot;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.CoalGeneratorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class CoalGeneratorMenu extends AbstractContainerMenu {
    private static final int INV_SLOT_START = 1;
    private static final int INV_SLOT_END = 28;
    private static final int USE_ROW_SLOT_START = 28;
    private static final int USE_ROW_SLOT_END = 37;
    private final DataSlot heatData;

    public CoalGeneratorMenu(int containerId, Inventory playerInventory, CoalGeneratorBlockEntity coalGenerator, DataSlot heatData) {
        super(GalacticraftMenuType.COAL_GENERATOR.get(), containerId);
        this.heatData = heatData;

        addSlot(new FuelHandlerSlot(
                ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, coalGenerator, null),
                coalGenerator::set,
                0,
                33,
                34
        ));
        addStandardInventorySlots(playerInventory, 8, 84);
        addDataSlot(heatData);
    }

    public CoalGeneratorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.COAL_GENERATOR.get(), playerInventory.player.level(), data), DataSlot.standalone());
    }

    public static boolean isFuel(Holder<Item> itemHolder) {
        FurnaceFuel fuelValues = BuiltInRegistries.ITEM.getData(NeoForgeDataMaps.FURNACE_FUELS, itemHolder.unwrapKey().orElseThrow());

        return fuelValues != null && fuelValues.burnTime() > 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack swappedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack newStack = slot.getItem();
            swappedStack = newStack.copy();

            if (index != 0) {
                if (isFuel(newStack.typeHolder())) {
                    if (!this.moveItemStackTo(newStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
                    if (!this.moveItemStackTo(newStack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END && !this.moveItemStackTo(newStack, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(newStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (newStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (newStack.getCount() == swappedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, newStack);
        }

        return swappedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public float getHeatLevel() {
        return Math.max(0, Math.min(this.heatData.get(), 100));
    }
}
