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
import io.kalishak.galacticraftlegacy.world.item.component.ItemAccessEnergyUtils;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric.AbstractSolarPanelBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SolarPanelMenu extends AbstractContainerMenu {
    private final AbstractSolarPanelBlockEntity solarPanel;
    private final DataSlot solarStrengthSlot;

    public SolarPanelMenu(int containerId, Inventory playerInventory, AbstractSolarPanelBlockEntity solarPanel, DataSlot solarStrengthSlot) {
        super(GalacticraftMenuType.SOLAR_PANEL.get(), containerId);
        this.solarPanel = solarPanel;
        this.solarStrengthSlot = solarStrengthSlot;

        addSlot(new CapabilityHandlerSlot<>(this.solarPanel.getResourceHandler(), this.solarPanel::setItem, Capabilities.Energy.ITEM, 0, 152, 83));
        addStandardInventorySlots(playerInventory, 8, 119);

        addDataSlot(solarStrengthSlot);
    }

    public SolarPanelMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntityOfClass(AbstractSolarPanelBlockEntity.class, playerInventory.player.level(), data), DataSlot.standalone());
    }

    public AbstractSolarPanelBlockEntity getSolarPanel() {
        return solarPanel;
    }

    public int getEnergyCapacity() {
        return this.solarPanel.getProperties().getEnergyCapacity();
    }

    public int getSolarStrength() {
        return this.solarStrengthSlot.get();
    }

    @Override
    public @NonNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack returnedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        int size = this.slots.size();

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            returnedStack = stack.copy();
            boolean movedToMachineSlot = false;

            if (index == 0) {
                if (!moveItemStackTo(stack, size - 36, size, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (ItemAccessEnergyUtils.hasEnergyHandler(stack)) {
                    if (!moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    movedToMachineSlot = true;
                } else {
                    if (index < size - 9) {
                        if (!moveItemStackTo(stack, size - 9, size, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!moveItemStackTo(stack, size - 36, size - 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.getCount() == 0) {
                if (movedToMachineSlot && returnedStack.getCount() > 1) {
                    ItemStack remainder = returnedStack.copy();
                    remainder.shrink(1);
                    slot.safeInsert(remainder);
                } else {
                    slot.safeInsert(ItemStack.EMPTY);
                }
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == returnedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return returnedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this.solarPanel, player);
    }
}
