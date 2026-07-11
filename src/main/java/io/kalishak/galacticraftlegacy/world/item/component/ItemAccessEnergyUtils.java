/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.config.values.EnergyUnit;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EmptyEnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler;
import net.neoforged.neoforge.transfer.energy.VoidingEnergyHandler;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ItemAccessEnergyUtils extends TooltipProvider {
    int stored();
    int capacity();

    @Override
    default void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if (stored() > 0 && componentGetter instanceof ItemStack stack) {
            EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

            if (energyHandler != null) {
                ItemAccessEnergyUtils.addTooltip(energyHandler, Constants.ifClient(context.level(), ClientConfig.ENERGY_UNIT, EnergyUnit.GIGA_JOULES), tooltipAdder);
            }
        }
    }

    static boolean hasEnergyHandler(ItemStack stack) {
        if (stack.isEmpty()) return false;

        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        return energyHandler != null;
    }

    static EnergyHandler getEnergyHandler(ItemStack stack) {
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        return energyHandler == null ? EmptyEnergyHandler.INSTANCE : energyHandler;
    }

    static void addTooltip(@NonNull EnergyHandler energyHandler, Supplier<EnergyUnit> energyUnit, Consumer<Component> tooltipAdder) {
        if (energyHandler instanceof InfiniteEnergyHandler) {
            GalacticraftComponents.infinite(tooltipAdder);
        } else {
            Constants.energy(energyHandler.getAmountAsInt(), energyHandler.getCapacityAsInt(), energyUnit, tooltipAdder);
        }
    }

    static int colorFromStorage(EnergyHandler energyHandler) {
        return colorFromStorage(energyHandler.getAmountAsInt(), energyHandler.getCapacityAsInt());
    }

    static int colorFromStorage(int stored, int capacity) {
        float per = Math.clamp((float) stored / (float) capacity, 0.0F, 1.0F);

        int r = Math.round(255.0F * (1.0F - per));
        int g = Math.round(255.0F * per);

        return r << 16 | g << 8;
    }
}
