/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.Map;

public record GearDropChances(Map<GearEquipmentSlot, Float> byGear) {
    public static final float DEFAULT_EQUIPMENT_DROP_CHANCE = 0.045F;
    public static final float PRESERVE_ITEM_DROP_CHANCE_THRESHOLD = 1.0F;
    public static final int PRESERVE_ITEM_DROP_CHANCE = 2;
    public static final GearDropChances DEFAULT = new GearDropChances(Util.makeEnumMap(GearEquipmentSlot.class, _ -> DEFAULT_EQUIPMENT_DROP_CHANCE));
    public static final Codec<GearDropChances> CODEC = Codec.unboundedMap(GearEquipmentSlot.CODEC, ExtraCodecs.NON_NEGATIVE_FLOAT)
            .xmap(GearDropChances::toEnumMap, GearDropChances::filterDefaultValues)
            .xmap(GearDropChances::new, GearDropChances::byGear);
    public static final StreamCodec<ByteBuf, GearDropChances> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, GearEquipmentSlot.STREAM_CODEC, ByteBufCodecs.FLOAT), GearDropChances::byGear,
            GearDropChances::new
    );

    private static Map<GearEquipmentSlot, Float> filterDefaultValues(Map<GearEquipmentSlot, Float> map) {
        Map<GearEquipmentSlot, Float> filteredMap = new HashMap<>(map);
        filteredMap.values().removeIf(chance -> chance == DEFAULT_EQUIPMENT_DROP_CHANCE);
        return filteredMap;
    }

    private static Map<GearEquipmentSlot, Float> toEnumMap(Map<GearEquipmentSlot, Float> map) {
        return Util.makeEnumMap(GearEquipmentSlot.class, slot -> map.getOrDefault(slot, DEFAULT_EQUIPMENT_DROP_CHANCE));
    }

    public GearDropChances withGuaranteedDrop(GearEquipmentSlot slot) {
        return this.withEquipmentChance(slot, PRESERVE_ITEM_DROP_CHANCE);
    }

    public GearDropChances withEquipmentChance(GearEquipmentSlot slot, float chance) {
        if (chance < 0.0F) {
            throw new IllegalArgumentException("Tried to set invalid gear equipment chance " + chance + " for " + slot);
        } else {
            return byGear(slot) == chance
                    ? this
                    : new GearDropChances(Util.makeEnumMap(GearEquipmentSlot.class, newSlot -> newSlot == slot ? chance : byGear(newSlot)));
        }
    }

    public float byGear(GearEquipmentSlot slot) {
        return this.byGear.getOrDefault(slot, DEFAULT_EQUIPMENT_DROP_CHANCE);
    }

    public boolean isPreserved(GearEquipmentSlot slot) {
        return this.byGear(slot) > PRESERVE_ITEM_DROP_CHANCE_THRESHOLD;
    }
}
