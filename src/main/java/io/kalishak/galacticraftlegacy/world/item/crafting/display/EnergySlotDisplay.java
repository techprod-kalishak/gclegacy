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
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.stream.Stream;

public record EnergySlotDisplay(Holder<Item> item, int energyNeeded) implements SlotDisplay {
    public static final MapCodec<EnergySlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Item.CODEC.fieldOf("item").forGetter(EnergySlotDisplay::item),
            Codec.INT.fieldOf("energy_needed").forGetter(EnergySlotDisplay::energyNeeded)
    ).apply(instance, EnergySlotDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, EnergySlotDisplay> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, EnergySlotDisplay::item,
            ByteBufCodecs.INT, EnergySlotDisplay::energyNeeded,
            EnergySlotDisplay::new
    );

    @Override
    public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> output) {
        return Stream.empty();
    }

    @Override
    public Type<EnergySlotDisplay> type() {
        return null;
    }
}
