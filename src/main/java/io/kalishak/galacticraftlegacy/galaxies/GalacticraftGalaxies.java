/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBody;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialObjectType;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredGalaxyRegister;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensionTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.UnaryOperator;

public class GalacticraftGalaxies {
    private static final DeferredGalaxyRegister REGISTRY = DeferredGalaxyRegister.createGalaxy(Galacticraft.MODID);

    public static final DeferredHolder<CelestialBody, CelestialBody> SUN = REGISTRY.registerCelestialBody(
            "sun",
            CelestialObjectType.STAR,
            UnaryOperator.identity()
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> MERCURY = REGISTRY.registerCelestialBody(
            "mercury",
            CelestialObjectType.PLANET,
                    builder -> builder
                            .phaseShift(1.45F)
                            .relativeDistanceFromCenter(0.5F)
                            .relativeOrbitTime(0.24096385542168674698795180722892F)
                            .parent(SUN::value)
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> VENUS = REGISTRY.registerCelestialBody(
            "venus",
            CelestialObjectType.PLANET,
            builder -> builder
                    .phaseShift(2.0F)
                    .relativeDistanceFromCenter(0.75F)
                    .relativeOrbitTime(0.61527929901423877327491785323111F)
                    .atmosphereInfo(atmosphere -> atmosphere
                            .corrosive()
                            .rainy()
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 96.5)
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 3.5)

                    )
                    .transferableTo(GalacticraftDimensionTypes.VENUS, FeatureTier.TIER_3)
                    .parent(SUN::value)
                    .ringColors(0.1F, 0.9F, 0.6F)
                    //.biome(GalacticraftTags.Biomes.IS_VENUS)
                    .checklist(GalacticraftTags.Checklist.VENUS_CHECKLIST)
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> OVERWORLD = REGISTRY.registerCelestialBody(
            "overworld",
            CelestialObjectType.PLANET,
            builder -> builder
                    .atmosphereInfo(atmosphere -> atmosphere
                            .rainy()
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 78.08F)
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("oxygen")), 20.95F)
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("argon")), 0.93F)
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 0.04F)
                    )
                    .parent(SUN::value)
                    //.biome(BiomeTags.IS_OVERWORLD)
                    .checklist(GalacticraftTags.Checklist.OVERWORLD_CHECKLIST)
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> MARS = REGISTRY.registerCelestialBody(
            "mars",
            CelestialObjectType.PLANET,
            builder -> builder
                    .relativeSize(0.5319F)
                    .phaseShift(0.1667F)
                    .relativeDistanceFromCenter(1.25F)
                    .relativeOrbitTime(1.8811610076670317634173055859803F)
                    .atmosphereInfo(atmosphere -> atmosphere
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 95.3F)
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("argon")), 2.7F)
                            .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 2.0F)
                    )
                    .transferableTo(GalacticraftDimensionTypes.MARS, FeatureTier.TIER_2)
                    .parent(SUN::value)
                    .ringColors(0.67F, 0.1F, 0.1F)
                    //.biome(GalacticraftTags.Biomes.IS_MARS)
                    .checklist(GalacticraftTags.Checklist.MARS_CHECKLIST)
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> JUPITER = REGISTRY.registerCelestialBody(
            "jupiter",
            CelestialObjectType.PLANET,
            builder -> builder
                    .phaseShift((float) Math.PI)
                    .relativeDistanceFromCenter(1.5F)
                    .relativeOrbitTime(11.861993428258488499452354874042F)
                    .parent(SUN::value)
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> SATURN = REGISTRY.registerCelestialBody(
            "saturn",
            CelestialObjectType.PLANET,
            builder -> builder
                    .phaseShift(5.45F)
                    .relativeDistanceFromCenter(1.75F)
                    .relativeOrbitTime(29.463307776560788608981380065717F)
                    .parent(SUN::value)
                    .withRings(Constants.texture("gui/celestial_body/saturn_rings"))
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> URANUS = REGISTRY.registerCelestialBody(
            "uranus",
            CelestialObjectType.PLANET,
            builder -> builder
                    .phaseShift(1.38F)
                    .relativeDistanceFromCenter(2.0F)
                    .relativeOrbitTime(84.063526834611171960569550930997F)
                    .parent(SUN::value)
                    .withRings(Constants.texture("gui/celestial_body/uranus_rings"))
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> NEPTUNE = REGISTRY.registerCelestialBody(
            "neptune",
            CelestialObjectType.PLANET,
            builder -> builder
                    .phaseShift(1.0F)
                    .relativeDistanceFromCenter(2.25F)
                    .relativeOrbitTime(164.84118291347207009857612267251F)
                    .parent(SUN::value)
                    .withRings(Constants.texture("gui/celestial_body/neptune_rings"))
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> MOON = REGISTRY.registerCelestialBody(
            "moon",
            CelestialObjectType.MOON,
            builder -> builder
                    .relativeSize(0.2667F)
                    .relativeDistanceFromCenter(13.0F)
                    .relativeOrbitTime(1.0F / 0.1F)
                    .parent(OVERWORLD::value)
                    .transferableTo(GalacticraftDimensionTypes.MOON, FeatureTier.TIER_1)
                    //.biome(GalacticraftTags.Biomes.IS_MOON)
                    .checklist(GalacticraftTags.Checklist.MOON_CHECKLIST)
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> SATELLITE = REGISTRY.registerCelestialBody(
            "satellite",
            CelestialObjectType.SATELLITE,
            builder -> builder
                    .relativeSize(0.2667F)
                    .relativeDistanceFromCenter(9.0F)
                    .relativeOrbitTime(1.0F / 0.05F)
                    .parent(OVERWORLD::value)
                    //.biome(GalacticraftTags.Biomes.IS_OPEN_SPACE)
                    //.transferableTo(GalacticraftDimensionTypes.SATTELITE, FeatureTier.TIER_1)
                    .checklist(GalacticraftTags.Checklist.SATELLITE_CHECKLIST)
    );
    public static final DeferredHolder<CelestialBody, CelestialBody> ASTEROIDS = REGISTRY.registerCelestialBody(
            "asteroids",
            CelestialObjectType.PLANET,
            builder -> builder
                    .phaseShift((float) (Math.random() * (2 * Math.PI)))
                    .relativeDistanceFromCenter(1.375F)
                    .relativeOrbitTime(45.0F)
                    .parent(SUN::value)
                    //.biome(GalacticraftTags.Biomes.IS_ASTEROIDS)
                    .transferableTo(GalacticraftDimensionTypes.ASTEROIDS, FeatureTier.TIER_3)
                    .checklist(GalacticraftTags.Checklist.ASTEROIDS_CHECKLIST)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
