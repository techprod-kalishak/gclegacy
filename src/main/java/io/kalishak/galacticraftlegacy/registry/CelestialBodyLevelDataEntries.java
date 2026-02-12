package io.kalishak.galacticraftlegacy.registry;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.space.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.space.environment.AtmosphereInfoImpl;
import io.kalishak.galacticraftlegacy.space.environment.CelestialBodyTransition;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.phys.Vec3;

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
            1.0F,
            AtmosphereInfo.EARTH,
            1.0F,
            new CelestialBodyTransition(
                    Vec3.ZERO,
                    0,
                    CelestialBodyTransition.Type.PARACHUTE
            )
    );


    public static void bootstrap(BootstrapContext<CelestialBodyLevelData> cxt) {
        cxt.register(
                OVERWORLD,
                new CelestialBodyLevelData(
                        0.0F,
                        AtmosphereInfo.EARTH,
                        1.0F,
                        new CelestialBodyTransition(
                                Vec3.ZERO,
                                256,
                                CelestialBodyTransition.Type.PARACHUTE
                        )
                )
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
                PLACEHOLDER.get()
        );
        cxt.register(
                MOON,
                new CelestialBodyLevelData(
                        0.1F,
                        new AtmosphereInfoImpl.Builder()
                                .temperature(140.0F)
                                .build(),
                        0.165F,
                        new CelestialBodyTransition(
                                Vec3.ZERO,
                                256,
                                CelestialBodyTransition.Type.LANDER
                        )
                )
        );
        cxt.register(
                MARS,
                new CelestialBodyLevelData(
                        0.38F,
                        new AtmosphereInfoImpl.Builder()
                                .gas(Constants.key(Registries.FLUID, "carbon_dioxide"), 0.95)
                                .gas(Constants.key(Registries.FLUID, "nitrogen"), 0.03)
                                .gas(Constants.key(Registries.FLUID, "argon"), 0.02)
                                .temperature(-60.0F)
                                .build(),
                        0.38F,
                        new CelestialBodyTransition(
                                Vec3.ZERO,
                                256,
                                CelestialBodyTransition.Type.BUBBLE_LANDER
                        )
                )
        );
        cxt.register(
                ASTEROIDS,
                new CelestialBodyLevelData(
                        0.0F,
                        new AtmosphereInfoImpl.Builder()
                                .temperature(2.7F)
                                .build(),
                        0.01F,
                        new CelestialBodyTransition(
                                Vec3.ZERO,
                                256,
                                CelestialBodyTransition.Type.CAPSULE
                        )
                )
        );
        cxt.register(
                VENUS,
                new  CelestialBodyLevelData(
                        1.0F,
                        new AtmosphereInfoImpl.Builder()
                                .gas(Constants.key(Registries.FLUID, "carbon_dioxide"), 0.965)
                                .gas(Constants.key(Registries.FLUID, "nitrogen"), 0.035)
                                .temperature(462.0F)
                                .build(),
                        0.904F,
                        new CelestialBodyTransition(
                                Vec3.ZERO,
                                256,
                                CelestialBodyTransition.Type.CAPSULE
                        )
                )
        );
    }
}
