/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui;

import io.kalishak.galacticraftlegacy.ClientConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

import java.util.function.UnaryOperator;

public class ClientResourceHandlerTextUtils {

    public static Component energyComponent(int amount, boolean applyPerTickSuffix, UnaryOperator<Style> withStyle) {
        return Component.literal(ClientConfig.ENERGY_UNIT.get().calculate(amount) + " " + ClientConfig.ENERGY_UNIT.get().getUnit() + (applyPerTickSuffix ? "/t" : "")).withStyle(withStyle);
    }

    public static Component energyComponent(int amount, boolean applyPerTickSuffix) {
        return energyComponent(amount, applyPerTickSuffix, UnaryOperator.identity());
    }

    public static Component energyComponent(EnergyHandler energyHandler, boolean applyPerTickSuffix, UnaryOperator<Style> withStyle) {
        return energyComponent(energyHandler.getAmountAsInt(), applyPerTickSuffix, withStyle);
    }

    public static Component energyComponentWithCapacity(int amount, int capacity, UnaryOperator<Style> withStyle) {
        return Component.translatable("item.galacticraftlegacy.battery.tooltip", ClientConfig.ENERGY_UNIT.get().calculate(amount) + "/" + ClientConfig.ENERGY_UNIT.get().calculate(capacity) + " " + ClientConfig.ENERGY_UNIT.get().getUnit()).withStyle(withStyle);
    }

    public static Component energyComponentWithCapacity(EnergyHandler energyHandler, UnaryOperator<Style> withStyle) {
        return energyComponentWithCapacity(energyHandler.getAmountAsInt(), energyHandler.getCapacityAsInt(), withStyle);
    }
}
