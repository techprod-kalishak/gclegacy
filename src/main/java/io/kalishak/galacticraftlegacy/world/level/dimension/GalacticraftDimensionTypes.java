package io.kalishak.galacticraftlegacy.world.level.dimension;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;

public class GalacticraftDimensionTypes {
    public static final ResourceKey<DimensionType> OVERWORLD_ORBIT = Constants.key(Registries.DIMENSION_TYPE, "overworld");
    public static final ResourceKey<DimensionType> MOON = Constants.key(Registries.DIMENSION_TYPE, "moon");
    public static final ResourceKey<DimensionType> MARS = Constants.key(Registries.DIMENSION_TYPE, "mars");
    public static final ResourceKey<DimensionType> ASTEROIDS = Constants.key(Registries.DIMENSION_TYPE, "asteroids");
    public static final ResourceKey<DimensionType> VENUS = Constants.key(Registries.DIMENSION_TYPE, "venus");

    public static void bootstrap(BootstrapContext<DimensionType> cxt) {
        HolderGetter<Timeline> timelineHolderGetter = cxt.lookup(Registries.TIMELINE);
        cxt.register(
                OVERWORLD_ORBIT,
                new DimensionType(
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
                        DimensionType.CardinalLightType.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_ORBIT)
                )
        );
        cxt.register(
                MOON,
                new DimensionType(
                        false,
                        false,
                        false,
                        1.0,
                        0,
                        256,
                        128,
                        GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(5), 15),
                        DimensionType.Skybox.END,
                        DimensionType.CardinalLightType.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_MOON)
                )
        );
        cxt.register(
                MARS,
                new DimensionType(
                        false,
                        true,
                        false,
                        1.0,
                        0,
                        256,
                        128,
                        GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(6), 15),
                        DimensionType.Skybox.OVERWORLD,
                        DimensionType.CardinalLightType.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_MARS)
                )
        );
        cxt.register(
                ASTEROIDS,
                new DimensionType(
                        false,
                        true,
                        false,
                        1.0,
                        0,
                        256,
                        128,
                        GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.ZERO, 0),
                        DimensionType.Skybox.END,
                        DimensionType.CardinalLightType.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_ASTEROIDS)
                )
        );
        cxt.register(
                VENUS,
                new DimensionType(
                        false,
                        true,
                        false,
                        1.0,
                        0,
                        256,
                        128,
                        GalacticraftTags.Blocks.INFINIBURN_VENUS,
                        0.0F,
                        new DimensionType.MonsterSettings(ConstantInt.of(6), 15),
                        DimensionType.Skybox.OVERWORLD,
                        DimensionType.CardinalLightType.DEFAULT,
                        EnvironmentAttributeMap.builder()
                                .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
                                .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                                .build(),
                        timelineHolderGetter.getOrThrow(GalacticraftTags.Timelines.IN_VENUS)
                )
        );
    }
}
