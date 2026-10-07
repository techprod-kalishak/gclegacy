package io.kalishak.galacticraftlegacy.client.gui.screens.galaxy;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public record StationData(String stationId, ResourceKey<DimensionType> stationDimension) {
}
