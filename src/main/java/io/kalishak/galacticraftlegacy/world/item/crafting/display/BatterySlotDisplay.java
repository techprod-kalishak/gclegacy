/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record BatterySlotDisplay(SlotDisplay battery, SlotDisplay depletedBattery, int energyNeeded) implements SlotDisplay {
    public static final MapCodec<BatterySlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SlotDisplay.CODEC.fieldOf("battery").forGetter(BatterySlotDisplay::battery),
            SlotDisplay.CODEC.fieldOf("depleted_battery").forGetter(BatterySlotDisplay::battery),
            Codec.INT.fieldOf("energy_needed").forGetter(BatterySlotDisplay::energyNeeded)
    ).apply(instance, BatterySlotDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BatterySlotDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC, BatterySlotDisplay::battery,
            SlotDisplay.STREAM_CODEC, BatterySlotDisplay::depletedBattery,
            ByteBufCodecs.INT, BatterySlotDisplay::energyNeeded,
            BatterySlotDisplay::new
    );

    @Override
    public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> output) {
        BinaryOperator<ItemStack> transformation = (battery, depletion) -> {
            int energy = battery.getOrDefault(GalacticraftDataComponents.STORED_ENERGY, 0);

            ItemStack result = depletion.copy();
            result.set(GalacticraftDataComponents.STORED_ENERGY, Math.min(energy - this.energyNeeded, 0));
            return result;
        };

        return applyDemoTransformation(context, output, this.battery, this.depletedBattery, transformation);
    }

    @Override
    public Type<BatterySlotDisplay> type() {
        return GalacticraftSlotDisplays.BATTERY.get();
    }

    private static <T> Stream<T> applyDemoTransformation(ContextMap context, DisplayContentsFactory<T> factory, SlotDisplay firstDisplay, SlotDisplay secondDisplay, BinaryOperator<ItemStack> operation) {
        if (factory instanceof DisplayContentsFactory.ForStacks<T> stacks) {
            List<ItemStack> firstItems = firstDisplay.resolveForStacks(context);
            if (firstItems.isEmpty()) {
                return Stream.empty();
            } else {
                List<ItemStack> secondItems = secondDisplay.resolveForStacks(context);
                if (secondItems.isEmpty()) {
                    return Stream.empty();
                } else {
                    int cycle = firstItems.size() * secondItems.size();
                    return IntStream.range(0, cycle).mapToObj(index -> {
                        int firstItemCount = firstItems.size();
                        int firstItemIndex = index % firstItemCount;
                        int secondItemIndex = index / firstItemCount;
                        ItemStack first = firstItems.get(firstItemIndex);
                        ItemStack second = secondItems.get(secondItemIndex);
                        return operation.apply(first, second);
                    }).filter(s -> !s.isEmpty()).limit(16L).map(stacks::forStack);
                }
            }
        } else {
            return Stream.empty();
        }
    }
}
