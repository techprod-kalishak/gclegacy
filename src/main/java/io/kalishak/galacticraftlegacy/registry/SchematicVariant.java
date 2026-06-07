/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;
import java.util.function.Function;

public record SchematicVariant(FeatureTier tier, Identifier assetId, Component title) implements TooltipProvider {
    public static final Codec<SchematicVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FeatureTier.CODEC.fieldOf("level").forGetter(SchematicVariant::tier),
            Identifier.CODEC.fieldOf("asset_id").forGetter(SchematicVariant::assetId),
            ComponentSerialization.CODEC.fieldOf("title").forGetter(SchematicVariant::title)
    ).apply(instance, SchematicVariant::new));
    public static final Codec<Holder<SchematicVariant>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.SCHEMATIC);
    public static final MapCodec<Holder<SchematicVariant>> MAP_CODEC = CODEC.fieldOf("variant");
    public static final StreamCodec<RegistryFriendlyByteBuf, SchematicVariant> DIRECT_STREAM_CODEC = StreamCodec.composite(
            FeatureTier.STREAM_CODEC, SchematicVariant::tier,
            Identifier.STREAM_CODEC, SchematicVariant::assetId,
            ComponentSerialization.TRUSTED_STREAM_CODEC, SchematicVariant::title,
            SchematicVariant::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SchematicVariant>> STREAM_CODEC = ByteBufCodecs.holder(
            GalacticraftRegistries.Keys.SCHEMATIC,
            DIRECT_STREAM_CODEC
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        tooltipAdder.accept(this.title);
    }
}