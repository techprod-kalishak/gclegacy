package io.kalishak.galacticraftlegacy.world.item;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

import java.util.function.Consumer;

public interface EnergyTooltip extends TooltipProvider {
    int stored();
    int capacity();

    @Override
    default void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if (stored() > 0 && flag.hasShiftDown()) {
            tooltipAdder.accept(addTooltip(new SimpleEnergyHandler(capacity(), 0, 0, stored())));
        }
    }

    static Component addTooltip(EnergyHandler energyHandler) {
        return Component.translatable("item.galacticraftlegacy.battery.tooltip", Component.literal(energyHandler.getAmountAsInt() + "/" + energyHandler.getCapacityAsInt()).withStyle(Style.EMPTY.withColor(EnergyTooltip.colorFromStorage(energyHandler))));
    }

    static int colorFromStorage(EnergyHandler energyHandler) {
        return colorFromStorage(energyHandler.getAmountAsInt(), energyHandler.getCapacityAsInt());
    }

    static int colorFromStorage(int stored, int capacity) {
        float perc = Math.max(0.0F, Math.min(1.0F, (float) stored / (float) capacity));

        int r = Math.round(255 * (1 - perc));
        int g = Math.round(255 * perc);

        return r << 16 | g << 8;
    }
}
