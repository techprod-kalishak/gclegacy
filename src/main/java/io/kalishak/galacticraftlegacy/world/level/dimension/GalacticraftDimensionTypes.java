package io.kalishak.galacticraftlegacy.world.level.dimension;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.renderer.environment.MoonSkyRenderer;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftWorldAttributes;
import io.kalishak.galacticraftlegacy.world.level.biome.MoonBiomes;
import io.kalishak.galacticraftlegacy.world.timeline.GalacticraftWorldClocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.CardinalLighting;
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
                        GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.ZERO, 0),
                        DimensionType.Skybox.END,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
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
                        0,
                        128,
                        64,
                        GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(5), 15),
                        DimensionType.Skybox.END,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(NeoForgeEnvironmentAttributes.CUSTOM_SKYBOX, MoonSkyRenderer.ID)
                                .set(EnvironmentAttributes.SKY_COLOR, MoonBiomes.calculateSkyColor())
                                .set(EnvironmentAttributes.BACKGROUND_MUSIC, GalacticraftWorldAttributes.MUSIC_SPACE)
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.AMBIENT_SOUNDS, GalacticraftWorldAttributes.AMBIENT_SOUNDS_SPACE)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
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
                        GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(6), 15),
                        DimensionType.Skybox.OVERWORLD,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
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
                        GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.ZERO, 0),
                        DimensionType.Skybox.END,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
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
                        GalacticraftTags.Blocks.INFINIBURN_VENUS,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(6), 15),
                        DimensionType.Skybox.OVERWORLD,
                        CardinalLighting.Type.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_VENUS),
                        worldClocks.get(GalacticraftWorldClocks.VENUS).map(IHolderExtension::getDelegate)
                )
        );
    }
}
