/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.attachment.level.race.FlagData;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record FlagItemData(FlagData flagData, Component teamName) implements TooltipProvider {
    public static final Codec<FlagItemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FlagData.CODEC.fieldOf("flag_data").forGetter(FlagItemData::flagData),
            ComponentSerialization.CODEC.optionalFieldOf("team_name", Component.empty()).forGetter(FlagItemData::teamName)
    ).apply(instance, FlagItemData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FlagItemData> STREAM_CODEC = StreamCodec.composite(
            FlagData.STREAM_CODEC, FlagItemData::flagData,
            ComponentSerialization.STREAM_CODEC, FlagItemData::teamName,
            FlagItemData::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if (!this.teamName.getString().isEmpty()) {
            tooltipAdder.accept(Component.translatable("space_race.galacticraftlegacy.team_flag").append(this.teamName));
        }
    }
}
