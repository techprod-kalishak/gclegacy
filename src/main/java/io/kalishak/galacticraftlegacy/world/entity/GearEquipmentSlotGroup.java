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
    TOOL(2, "tool", slot -> slot == GearEquipmentSlot.TELEMETRY || slot == GearEquipmentSlot.PARACHUTE),
    THERMAL(3, "thermal", GearEquipmentSlot::isThermal);

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
            case MASK, GEAR, TANK, ADDITIONAL_TANK -> GEAR;
            case TELEMETRY, PARACHUTE -> TOOL;
            case THERMAL_CAP, THERMAL_SHIRT, THERMAL_LEGGINGS, THERMAL_SOCKS -> THERMAL;
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
