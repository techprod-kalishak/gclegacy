/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import io.kalishak.galacticraftlegacy.data.datamap.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.galaxies.environment.CelestialBodyInfo;
import io.kalishak.galacticraftlegacy.galaxies.GalacticraftGalaxies;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensionTypes;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.EarthPlanetaryTranstion;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.FixedPlanetaryTransition;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.InaccessiblePlanetaryTransition;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.LanderPlanetaryTransition;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Optional;

public class CelestialBodyInfoEntries {
    public static final ResourceKey<CelestialBodyInfo> INACCESSIBLE = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO, "inaccessible");
    public static final ResourceKey<CelestialBodyInfo> OVERWORLD = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO, "overworld");
    public static final ResourceKey<CelestialBodyInfo> MOON = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO, "moon");
    public static final ResourceKey<CelestialBodyInfo> ORBIT = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO, "orbit");
    public static final ResourceKey<CelestialBodyInfo> MARS = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO, "mars");
    public static final ResourceKey<CelestialBodyInfo> ASTEROIDS = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO, "asteroids");
    public static final ResourceKey<CelestialBodyInfo> VENUS = Constants.key(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO, "venus");

    public static void buildDataMaps(DataMapProvider.Builder<CelestialBodyLevelData, DimensionType> dataMapBuilder) {
        CelestialBodyLevelData inaccessible = new CelestialBodyLevelData(INACCESSIBLE);
        dataMapBuilder.add(BuiltinDimensionTypes.OVERWORLD, new CelestialBodyLevelData(OVERWORLD), false)
                .add(BuiltinDimensionTypes.OVERWORLD_CAVES, inaccessible, false)
                .add(BuiltinDimensionTypes.NETHER, inaccessible, false)
                .add(BuiltinDimensionTypes.END, inaccessible, false)
                .add(GalacticraftDimensionTypes.OVERWORLD_ORBIT, new CelestialBodyLevelData(ORBIT), false)
                .add(GalacticraftDimensionTypes.MOON, new CelestialBodyLevelData(MOON), false)
                .add(GalacticraftDimensionTypes.MARS, new CelestialBodyLevelData(MARS), false)
                .add(GalacticraftDimensionTypes.ASTEROIDS, new CelestialBodyLevelData(ASTEROIDS), false)
                .add(GalacticraftDimensionTypes.VENUS, new CelestialBodyLevelData(VENUS), false);
    }
    public static void bootstrap(BootstrapContext<CelestialBodyInfo> cxt) {
        cxt.register(INACCESSIBLE, new CelestialBodyInfo(
                GalacticraftGalaxies.SOL,
                AtmosphereInfo.builder().build(),
                1.0F,
                InaccessiblePlanetaryTransition.INSTANCE,
                Optional.empty()
        ));

        cxt.register(OVERWORLD, new CelestialBodyInfo(
                GalacticraftGalaxies.OVERWORLD,
                AtmosphereInfo.EARTH,
                1.0F,
                EarthPlanetaryTranstion.INSTANCE,
                Optional.empty()
        ));
        cxt.register(
                ORBIT,
                new CelestialBodyInfo(
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
                new CelestialBodyInfo(
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
                new CelestialBodyInfo(
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
                new CelestialBodyInfo(
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
                new CelestialBodyInfo(
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
