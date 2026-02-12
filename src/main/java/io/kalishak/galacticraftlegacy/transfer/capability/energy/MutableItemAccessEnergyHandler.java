package io.kalishak.galacticraftlegacy.transfer.capability.energy;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class MutableItemAccessEnergyHandler extends ItemAccessEnergyHandler {
    public MutableItemAccessEnergyHandler(ItemAccess itemAccess, DataComponentType<Integer> energyComponent, int capacity, int maxTransfer) {
        super(itemAccess, energyComponent, capacity, maxTransfer);
    }

    public MutableItemAccessEnergyHandler(ItemAccess itemAccess, DataComponentType<Integer> energyComponent, int capacity) {
        super(itemAccess, energyComponent, capacity);
    }

    public void set(int amount) {
        ItemResource accessResource = this.itemAccess.getResource();

        if (accessResource.is(this.validItem)) {
            update(accessResource, amount);
        }
    }
}
