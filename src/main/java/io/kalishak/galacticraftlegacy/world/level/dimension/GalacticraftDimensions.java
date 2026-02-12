package io.kalishak.galacticraftlegacy.world.level.dimension;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class GalacticraftDimensions {
    public static final ResourceKey<Level> EARTH_ORBIT = Constants.key(Registries.DIMENSION, "earth_orbit");
    public static final ResourceKey<Level> MOON = Constants.key(Registries.DIMENSION, "moon");
    public static final ResourceKey<Level> MARS = Constants.key(Registries.DIMENSION, "mars");
    public static final ResourceKey<Level> ASTEROIDS = Constants.key(Registries.DIMENSION, "asteroids");
    public static final ResourceKey<Level> VENUS = Constants.key(Registries.DIMENSION, "venus");
}
