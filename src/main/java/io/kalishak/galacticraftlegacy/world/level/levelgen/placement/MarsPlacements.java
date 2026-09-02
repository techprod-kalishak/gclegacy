/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.placement;

import io.kalishak.galacticraftlegacy.world.level.levelgen.features.MarsFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class MarsPlacements {
    public static final ResourceKey<PlacedFeature> COPPER_ORE = GalacticraftPlacements.key("mars_copper_ore");
    public static final ResourceKey<PlacedFeature> TIN_ORE = GalacticraftPlacements.key("mars_tin_ore");
    public static final ResourceKey<PlacedFeature> DESH_ORE = GalacticraftPlacements.key("mars_desh_ore");
    public static final ResourceKey<PlacedFeature> IRON_ORE = GalacticraftPlacements.key("mars_iron_ore");
    public static final ResourceKey<PlacedFeature> PACKED_ICE_PATCH = GalacticraftPlacements.key("mars_packed_ice_patch");
    public static final ResourceKey<PlacedFeature> DIRT_PATCH = GalacticraftPlacements.key("mars_dirt_patch");

    static void bootstrap(BootstrapContext<PlacedFeature> cxt, HolderGetter<ConfiguredFeature<?, ?>> featureGetter) {
        Holder<ConfiguredFeature<?, ?>> copperOre = featureGetter.getOrThrow(MarsFeatures.COPPER_ORE);
        Holder<ConfiguredFeature<?, ?>> tinOre = featureGetter.getOrThrow(MarsFeatures.TIN_ORE);
        Holder<ConfiguredFeature<?, ?>> deshOre = featureGetter.getOrThrow(MarsFeatures.DESH_ORE);
        Holder<ConfiguredFeature<?, ?>> ironOre = featureGetter.getOrThrow(MarsFeatures.IRON_ORE);
        Holder<ConfiguredFeature<?, ?>> packedIce = featureGetter.getOrThrow(MarsFeatures.PACKED_ICE_PATCH);
        Holder<ConfiguredFeature<?, ?>> dirt = featureGetter.getOrThrow(MarsFeatures.DIRT_PATCH);

        PlacementUtils.register(
                cxt,
                COPPER_ORE,
                copperOre,
                GalacticraftPlacements.commonOrePlacement(
                        26,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.belowTop(60)
                        )
                )
        );

        PlacementUtils.register(
                cxt,
                TIN_ORE,
                tinOre,
                GalacticraftPlacements.commonOrePlacement(
                        23,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.belowTop(60)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                DESH_ORE,
                deshOre,
                GalacticraftPlacements.commonOrePlacement(
                        15,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.aboveBottom(20),
                                VerticalAnchor.belowTop(64)
                        )
                )
        );
        PlacementUtils.register(
                cxt,
                IRON_ORE,
                ironOre,
                GalacticraftPlacements.commonOrePlacement(
                        20,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.belowTop(64)
                        )
                )

        );
        PlacementUtils.register(
                cxt,
                PACKED_ICE_PATCH,
                packedIce,
                List.of(
                        RarityFilter.onAverageOnceEvery(4),
                        BiomeFilter.biome(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(60), VerticalAnchor.absolute(60))
                )
        );
        PlacementUtils.register(
                cxt,
                DIRT_PATCH,
                dirt,
                List.of(
                        RarityFilter.onAverageOnceEvery(20),
                        BiomeFilter.biome(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(200))
                )
        );
    }
}
