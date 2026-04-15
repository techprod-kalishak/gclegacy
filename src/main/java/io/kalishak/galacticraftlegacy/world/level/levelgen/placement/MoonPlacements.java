/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.placement;

import io.kalishak.galacticraftlegacy.world.level.levelgen.features.MoonFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class MoonPlacements {
    public static final ResourceKey<PlacedFeature> MOON_CRATER = GalacticraftPlacements.key("moon_crater");
    public static final ResourceKey<PlacedFeature> MOON_CRATER_LARGE = GalacticraftPlacements.key("moon_crater_large");
    public static final ResourceKey<PlacedFeature> ORE_COPPER_UPPER = GalacticraftPlacements.key("ore_moon_copper_upper");
    public static final ResourceKey<PlacedFeature> ORE_COPPER_LOWER = GalacticraftPlacements.key("ore_moon_copper_lower");
    public static final ResourceKey<PlacedFeature> ORE_TIN_UPPER = GalacticraftPlacements.key("ore_moon_tin_upper");
    public static final ResourceKey<PlacedFeature> ORE_TIN_MIDDLE = GalacticraftPlacements.key("ore_moon_tin_middle");
    public static final ResourceKey<PlacedFeature> ORE_TIN_LOWER = GalacticraftPlacements.key("ore_moon_tin_lower");
    public static final ResourceKey<PlacedFeature> ORE_CHEESE = GalacticraftPlacements.key("ore_moon_cheese");
    public static final ResourceKey<PlacedFeature> ORE_SAPPHIRE = GalacticraftPlacements.key("ore_moon_sapphire");

    static void bootstrap(BootstrapContext<PlacedFeature> cxt, HolderGetter<ConfiguredFeature<?, ?>> featureGetter) {
        Holder<ConfiguredFeature<?, ?>> crater = featureGetter.getOrThrow(MoonFeatures.MOON_CRATER);
        Holder<ConfiguredFeature<?, ?>> craterLarge = featureGetter.getOrThrow(MoonFeatures.MOON_CRATER_LARGE);
        Holder<ConfiguredFeature<?, ?>> copperOre = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_COPPER);
        Holder<ConfiguredFeature<?, ?>> copperOreSmall = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_COPPER_SMALL);
        Holder<ConfiguredFeature<?, ?>> tinOre = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_TIN);
        Holder<ConfiguredFeature<?, ?>> cheeseOre = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_CHEESE);
        Holder<ConfiguredFeature<?, ?>> sapphireOre = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_SAPPHIRE);

        PlacementUtils.register(
                cxt,
                MOON_CRATER,
                crater,
                List.of(
                        RarityFilter.onAverageOnceEvery(24),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP
                )
        );
        PlacementUtils.register(
                cxt,
                MOON_CRATER_LARGE,
                craterLarge,
                List.of(
                        RarityFilter.onAverageOnceEvery(4),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP
                )
        );

        PlacementUtils.register(
                cxt,
                ORE_COPPER_UPPER,
                copperOre,
                GalacticraftPlacements.commonOrePlacement(
                        20,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(80),
                                VerticalAnchor.absolute(120)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_COPPER_LOWER,
                copperOreSmall,
                GalacticraftPlacements.commonOrePlacement(
                        10,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(10),
                                VerticalAnchor.absolute(70)
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
                                VerticalAnchor.absolute(80),
                                VerticalAnchor.absolute(120)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_TIN_MIDDLE,
                tinOre,
                GalacticraftPlacements.commonOrePlacement(
                        8,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(30),
                                VerticalAnchor.absolute(90)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_TIN_LOWER,
                tinOre,
                GalacticraftPlacements.commonOrePlacement(
                        4,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(10),
                                VerticalAnchor.absolute(50)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_CHEESE,
                cheeseOre,
                GalacticraftPlacements.commonOrePlacement(
                        10,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(120)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_SAPPHIRE,
                sapphireOre,
                GalacticraftPlacements.commonOrePlacement(
                        10,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.aboveBottom(10),
                                VerticalAnchor.absolute(90)
                        )
                )
        );
    }
}
