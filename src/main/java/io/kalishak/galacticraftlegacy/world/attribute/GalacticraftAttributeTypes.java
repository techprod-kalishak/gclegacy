/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.attribute;

import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.world.attribute.AttributeType;

public final class GalacticraftAttributeTypes {
    public static final AttributeType<EarthPhase> EARTH_PHASE = AttributeType.ofNotInterpolated(EarthPhase.CODEC);
    public static final AttributeType<PlanetAttribute> PLANET_ATTRIBUTE = AttributeType.ofNotInterpolated(PlanetAttribute.CODEC);
}
