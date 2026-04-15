/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class GalacticraftKeys {
    public static final KeyMapping.Category GC_CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Galacticraft.MODID, "galacticraft"));
    public static final KeyMapping OPEN_GEAR_KEY = new KeyMapping(
            "galacticraftlegacy.key.toggle_gear_inventory",
            InputConstants.KEY_G,
            GC_CATEGORY
    );
    public static final KeyMapping OPEN_GALAXY_MAP = new KeyMapping(
            "galacticraftlegacy.key.open_galaxy_map",
            InputConstants.KEY_M,
            GC_CATEGORY
    );
    public static final KeyMapping ACTIVATE_SENSOR_GLASSES = new KeyMapping(
            "galacticraftlegacy.key.activate_sensor_glasses",
            InputConstants.KEY_K,
            GC_CATEGORY
    );

    static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.registerCategory(GC_CATEGORY);
        event.register(OPEN_GEAR_KEY);
        event.register(OPEN_GALAXY_MAP);
        event.register(ACTIVATE_SENSOR_GLASSES);
    }
}
