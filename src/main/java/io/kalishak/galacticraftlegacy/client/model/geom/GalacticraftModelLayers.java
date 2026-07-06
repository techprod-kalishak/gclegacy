/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.geom;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ArmorModelSet;

public class GalacticraftModelLayers {
    public static final ModelLayerLocation FLAG = createLocation("flag");
    public static final ModelLayerLocation KEY = createLocation("key");
    public static final ModelLayerLocation OXYGEN_MASK = createLocation("oxygen_mask");
    public static final ModelLayerLocation OXYGEN_GEAR = createLocation("oxygen_gear");
    public static final ModelLayerLocation HEAVY_OXYGEN_TANK = createLocation("heavy_oxygen_tank");
    public static final ModelLayerLocation MEDIUM_OXYGEN_TANK = createLocation("medium_oxygen_tank");
    public static final ModelLayerLocation LIGHT_OXYGEN_TANK = createLocation("light_oxygen_tank");
    public static final ModelLayerLocation PARACHUTE = createLocation("parachute");
    public static final ArmorModelSet<ModelLayerLocation> THERMAL_PADDING = new ArmorModelSet<>(
            createLocation("head_thermal_padding"),
            createLocation("chest_thermal_padding"),
            createLocation("legs_thermal_padding"),
            createLocation("foot_thermal_padding")
    );

    private static ModelLayerLocation createLocation(String path, String model) {
        return new ModelLayerLocation(Constants.id(path), model);
    }

    private static ModelLayerLocation createLocation(String path) {
        return createLocation(path, "main");
    }
}
