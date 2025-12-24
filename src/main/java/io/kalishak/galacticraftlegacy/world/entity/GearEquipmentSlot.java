package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.GearEquippable;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public enum GearEquipmentSlot implements SerializableEnum {
    MASK(EquipmentSlot.HEAD, 0, "mask"),
    GEAR(EquipmentSlot.CHEST, 1, "gear"),
    TANK(EquipmentSlot.CHEST, 2, "tank"),
    ADDITIONAL_TANK(EquipmentSlot.CHEST, 3, "additional_tank"),
    PARACHUTE(EquipmentSlot.CHEST, 4, "parachute"),
    TELEMETRY(EquipmentSlot.HEAD, 5, "telemetry"),
    THERMAL_CAP(EquipmentSlot.HEAD, 6, "thermal_cap"),
    THERMAL_SHIRT(EquipmentSlot.HEAD, 7, "thermal_shirt"),
    THERMAL_LEGGINGS(EquipmentSlot.HEAD, 8, "thermal_leggings"),
    THERMAL_SOCKS(EquipmentSlot.HEAD, 9, "thermal_foot_socks");

    public static final EnumCodec<@NonNull GearEquipmentSlot> CODEC = StringRepresentable.fromEnum(GearEquipmentSlot::values);
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
            for (GearEquipmentSlot slot : GearEquipmentSlot.values()) {
                if (slot.index == index) return slot;
            }
        }

        throw new IndexOutOfBoundsException("Index " +  index + " is out of bounds for GearEquipmentSlot.");
    }

    public boolean isTank() {
        return this == TANK || this == ADDITIONAL_TANK;
    }

    public boolean isGear() {
        return this.index < THERMAL_CAP.getIndex();
    }

    public boolean isThermal() {
        return this.index >= THERMAL_CAP.getIndex();
    }
}
