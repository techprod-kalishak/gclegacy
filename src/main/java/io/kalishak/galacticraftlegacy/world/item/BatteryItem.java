package io.kalishak.galacticraftlegacy.world.item;

import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.VoidingEnergyHandler;

public class BatteryItem extends Item {
    public BatteryItem(Properties properties) {
        super(properties);
    }

    private static EnergyHandler getEnergyHandler(ItemStack stack) {
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        return energyHandler == null ? new VoidingEnergyHandler() : energyHandler;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return EnergyTooltip.colorFromStorage(getEnergyHandler(stack));
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        EnergyHandler energyHandler = getEnergyHandler(stack);
        return Mth.clamp(Math.round((energyHandler.getCapacityAsInt() - energyHandler.getAmountAsInt()) / 100.0F * 13.0F - 13.0F), 0, 13);
    }
}
