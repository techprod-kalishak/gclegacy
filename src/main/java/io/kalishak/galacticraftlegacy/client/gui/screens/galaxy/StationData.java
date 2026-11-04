/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.galaxy;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public record StationData(String stationId, ResourceKey<DimensionType> stationDimension) {
}
