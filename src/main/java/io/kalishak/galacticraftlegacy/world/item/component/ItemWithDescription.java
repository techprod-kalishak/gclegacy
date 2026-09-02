/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.config.values.EnergyUnit;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record ItemWithDescription(String translationKey, int energyPerTick, Optional<Style> style) implements TooltipProvider {
    public static final Codec<ItemWithDescription> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("translation_key").forGetter(ItemWithDescription::translationKey),
            Codec.INT.optionalFieldOf("energy_per_tick", 0).forGetter(ItemWithDescription::energyPerTick),
            Style.Serializer.CODEC.optionalFieldOf("style").forGetter(ItemWithDescription::style)
    ).apply(instance, ItemWithDescription::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemWithDescription> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ItemWithDescription::translationKey,
            ByteBufCodecs.INT, ItemWithDescription::energyPerTick,
            Style.Serializer.TRUSTED_STREAM_CODEC.apply(ByteBufCodecs::optional), ItemWithDescription::style,
            ItemWithDescription::new
    );

    public static Supplier<Item.Properties> withDescription(Supplier<Item.Properties> properties, Identifier id) {
        return () -> properties.get().component(GalacticraftDataComponents.ITEM_WITH_DESCRIPTION, new ItemWithDescription(id.toLanguageKey("item", "desc")));
    }

    public ItemWithDescription(String translationKey, int energyPerTick) {
        this(translationKey, energyPerTick, Optional.empty());
    }

    public ItemWithDescription(String translationKey) {
        this(translationKey, 0, Optional.empty());
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if (this.energyPerTick > 0) {
            tooltipAdder.accept(
                    GalacticraftComponents.TOOLTIP_ENERGY_PER_TICK
                            .apply(Constants.calculateUnit(this.energyPerTick, Constants.ifClient(context.level(), ClientConfig.ENERGY_UNIT, EnergyUnit.GIGA_JOULES)))
                            .withStyle(ChatFormatting.GREEN)
            );
        }

        if (!flag.hasShiftDown()) {
            tooltipAdder.accept(GalacticraftComponents.TOOLTIP_MORE.asComponent());
        } else {
            tooltipAdder.accept(Component.translatable(this.translationKey).withStyle(this.style.orElse(Style.EMPTY.applyFormat(ChatFormatting.GRAY))));
        }
    }
}
