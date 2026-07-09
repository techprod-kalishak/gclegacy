package io.kalishak.galacticraftlegacy.client.gui.screens.recipebook;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public interface EnergyRecipeBookComponent {
    static boolean hasEnergy(Slot slot) {
        ItemStack stack = slot.getItem();
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        return energyHandler != null;
    }

    static boolean hasSufficientEnergy(Slot slot, int energyPerRecipeCompletion) {
        ItemStack stack = slot.getItem();
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        if (energyHandler != null) {
            try (Transaction tx = Transaction.open(null)) {
                return energyHandler.extract(energyPerRecipeCompletion, tx) > energyPerRecipeCompletion;
            }
        }

        return false;
    }

    static SlotDisplay getEnergyDisplay(Slot slot, int energyPerRecipeCompletion, SlotDisplay currentDisplay) {
        ItemStack stack = slot.getItem();
        SlotDisplay fallback = new SlotDisplay.ItemSlotDisplay(GalacticraftItems.INFINITE_BATTERY);

        if (stack.isEmpty()) {
            return fallback;
        }

        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        if (energyHandler != null) {
            int currentlyStored = energyHandler.getAmountAsInt();

            if (currentlyStored == 0) {
                return fallback;
            } else if (currentlyStored >= energyPerRecipeCompletion) {
                return currentDisplay;
            }

            ItemStack newStack = stack.copy();
            newStack.set(GalacticraftDataComponents.STORED_ENERGY, energyPerRecipeCompletion);

            return new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(newStack));
        }

        return SlotDisplay.Empty.INSTANCE;
    }
}
