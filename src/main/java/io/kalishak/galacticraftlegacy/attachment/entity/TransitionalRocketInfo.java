/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.RocketTier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record TransitionalRocketInfo(RocketTier.Type rocketType, ItemStack rocket, int fuelAmount) {
    public static final Codec<TransitionalRocketInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RocketTier.Type.CODEC.fieldOf("rocket_type").forGetter(TransitionalRocketInfo::rocketType),
            ItemStack.CODEC.fieldOf("rocket").forGetter(TransitionalRocketInfo::rocket),
            Codec.INT.fieldOf("fuel").forGetter(TransitionalRocketInfo::fuelAmount)
    ).apply(instance, TransitionalRocketInfo::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TransitionalRocketInfo> STREAM_CODEC = StreamCodec.composite(
            RocketTier.Type.STREAM_CODEC, TransitionalRocketInfo::rocketType,
            ItemStack.STREAM_CODEC, TransitionalRocketInfo::rocket,
            ByteBufCodecs.INT, TransitionalRocketInfo::fuelAmount,
            TransitionalRocketInfo::new
    );
}
