/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.advancements.criterion;

import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class PredicateByteBufs {
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPredicate> ITEM_PREDICATE = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ITEM).apply(ByteBufCodecs::optional), ItemPredicate::items,
            MinMaxBounds.Ints.STREAM_CODEC, ItemPredicate::count,
            DataComponentMatchers.STREAM_CODEC, ItemPredicate::components,
            ItemPredicate::new
    );
}
