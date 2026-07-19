/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension;

import io.kalishak.galacticraftlegacy.client.renderer.environment.*;
import io.kalishak.galacticraftlegacy.client.renderer.environment.sky.MoonSkyRenderer;
import io.kalishak.galacticraftlegacy.client.renderer.environment.sky.OrbitalSkyRenderer;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftWorldAttributes;
import io.kalishak.galacticraftlegacy.world.timeline.GalacticraftWorldClocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;
import net.neoforged.neoforge.common.extensions.IHolderExtension;
import net.neoforged.neoforge.common.world.NeoForgeEnvironmentAttributes;

import java.util.Optional;

public class GalacticraftDimensionTypes {
    public static final ResourceKey<DimensionType> OVERWORLD_ORBIT = Constants.key(Registries.DIMENSION_TYPE, "overworld_orbit");
    public static final ResourceKey<DimensionType> MOON = Constants.key(Registries.DIMENSION_TYPE, "moon");
    public static final ResourceKey<DimensionType> MARS = Constants.key(Registries.DIMENSION_TYPE, "mars");
    public static final ResourceKey<DimensionType> ASTEROIDS = Constants.key(Registries.DIMENSION_TYPE, "asteroids");
    public static final ResourceKey<DimensionType> VENUS = Constants.key(Registries.DIMENSION_TYPE, "venus");

    public static void bootstrap(BootstrapContext<DimensionType> cxt) {
        HolderGetter<Timeline> timelineHolderGetter = cxt.lookup(Registries.TIMELINE);
        HolderGetter<WorldClock> worldClocks = cxt.lookup(Registries.WORLD_CLOCK);
        HolderGetter<Block> blocks = cxt.lookup(Registries.BLOCK);
        HolderSet<Block> infiniburnOpenSpace = blocks.getOrThrow(GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE);
        cxt.register(
                OVERWORLD_ORBIT,
                new DimensionType(
                        false,
                        false,
                        false,
                        false,
                        1.0,
                        0,
                        256,
                        128,
                        infiniburnOpenSpace,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.ZERO, 0),
                        DimensionType.Skybox.OVERWORLD,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(NeoForgeEnvironmentAttributes.CUSTOM_SKYBOX, OrbitalSkyRenderer.ID)
                                .set(NeoForgeEnvironmentAttributes.CUSTOM_CLOUDS, DummyCloudsRenderer.ID)
                                .set(NeoForgeEnvironmentAttributes.CUSTOM_WEATHER_EFFECTS, DummyWeatherRenderer.ID)
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .set(GalacticraftEnvironmentAttributes.GRAVITY.get(), 0.01F)
                                .set(GalacticraftEnvironmentAttributes.FALL_DAMAGE_MODIFIER.get(), 0.0F)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_ORBIT),
                        Optional.empty()
                )
        );
        cxt.register(
                MOON,
                new DimensionType(
                        false,
                        false,
                        false,
                        false,
                        1.0,
                        -64,
                        256,
                        64,
                        infiniburnOpenSpace,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(5), 15),
                        DimensionType.Skybox.NONE,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(NeoForgeEnvironmentAttributes.CUSTOM_SKYBOX, MoonSkyRenderer.ID)
                                .set(NeoForgeEnvironmentAttributes.CUSTOM_CLOUDS, DummyCloudsRenderer.ID)
                                .set(NeoForgeEnvironmentAttributes.CUSTOM_WEATHER_EFFECTS, DummyWeatherRenderer.ID)
                                .set(EnvironmentAttributes.SKY_COLOR, 0)
                                .set(EnvironmentAttributes.BACKGROUND_MUSIC, GalacticraftWorldAttributes.MUSIC_SPACE)
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.AMBIENT_SOUNDS, GalacticraftWorldAttributes.AMBIENT_SOUNDS_SPACE)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .set(GalacticraftEnvironmentAttributes.GRAVITY.get(), 0.013F)
                                .set(GalacticraftEnvironmentAttributes.FALL_DAMAGE_MODIFIER.get(), 0.16F)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_MOON),
                        worldClocks.get(GalacticraftWorldClocks.MOON).map(IHolderExtension::getDelegate)
                )
        );
        cxt.register(
                MARS,
                new DimensionType(
                        false,
                        true,
                        false,
                        false,
                        1.0,
                        0,
                        256,
                        128,
                        infiniburnOpenSpace,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(6), 15),
                        DimensionType.Skybox.NONE,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .set(GalacticraftEnvironmentAttributes.GRAVITY.get(), 0.04F)
                                .set(GalacticraftEnvironmentAttributes.FALL_DAMAGE_MODIFIER.get(), 0.5F)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_MARS),
                        worldClocks.get(GalacticraftWorldClocks.MARS).map(IHolderExtension::getDelegate)
                )
        );
        cxt.register(
                ASTEROIDS,
                new DimensionType(
                        false,
                        true,
                        false,
                        false,
                        1.0,
                        0,
                        256,
                        128,
                        infiniburnOpenSpace,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.ZERO, 0),
                        DimensionType.Skybox.NONE,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .set(GalacticraftEnvironmentAttributes.GRAVITY.get(), 0.01F)
                                .set(GalacticraftEnvironmentAttributes.FALL_DAMAGE_MODIFIER.get(), 0.16F)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_ASTEROIDS),
                        worldClocks.get(GalacticraftWorldClocks.ASTEROIDS).map(IHolderExtension::getDelegate)
                )
        );
        cxt.register(
                VENUS,
                new DimensionType(
                        false,
                        true,
                        false,
                        false,
                        1.0,
                        0,
                        256,
                        128,
                        blocks.getOrThrow(GalacticraftTags.Blocks.INFINIBURN_VENUS),
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(6), 15),
                        DimensionType.Skybox.NONE,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .set(GalacticraftEnvironmentAttributes.GRAVITY.get(), 0.065F)
                                .set(GalacticraftEnvironmentAttributes.FALL_DAMAGE_MODIFIER.get(), 0.81F)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_VENUS),
                        worldClocks.get(GalacticraftWorldClocks.VENUS).map(IHolderExtension::getDelegate)
                )
        );
    }
}
