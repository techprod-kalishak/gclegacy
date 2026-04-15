/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;

@SuppressWarnings("unused")
public class GalacticraftLevelStem {
    public static final ResourceKey<LevelStem> EARTH_ORBIT = Constants.key(Registries.LEVEL_STEM, "earth_orbit");
    public static final ResourceKey<LevelStem> MOON = Constants.key(Registries.LEVEL_STEM, "moon");
    public static final ResourceKey<LevelStem> MARS = Constants.key(Registries.LEVEL_STEM, "mars");
    public static final ResourceKey<LevelStem> ASTEROIDS = Constants.key(Registries.LEVEL_STEM, "asteroids");
    public static final ResourceKey<LevelStem> VENUS = Constants.key(Registries.LEVEL_STEM, "venus");

    public static void bootstrap(BootstrapContext<LevelStem> cxt) {

    }
}
