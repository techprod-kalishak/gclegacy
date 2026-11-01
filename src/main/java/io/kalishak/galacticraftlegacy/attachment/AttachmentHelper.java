/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment;

import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface AttachmentHelper {
    static GearInventoryProvider getGearInventory(Entity entity) {
        if (entity.is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)) {
            if (entity instanceof Player) {
                return entity.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
            }

            return entity.getData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY);
        }

        throw new IllegalArgumentException("Attempted to extract GearInventoryProvider from disallowed entity type: " + entity.getType() + ", is it in tag: " + GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR + "?");
    }

    static boolean hasGearInventory(Entity entity) {
        return entity.hasData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY) || entity.hasData(GalacticraftAttachments.PLAYER_SPACE_DATA);
    }
}
