/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public enum GearEquipmentSlot implements SerializableEnum {
    THERMAL_CAP(EquipmentSlot.HEAD, 0, "thermal_cap"),
    THERMAL_SHIRT(EquipmentSlot.CHEST, 1, "thermal_shirt"),
    THERMAL_LEGGINGS(EquipmentSlot.LEGS, 2, "thermal_leggings"),
    THERMAL_SOCKS(EquipmentSlot.FEET, 3, "thermal_foot_socks"),
    MASK(EquipmentSlot.HEAD, 4, "mask"),
    GEAR(EquipmentSlot.CHEST, 5, "gear"),
    TANK(EquipmentSlot.CHEST, 6, "tank"),
    ADDITIONAL_TANK(EquipmentSlot.CHEST, 7, "additional_tank"),
    PARACHUTE(EquipmentSlot.CHEST, 8, "parachute"),
    TELEMETRY(EquipmentSlot.HEAD, 9, "telemetry"),
    SHIELD(EquipmentSlot.BODY, 10, "shield");

    public static final EnumCodec<GearEquipmentSlot> CODEC = StringRepresentable.fromEnum(GearEquipmentSlot::values);
    public static final StreamCodec<ByteBuf, GearEquipmentSlot> STREAM_CODEC = SerializableEnum.streamCodec(GearEquipmentSlot.class);
    private final EquipmentSlot relatedEquipment;
    private final int index;
    private final String name;

    GearEquipmentSlot(EquipmentSlot relatedEquipment, int index, String name) {
        this.relatedEquipment = relatedEquipment;
        this.index = index;
        this.name = name;
    }

    public EquipmentSlot getRelatedEquipment() {
        return this.relatedEquipment;
    }

    public static @Nullable GearEquipmentSlot getGearSlotForItem(ItemStack stack) {
        if (stack.isEmpty()) return null;

        GearEquippable equippable = stack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        return equippable != null ? equippable.gearSlot() : null;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public static GearEquipmentSlot byId(int index) {
        if (index >= 0 && index < GearEquipmentSlot.values().length) {
            return GearEquipmentSlot.values()[index];
        }

        throw new IndexOutOfBoundsException("Index " +  index + " is out of bounds for GearEquipmentSlot.");
    }

    public boolean isTank() {
        return this == TANK || this == ADDITIONAL_TANK;
    }

    public boolean isGear() {
        return this.index >= MASK.getIndex() && this.index <= PARACHUTE.getIndex();
    }

    public boolean isThermal() {
        return this.index < MASK.getIndex();
    }
}
