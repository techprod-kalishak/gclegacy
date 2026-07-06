/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.levelgen.placement.OverworldPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class GalacticraftBiomeModifiers {
    public static final ResourceKey<BiomeModifier> OVERWORLD_ORES = Constants.key(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "overworld_biomes");
    public static final ResourceKey<BiomeModifier> OCEAN_OIL_POOLS = Constants.key(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "ocean_oil_pools");
    public static final ResourceKey<BiomeModifier> UNDERGROUND_OIL_POOLS = Constants.key(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "undergound_oil_pools");

    public static void bootstrap(BootstrapContext<BiomeModifier> cxt) {
        HolderGetter<Biome> biomeGetter = cxt.lookup(Registries.BIOME);

        cxt.register(
                OVERWORLD_ORES,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                        placements(cxt, List.of(
                                OverworldPlacements.ORE_ALUMINUM_UPPER,
                                OverworldPlacements.ORE_ALUMINUM_LOWER,
                                OverworldPlacements.ORE_TIN_UPPER,
                                OverworldPlacements.ORE_TIN_MIDDLE,
                                OverworldPlacements.ORE_TIN_LOWER,
                                OverworldPlacements.ORE_SILICON,
                                OverworldPlacements.ORE_SILICON_EXTRA)
                        ),
                        GenerationStep.Decoration.UNDERGROUND_DECORATION
                )
        );
        cxt.register(
                OCEAN_OIL_POOLS,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomeGetter.getOrThrow(BiomeTags.IS_OCEAN),
                        placements(
                                cxt,
                                List.of(
                                        OverworldPlacements.CRUDE_OIL_FOUNTAIN,
                                        OverworldPlacements.CRUDE_OIL_FOUNTAIN_SMALL
                                )
                        ),
                        GenerationStep.Decoration.UNDERGROUND_DECORATION
                )
        );
        cxt.register(
                UNDERGROUND_OIL_POOLS,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        holdersFromKeys(cxt, Registries.BIOME, List.of(
                                Biomes.DESERT,
                                Biomes.SAVANNA,
                                Biomes.SAVANNA_PLATEAU,
                                Biomes.WINDSWEPT_SAVANNA,
                                Biomes.JUNGLE,
                                Biomes.ERODED_BADLANDS
                        )),
                        placements(
                                cxt,
                                List.of(
                                        OverworldPlacements.ORE_CRUDE_OIL,
                                        OverworldPlacements.ORE_CRUDE_OIL_MAJOR
                                )
                        ),
                        GenerationStep.Decoration.UNDERGROUND_DECORATION
                )
        );
    }

    private static HolderSet<PlacedFeature> placements(BootstrapContext<BiomeModifier> cxt, List<ResourceKey<PlacedFeature>> placementKeys) {
        return HolderSet.direct(
                key -> cxt.lookup(Registries.PLACED_FEATURE).getOrThrow(key),
                placementKeys
        );
    }

    private static <R> HolderSet<R> holdersFromKeys(BootstrapContext<BiomeModifier> cxt, ResourceKey<? extends Registry<R>> registry, List<ResourceKey<R>> placementKeys) {
        return HolderSet.direct(
                key -> cxt.lookup(registry).getOrThrow(key),
                placementKeys
        );
    }
}
