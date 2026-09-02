/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.biome;

import io.kalishak.galacticraftlegacy.data.worldgen.MoonCarvers;
import io.kalishak.galacticraftlegacy.sounds.GalacticraftSounds;
import io.kalishak.galacticraftlegacy.world.level.levelgen.placement.MoonPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class MoonBiomes {
    public static final ResourceKey<Biome> MOON_PLAINS = GalacticraftBiomes.key("moon_plains");

    static void bootstrap(BootstrapContext<Biome> cxt, HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        cxt.register(MOON_PLAINS, moonPlains(placedFeatures, worldCarvers));
    }

    static Biome moonPlains(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobSpawnSettings = new MobSpawnSettings.Builder();
        GalacticraftBiomes.evolvedMonsters(mobSpawnSettings, 20, 15);
        BiomeGenerationSettings.Builder biomeGenerationSettings = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MoonPlacements.MOON_CRATER);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MoonPlacements.MOON_CRATER_LARGE);

        biomeGenerationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MoonPlacements.FALLEN_METEOR);

        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_COPPER);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_CHEESE);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_SAPPHIRE);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_TIN);
        //biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.DIRT_PATCH);

        biomeGenerationSettings.addCarver(MoonCarvers.CAVE);

        return GalacticraftBiomes.baseSpaceBiome(0.2F)
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(GalacticraftSounds.MUSIC_SPACE_RACE))
                .mobSpawnSettings(mobSpawnSettings.build())
                .generationSettings(biomeGenerationSettings.build())
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(422313).build())
                .build();
    }
}
