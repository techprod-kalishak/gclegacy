package io.kalishak.galacticraftlegacy.world.item.component;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler;
import net.neoforged.neoforge.transfer.energy.VoidingEnergyHandler;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public interface ItemAccessEnergyUtils extends TooltipProvider {
    int stored();
    int capacity();

    @Override
    default void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if (stored() > 0 && componentGetter instanceof ItemStack stack) {
            EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

            if (energyHandler instanceof InfiniteEnergyHandler) {
                Constants.infinite(tooltipAdder);
            } else if (energyHandler != null) {
                addTooltip(stored(), capacity(), tooltipAdder);
            }
        }
    }

    static boolean hasEnergyHandler(ItemStack stack) {
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        return energyHandler != null && energyHandler != VoidingEnergyHandler.INSTANCE;
    }

    static EnergyHandler getEnergyHandler(ItemStack stack) {
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        return energyHandler == null ? new VoidingEnergyHandler() : energyHandler;
    }

    static void addTooltip(@NonNull EnergyHandler energyHandler, Consumer<Component> tooltipAdder) {
        if (energyHandler instanceof InfiniteEnergyHandler) {
            Constants.infinite(tooltipAdder);
        } else {
            addTooltip(energyHandler.getAmountAsInt(), energyHandler.getCapacityAsInt(), tooltipAdder);
        }
    }

    static void addTooltip(int stored, int capacity, Consumer<Component> tooltipAdder) {
        tooltipAdder.accept(
                Component.translatable("item.galacticraftlegacy.battery.tooltip").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(stored + "/" + capacity + " gJ")
                                .withStyle(Style.EMPTY.withColor(ItemAccessEnergyUtils.colorFromStorage(stored, capacity))))
        );
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
