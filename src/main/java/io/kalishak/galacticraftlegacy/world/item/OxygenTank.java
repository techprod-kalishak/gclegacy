package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public record OxygenTank(long capacity, DyeColor indicator, ResourceKey<EquipmentAsset> assetId) {
    public static final Codec<OxygenTank> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("capacity").forGetter(OxygenTank::capacity),
            DyeColor.CODEC.fieldOf("indicator").forGetter(OxygenTank::indicator),
            ResourceKey.codec(EquipmentAssets.ROOT_ID).fieldOf("asset_id").forGetter(OxygenTank::assetId)
    ).apply(instance, OxygenTank::new));
    public static final StreamCodec<ByteBuf, OxygenTank> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, OxygenTank::capacity,
            DyeColor.STREAM_CODEC, OxygenTank::indicator,
            ResourceKey.streamCodec(EquipmentAssets.ROOT_ID), OxygenTank::assetId,
            OxygenTank::new
    );
}
