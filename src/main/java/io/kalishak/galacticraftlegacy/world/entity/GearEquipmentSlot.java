package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import org.jspecify.annotations.NonNull;

public enum GearEquipmentSlot implements SerializableEnum {
    HELMET(EquipmentSlot.HEAD, 0, "helmet"),
    GEAR(EquipmentSlot.CHEST, 1, "gear"),
    LEFT_TANK(EquipmentSlot.CHEST, 2, "left_tank"),
    RIGHT_TANK(EquipmentSlot.CHEST, 3, "right_tank"),
    PARACHUTE(EquipmentSlot.CHEST, 4, "parachute"),
    TELEMETRY(EquipmentSlot.HEAD, 5, "telemetry"),
    THERMAL_HEAD(EquipmentSlot.HEAD, 6, "thermal_head_piece"),
    THERMAL_CHEST(EquipmentSlot.HEAD, 7, "thermal_chest_piece"),
    THERMAL_LEG(EquipmentSlot.HEAD, 8, "thermal_leg_piece"),
    THERMAL_FOOT(EquipmentSlot.HEAD, 9, "thermal_foot_piece"),;

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

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public static GearEquipmentSlot byName(String name) {
        GearEquipmentSlot gearSlot = CODEC.byName(name);

        if (gearSlot == null) {
            throw new IllegalArgumentException("Unknown GearEquipmentSlot= " + name);
        }

        return gearSlot;
    }

    public boolean isGear() {
        return this.index < 4;
    }

    public boolean isThermal() {
        return this.index >= 6;
    }
}
