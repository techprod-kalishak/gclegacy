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
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class MoonPlacements {
    public static final ResourceKey<PlacedFeature> FALLEN_METEOR = GalacticraftPlacements.key("moon_fallen_meteor");
    public static final ResourceKey<PlacedFeature> MOON_CRATER = GalacticraftPlacements.key("moon_crater");
    public static final ResourceKey<PlacedFeature> MOON_CRATER_LARGE = GalacticraftPlacements.key("moon_crater_large");
    public static final ResourceKey<PlacedFeature> ORE_COPPER = GalacticraftPlacements.key("moon_copper_ore");
    public static final ResourceKey<PlacedFeature> ORE_TIN = GalacticraftPlacements.key("moon_tin_ore");
    public static final ResourceKey<PlacedFeature> ORE_CHEESE = GalacticraftPlacements.key("moon_cheese_ore");
    public static final ResourceKey<PlacedFeature> ORE_SAPPHIRE = GalacticraftPlacements.key("moon_sapphire_ore");
    public static final ResourceKey<PlacedFeature> DIRT_PATCH = GalacticraftPlacements.key("moon_dirt_patch");

    static void bootstrap(BootstrapContext<PlacedFeature> cxt, HolderGetter<ConfiguredFeature<?, ?>> featureGetter) {
        Holder<ConfiguredFeature<?, ?>> fallenMeteor = featureGetter.getOrThrow(MoonFeatures.FALLEN_METEOR);
        Holder<ConfiguredFeature<?, ?>> crater = featureGetter.getOrThrow(MoonFeatures.MOON_CRATER);
        Holder<ConfiguredFeature<?, ?>> craterLarge = featureGetter.getOrThrow(MoonFeatures.MOON_CRATER_LARGE);
        Holder<ConfiguredFeature<?, ?>> copperOre = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_COPPER);
        Holder<ConfiguredFeature<?, ?>> tinOre = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_TIN);
        Holder<ConfiguredFeature<?, ?>> cheeseOre = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_CHEESE);
        Holder<ConfiguredFeature<?, ?>> sapphireOre = featureGetter.getOrThrow(MoonFeatures.ORE_MOON_SAPPHIRE);

        PlacementUtils.register(
                cxt,
                FALLEN_METEOR,
                fallenMeteor,
                List.of(
                        RarityFilter.onAverageOnceEvery(8),
                        BiomeFilter.biome(),
                        RandomOffsetPlacement.horizontal(UniformInt.of(4, 16))
                )
        );

        PlacementUtils.register(
                cxt,
                MOON_CRATER,
                crater,
                List.of(
                        RarityFilter.onAverageOnceEvery(24),
                        BiomeFilter.biome(),
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
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP
                )
        );

        PlacementUtils.register(
                cxt,
                ORE_COPPER,
                copperOre,
                GalacticraftPlacements.commonOrePlacement(
                        26,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.aboveBottom(60)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_TIN,
                tinOre,
                GalacticraftPlacements.commonOrePlacement(
                        23,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(60)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_CHEESE,
                cheeseOre,
                GalacticraftPlacements.commonOrePlacement(
                        14,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(85)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                ORE_SAPPHIRE,
                sapphireOre,
                List.of(
                        RarityFilter.onAverageOnceEvery(6),
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP
                )
        );
    }
}
