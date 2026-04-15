/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.server.commands.arguments.item;

import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlotGroup;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public enum EmergencyEquipment implements StringRepresentable {
    LIGHT("light", GearEquipmentSlotGroup.GEAR, GalacticraftItems.LIGHT_TANK::value),
    MEDIUM("medium", GearEquipmentSlotGroup.GEAR, GalacticraftItems.MEDIUM_TANK::value),
    HEAVY("heavy", GearEquipmentSlotGroup.GEAR, GalacticraftItems.HEAVY_TANK::value),
    FULL("full", GearEquipmentSlotGroup.ANY, GalacticraftItems.INFINITE_OXYGEN_TANK::value);

    private final String name;
    private final GearEquipmentSlotGroup group;
    private final Supplier<Item> preferredTank;

    EmergencyEquipment(String name, GearEquipmentSlotGroup group, Supplier<Item> preferredTank) {
        this.name = name;
        this.group = group;
        this.preferredTank = preferredTank;
    }

    public static EmergencyEquipment byName(String name) {
        for (EmergencyEquipment arg : values()) {
            if (arg.name.equals(name)) {
                return arg;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.name + "[slots=" + this.group.getSerializedName() + ", preferredTank=" + this.preferredTank.get().getDescriptionId() + "]";
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public GearEquipmentSlotGroup getGroup() {
        return this.group;
    }

    public Supplier<Item> getPreferredTank() {
        return this.preferredTank;
    }
}