/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.placement;

import io.kalishak.galacticraftlegacy.world.level.levelgen.features.OverworldFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OverworldPlacements {
    public static final ResourceKey<PlacedFeature> ORE_ALUMINUM_UPPER = GalacticraftPlacements.key("ore_aluminum_upper");
    public static final ResourceKey<PlacedFeature> ORE_ALUMINUM_LOWER = GalacticraftPlacements.key("ore_aluminum_lower");
    public static final ResourceKey<PlacedFeature> ORE_TIN_UPPER = GalacticraftPlacements.key("ore_tin_upper");
    public static final ResourceKey<PlacedFeature> ORE_TIN_MIDDLE = GalacticraftPlacements.key("ore_tin_middle");
    public static final ResourceKey<PlacedFeature> ORE_TIN_LOWER = GalacticraftPlacements.key("ore_tin_lower");
    public static final ResourceKey<PlacedFeature> ORE_SILICON = GalacticraftPlacements.key("ore_silicon");
    public static final ResourceKey<PlacedFeature> ORE_SILICON_EXTRA = GalacticraftPlacements.key("ore_silicon_extra");

    public static final ResourceKey<PlacedFeature> CRUDE_OIL_FOUNTAIN = GalacticraftPlacements.key("crude_oil_fountain");
    public static final ResourceKey<PlacedFeature> CRUDE_OIL_FOUNTAIN_SMALL = GalacticraftPlacements.key("crude_oil_small_fountain");
    public static final ResourceKey<PlacedFeature> ORE_CRUDE_OIL = GalacticraftPlacements.key("ore_crude_oil");
    public static final ResourceKey<PlacedFeature> ORE_CRUDE_OIL_MAJOR = GalacticraftPlacements.key("ore_crude_oil_major");

    static void bootstrap(BootstrapContext<PlacedFeature> cxt, HolderGetter<ConfiguredFeature<?, ?>> featureGetter) {
        Holder<ConfiguredFeature<?, ?>> aluminumOre = featureGetter.getOrThrow(OverworldFeatures.ORE_ALUMINUM);
        Holder<ConfiguredFeature<?, ?>> aluminumOreSmall = featureGetter.getOrThrow(OverworldFeatures.ORE_ALUMINUM_SMALL);
        Holder<ConfiguredFeature<?, ?>> tinOre = featureGetter.getOrThrow(OverworldFeatures.ORE_TIN);
        Holder<ConfiguredFeature<?, ?>> tinOreSmall = featureGetter.getOrThrow(OverworldFeatures.ORE_TIN_SMALL);
        Holder<ConfiguredFeature<?, ?>> siliconOre = featureGetter.getOrThrow(OverworldFeatures.ORE_SILICON);
        Holder<ConfiguredFeature<?, ?>> siliconOreBuried = featureGetter.getOrThrow(OverworldFeatures.ORE_SILICON_BURIED);

        Holder<ConfiguredFeature<?, ?>> crudeOilFountain = featureGetter.getOrThrow(OverworldFeatures.CRUDE_OIL_FOUNTAIN);
        Holder<ConfiguredFeature<?, ?>> crudeOilFountainSmall = featureGetter.getOrThrow(OverworldFeatures.CRUDE_OIL_FOUNTAIN_SMALL);
        Holder<ConfiguredFeature<?, ?>> crudeOil = featureGetter.getOrThrow(OverworldFeatures.ORE_CRUDE_OIL);
        Holder<ConfiguredFeature<?, ?>> crudeOilMajor = featureGetter.getOrThrow(OverworldFeatures.ORE_CRUDE_OIL_MAJOR);

        PlacementUtils.register(
                cxt,
                ORE_ALUMINUM_UPPER,
                aluminumOre,
                GalacticraftPlacements.commonOrePlacement(
                        40,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(80),
                                VerticalAnchor.absolute(225)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_ALUMINUM_LOWER,
                aluminumOreSmall,
                GalacticraftPlacements.commonOrePlacement(
                        10,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-20),
                                VerticalAnchor.absolute(68)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_TIN_UPPER,
                tinOre,
                GalacticraftPlacements.commonOrePlacement(
                        16,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(100),
                                VerticalAnchor.absolute(134)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_TIN_MIDDLE,
                tinOreSmall,
                GalacticraftPlacements.commonOrePlacement(
                        12,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(30),
                                VerticalAnchor.absolute(120)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_TIN_LOWER,
                tinOreSmall,
                GalacticraftPlacements.commonOrePlacement(
                        20,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-16),
                                VerticalAnchor.absolute(90)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_SILICON,
                siliconOre,
                GalacticraftPlacements.commonOrePlacement(
                        24,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(90)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_SILICON_EXTRA,
                siliconOreBuried,
                GalacticraftPlacements.commonOrePlacement(
                        12,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(5)
                        )
                )
        );

        PlacementUtils.register(
                cxt,
                CRUDE_OIL_FOUNTAIN,
                crudeOilFountain,
                crudeOil(40, HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR))
        );
        PlacementUtils.register(
                cxt,
                CRUDE_OIL_FOUNTAIN_SMALL,
                crudeOilFountainSmall,
                crudeOil(60, HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR))
        );
        PlacementUtils.register(
                cxt,
                ORE_CRUDE_OIL,
                crudeOil,
                crudeOil(60, HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING))
        );
        PlacementUtils.register(
                cxt,
                ORE_CRUDE_OIL_MAJOR,
                crudeOilMajor,
                crudeOil(70, HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING))
        );
    }

    private static List<PlacementModifier> crudeOil(int chance, HeightmapPlacement heightmapPlacement) {
        return List.of(
                CountPlacement.of(1),
                RarityFilter.onAverageOnceEvery(chance),
                heightmapPlacement,
                BiomeFilter.biome()
        );
    }
}
