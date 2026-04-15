/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.stream.Collectors;

public class GalacticraftGalaxies {
    private static final DeferredRegister<CelestialObject> REGISTRY = DeferredRegister.create(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, Galacticraft.MODID);

    public static final DeferredHolder<CelestialObject, CelestialBody> SUN = REGISTRY.register(
            "sun",
            () -> new CelestialBody(
                    CelestialBodyType.STAR,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "star"))
                            .unreachable()
            )
    );
    public static final DeferredHolder<CelestialObject, SolarSystem> SOL = REGISTRY.register(
            "sol",
            () -> new SolarSystem(
                    CelestialBodyType.SOLAR_SYSTEM,
                    Vec3.ZERO,
                    SUN::value,
                    Constants.id("sol"),
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "sol"))
                            .unreachable()
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> MERCURY = REGISTRY.register(
            "mercury",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "mercury"))
                            .phaseShift(1.45F)
                            .relativeDistanceFromCenter(0.5F)
                            .relativeOrbitTime(0.24096385542168674698795180722892F)
                            .unreachable()
                            .parent(SOL::value)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> VENUS = REGISTRY.register(
            "venus",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "venus"))
                            .phaseShift(2.0F)
                            .relativeDistanceFromCenter(0.75F)
                            .relativeOrbitTime(0.61527929901423877327491785323111F)
                            .atmosphereInfo(builder -> builder
                                    .corrosive()
                                    .rainy()
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 96.5)
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 3.5)

                            )
                            .tierRequired(FeatureTier.TIER_3)
                            .parent(SOL::value)
                            .ringColors(0.1F, 0.9F, 0.6F)
                            .icon(Constants.texture("gui/celestialbodies/venus.png"))
                            .biome(GalacticraftTags.Biomes.IS_VENUS)
                            .checklist(GalacticraftTags.Checklist.VENUS_CHECKLIST)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> OVERWORLD = REGISTRY.register(
            "overworld",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "overworld"))
                            .atmosphereInfo(builder -> builder
                                    .rainy()
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 78.08F)
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("oxygen")), 20.95F)
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("argon")), 0.93F)
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 0.04F)
                            )
                            .parent(SOL::value)
                            .icon(Constants.texture("gui/celestialbodies/earth.png"))
                            .biome(BiomeTags.IS_OVERWORLD)
                            .checklist(GalacticraftTags.Checklist.OVERWORLD_CHECKLIST)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> MARS = REGISTRY.register(
            "mars",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "mars"))
                            .relativeSize(0.5319F)
                            .phaseShift(0.1667F)
                            .relativeDistanceFromCenter(1.25F)
                            .relativeOrbitTime(1.8811610076670317634173055859803F)
                            .atmosphereInfo(builder -> builder
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 95.3F)
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("argon")), 2.7F)
                                    .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 2.0F)
                            )
                            .tierRequired(FeatureTier.TIER_2)
                            .parent(SOL::value)
                            .ringColors(0.67F, 0.1F, 0.1F)
                            .icon(Constants.texture("gui/celestialbodies/mars.png"))
                            .biome(GalacticraftTags.Biomes.IS_MARS)
                            .checklist(GalacticraftTags.Checklist.MARS_CHECKLIST)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> JUPITER = REGISTRY.register(
            "jupiter",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "jupiter"))
                            .phaseShift((float) Math.PI)
                            .relativeDistanceFromCenter(1.5F)
                            .relativeOrbitTime(11.861993428258488499452354874042F)
                            .unreachable()
                            .parent(SOL::value)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> SATURN = REGISTRY.register(
            "saturn",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "saturn"))
                            .phaseShift(5.45F)
                            .relativeDistanceFromCenter(1.75F)
                            .relativeOrbitTime(29.463307776560788608981380065717F)
                            .unreachable()
                            .parent(SOL::value)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> URANUS = REGISTRY.register(
            "uranus",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "uranus"))
                            .phaseShift(1.38F)
                            .relativeDistanceFromCenter(2.0F)
                            .relativeOrbitTime(84.063526834611171960569550930997F)
                            .unreachable()
                            .parent(SOL::value)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> NEPTUNE = REGISTRY.register(
            "neptune",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "neptune"))
                            .phaseShift(1.0F)
                            .relativeDistanceFromCenter(2.25F)
                            .relativeOrbitTime(164.84118291347207009857612267251F)
                            .parent(SOL::value)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> MOON = REGISTRY.register(
            "moon",
            () -> new CelestialBody(
                    CelestialBodyType.MOON,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "moon"))
                            .relativeSize(0.2667F)
                            .relativeDistanceFromCenter(13.0F)
                            .relativeOrbitTime(1.0F / 0.1F)
                            .parent(OVERWORLD::value)
                            .icon(Constants.texture("gui/celestialbodies/moon.png"))
                            .biome(GalacticraftTags.Biomes.IS_MOON)
                            .checklist(GalacticraftTags.Checklist.MOON_CHECKLIST)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> SATELLITE = REGISTRY.register(
            "satellite",
            () -> new CelestialBody(
                    CelestialBodyType.SATELLITE,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "satellite"))
                            .relativeSize(0.2667F)
                            .relativeDistanceFromCenter(9.0F)
                            .relativeOrbitTime(1.0F / 0.05F)
                            .parent(OVERWORLD::value)
                            .icon(Constants.texture("gui/celestialbodies/space_station.png"))
                            .biome(GalacticraftTags.Biomes.IS_ORBIT)
                            .checklist(GalacticraftTags.Checklist.SATELLITE_CHECKLIST)
            )
    );
    public static final DeferredHolder<CelestialObject, CelestialBody> ASTEROIDS = REGISTRY.register(
            "asteroids",
            () -> new CelestialBody(
                    CelestialBodyType.PLANET,
                    CelestialBody.Properties.of(Constants.key(GalacticraftRegistries.Keys.CELESTIAL_OBJECT, "asteroids"))
                            .phaseShift((float) (Math.random() * (2 * Math.PI)))
                            .relativeDistanceFromCenter(1.375F)
                            .relativeOrbitTime(45.0F)
                            .parent(SOL::value)
                            .icon(Constants.texture("gui/celestialbodies/asteroids.png"))
                            .biome(GalacticraftTags.Biomes.IS_ASTEROIDS)
                            .checklist(GalacticraftTags.Checklist.ASTEROIDS_CHECKLIST)
            )
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static List<CelestialObject> getRenderables() {
        return REGISTRY.getEntries().stream().filter(DeferredHolder::isBound).map(DeferredHolder::get).collect(Collectors.toUnmodifiableList());
    }
}
