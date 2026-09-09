/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.Optional;
import java.util.function.BiConsumer;

public class CelestialBodyInfoEntries {
    public static void register(BiConsumer<ResourceKey<DimensionType>, CelestialBodyInfo> consumer) {
        CelestialBodyInfo inaccessible = new CelestialBodyInfo(
                GalacticraftGalaxies.SOL,
                AtmosphereInfo.builder().build(),
                1.0F,
                InaccessiblePlanetaryTransition.INSTANCE,
                Optional.empty()
        );

        consumer.accept(BuiltinDimensionTypes.OVERWORLD, new CelestialBodyInfo(
                GalacticraftGalaxies.OVERWORLD,
                AtmosphereInfo.EARTH,
                1.0F,
                EarthPlanetaryTranstion.INSTANCE,
                Optional.empty()
        ));
        consumer.accept(BuiltinDimensionTypes.OVERWORLD_CAVES, inaccessible);
        consumer.accept(BuiltinDimensionTypes.END, inaccessible);
        consumer.accept(BuiltinDimensionTypes.NETHER, inaccessible);
        consumer.accept(
                GalacticraftDimensionTypes.OVERWORLD_ORBIT,
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
        consumer.accept(
                GalacticraftDimensionTypes.MOON,
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
        consumer.accept(
                GalacticraftDimensionTypes.MARS,
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
        consumer.accept(
                GalacticraftDimensionTypes.ASTEROIDS,
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
        consumer.accept(
                GalacticraftDimensionTypes.VENUS,
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
