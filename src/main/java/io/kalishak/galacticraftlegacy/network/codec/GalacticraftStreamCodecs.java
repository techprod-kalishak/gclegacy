/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.codec;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public class GalacticraftStreamCodecs {
    public static final StreamCodec<RegistryFriendlyByteBuf, NonNullList<Ingredient>> INGREDIENT_NONNULL_LIST_STREAM_CODEC = Ingredient.CONTENTS_STREAM_CODEC
            .apply(ByteBufCodecs.collection(size -> NonNullList.withSize(size, Ingredient.of())));
}
