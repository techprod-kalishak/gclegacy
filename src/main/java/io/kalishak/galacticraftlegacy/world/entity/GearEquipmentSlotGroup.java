/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public enum GearEquipmentSlotGroup implements SerializableEnum, Iterable<GearEquipmentSlot>, Predicate<GearEquipmentSlot> {
    ANY(0, "any", Predicates.alwaysTrue()),
    GEAR(1, "gear", GearEquipmentSlot::isGear),
    TANK(2, "tank", GearEquipmentSlot::isTank),
    THERMAL_HEAD(3, "thermal_head", GearEquipmentSlot.THERMAL_HEAD),
    THERMAL_CHEST(4, "thermal_chest", GearEquipmentSlot.THERMAL_CHEST),
    THERMAL_LEGS(5, "thermal_legs", GearEquipmentSlot.THERMAL_LEGS),
    THERMAL_FEET(6, "thermal_feet", GearEquipmentSlot.THERMAL_FEET);

    public static final Codec<GearEquipmentSlotGroup> CODEC = SerializableEnum.codec(GearEquipmentSlotGroup.class);
    public static final StreamCodec<ByteBuf, GearEquipmentSlotGroup> STREAM_CODEC = SerializableEnum.streamCodec(GearEquipmentSlotGroup.class);
    private final int id;
    private final String key;
    private final Predicate<GearEquipmentSlot> validator;
    private final List<GearEquipmentSlot> validSlots;

    GearEquipmentSlotGroup(int id, String key, Predicate<GearEquipmentSlot> validator) {
        this.id = id;
        this.key = key;
        this.validator = validator;
        this.validSlots = Arrays.stream(GearEquipmentSlot.values()).filter(validator).toList();
    }

    GearEquipmentSlotGroup(int id, String key, GearEquipmentSlot validSlot) {
        this(id, key, slot -> slot == validSlot);
    }

    public static GearEquipmentSlotGroup bySlot(GearEquipmentSlot slot) {
        return switch (slot) {
            case TANK, ADDITIONAL_TANK -> TANK;
            case THERMAL_HEAD -> THERMAL_HEAD;
            case THERMAL_CHEST -> THERMAL_CHEST;
            case THERMAL_LEGS -> THERMAL_LEGS;
            case THERMAL_FEET -> THERMAL_FEET;
            default -> GEAR;
        };
    }

    public List<GearEquipmentSlot> validSlots() {
        return this.validSlots;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.key;
    }

    @Override
    public int getIndex() {
        return this.id;
    }

    @Override
    public @NotNull Iterator<GearEquipmentSlot> iterator() {
        return this.validSlots.iterator();
    }

    @Override
    public boolean test(GearEquipmentSlot slot) {
        return this.validator.test(slot);
    }
}
