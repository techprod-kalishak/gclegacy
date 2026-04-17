/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.monster;

import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;

public interface EvolvedMonster {
    static void populateDefault(SpaceGearEquipment empty) {
        empty.set(GearEquipmentSlot.MASK, GalacticraftItems.OXYGEN_MASK.toStack());
        empty.set(GearEquipmentSlot.GEAR, GalacticraftItems.OXYGEN_GEAR.toStack());
        empty.set(GearEquipmentSlot.TANK, GalacticraftItems.HEAVY_TANK.toStack());
        empty.set(GearEquipmentSlot.ADDITIONAL_TANK, GalacticraftItems.HEAVY_TANK.toStack());
    }
}
