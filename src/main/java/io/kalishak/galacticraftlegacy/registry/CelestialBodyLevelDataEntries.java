/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.galaxies.GalacticraftGalaxies;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.EarthPlanetaryTranstion;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.FixedPlanetaryTransition;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.LanderPlanetaryTransition;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.function.Supplier;

public class CelestialBodyLevelDataEntries {
    public static final ResourceKey<CelestialBodyLevelData> OVERWORLD = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "overworld");
    public static final ResourceKey<CelestialBodyLevelData> OVERWORLD_CAVES = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "overworld_caves");
    public static final ResourceKey<CelestialBodyLevelData> THE_NETHER = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "the_nether");
    public static final ResourceKey<CelestialBodyLevelData> THE_END = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "the_end");
    public static final ResourceKey<CelestialBodyLevelData> EARTH_ORBIT = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "earth_orbit");
    public static final ResourceKey<CelestialBodyLevelData> MOON = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "moon");
    public static final ResourceKey<CelestialBodyLevelData> MARS = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "mars");
    public static final ResourceKey<CelestialBodyLevelData> ASTEROIDS = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "asteroids");
    public static final ResourceKey<CelestialBodyLevelData> VENUS = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA, "venus");
    public static final Supplier<CelestialBodyLevelData> PLACEHOLDER = () -> new CelestialBodyLevelData(
            GalacticraftGalaxies.OVERWORLD,
            AtmosphereInfo.EARTH,
            1.0F,
            new EarthPlanetaryTranstion(),
            Optional.empty()
    );

    public static void bootstrap(BootstrapContext<CelestialBodyLevelData> cxt) {
        cxt.register(
                OVERWORLD,
                PLACEHOLDER.get()
        );
        cxt.register(
                OVERWORLD_CAVES,
                PLACEHOLDER.get()
        );
        cxt.register(
                THE_NETHER,
                PLACEHOLDER.get()
        );
        cxt.register(
                THE_END,
                PLACEHOLDER.get()
        );
        cxt.register(
                EARTH_ORBIT,
                new CelestialBodyLevelData(
                        GalacticraftGalaxies.SATELLITE,
                        AtmosphereInfo.builder()
                                .temperatureModifier(-0.9F)
                                .build(),
                        0.165F,
                        FixedPlanetaryTransition.spaceStation(),
                        Optional.of(0.2F)
                )
        );
        cxt.register(
                MOON,
                new CelestialBodyLevelData(
                        GalacticraftGalaxies.MOON,
                        AtmosphereInfo.builder()
                                .temperatureModifier(-0.9F)
                                .build(),
                        0.165F,
                        new LanderPlanetaryTransition(GalacticraftEntityType.LANDER),
                        Optional.of(0.2F)
                )
        );
        cxt.register(
                MARS,
                new CelestialBodyLevelData(
                        GalacticraftGalaxies.MARS,
                        AtmosphereInfo.builder()
                                .gas(Constants.key(Registries.FLUID, "carbon_dioxide"), 0.95)
                                .gas(Constants.key(Registries.FLUID, "nitrogen"), 0.03)
                                .gas(Constants.key(Registries.FLUID, "argon"), 0.02)
                                .temperatureModifier(-0.5F)
                                .build(),
                        0.38F,
                        new LanderPlanetaryTransition(GalacticraftEntityType.LANDING_BALLOONS),
                        Optional.of(0.4F)
                )
        );
        cxt.register(
                ASTEROIDS,
                new CelestialBodyLevelData(
                        GalacticraftGalaxies.ASTEROIDS,
                        AtmosphereInfo.builder()
                                .temperatureModifier(-0.9F)
                                .build(),
                        0.01F,
                        new LanderPlanetaryTransition(GalacticraftEntityType.ENTRY_POD),
                        Optional.of(0.1F)
                )
        );
        cxt.register(
                VENUS,
                new CelestialBodyLevelData(
                        GalacticraftGalaxies.VENUS,
                        AtmosphereInfo.builder()
                                .gas(Constants.key(Registries.FLUID, "carbon_dioxide"), 0.965)
                                .gas(Constants.key(Registries.FLUID, "nitrogen"), 0.035)
                                .temperatureModifier(2.0F)
                                .build(),
                        0.904F,
                        new LanderPlanetaryTransition(GalacticraftEntityType.ENTRY_POD),
                        Optional.of(0.9F)
                )
        );
    }
}
