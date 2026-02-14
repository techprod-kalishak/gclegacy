package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.world.item.component.ItemAccessEnergyUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public class BatteryItem extends Item {
    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return ItemAccessEnergyUtils.colorFromStorage(ItemAccessEnergyUtils.getEnergyHandler(stack));
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        EnergyHandler energyHandler = ItemAccessEnergyUtils.getEnergyHandler(stack);
        return Math.min(1 + energyHandler.getAmountAsInt() * 12 / energyHandler.getCapacityAsInt(), 13);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        EnergyHandler energyHandler = ItemAccessEnergyUtils.getEnergyHandler(stack);
        return energyHandler.getAmountAsInt() != energyHandler.getCapacityAsInt();
    }
}
