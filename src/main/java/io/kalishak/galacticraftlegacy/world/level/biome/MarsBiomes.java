/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.biome;

import io.kalishak.galacticraftlegacy.sounds.GalacticraftSounds;
import io.kalishak.galacticraftlegacy.world.level.levelgen.placement.MarsPlacements;
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

import static io.kalishak.galacticraftlegacy.world.level.biome.GalacticraftBiomes.key;

public class MarsBiomes {
    public static final ResourceKey<Biome> MARS_PLAINS = key("mars_plains");

    static void bootstrap(BootstrapContext<Biome> context, HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        context.register(MARS_PLAINS, marsPlains(placedFeatures, worldCarvers));
    }

    static Biome marsPlains(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobSpawnSettings = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeGenerationSettings = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        GalacticraftBiomes.evolvedMonsters(mobSpawnSettings, 20, 15);

        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MarsPlacements.COPPER_ORE);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MarsPlacements.TIN_ORE);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MarsPlacements.DESH_ORE);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MarsPlacements.IRON_ORE);

        return GalacticraftBiomes.baseSpaceBiome(0.2F)
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(GalacticraftSounds.MUSIC_SPACE_RACE))
                .mobSpawnSettings(mobSpawnSettings.build())
                .generationSettings(biomeGenerationSettings.build())
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(422313).build())
                .build();
    }
}
