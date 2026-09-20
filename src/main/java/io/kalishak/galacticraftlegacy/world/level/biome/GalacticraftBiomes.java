/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.biome;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.level.levelgen.placement.GalacticraftPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface GalacticraftBiomes {
    ResourceKey<Biome> SPACE = key("space");

    static void bootstrap(BootstrapContext<Biome> cxt) {
        HolderGetter<PlacedFeature> placedFeatures = cxt.lookup(Registries.PLACED_FEATURE);
        HolderGetter<WorldCarver> worldCarvers = cxt.lookup(Registries.CARVER);

        cxt.register(
                SPACE,
                baseSpaceBiome(0.0F)
                        .specialEffects(new BiomeSpecialEffects.Builder().waterColor(0).build())
                        .mobSpawnSettings(MobSpawnSettings.EMPTY)
                        .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GalacticraftPlacements.SPACE_STATION).build())
                        .build()
        );

        MoonBiomes.bootstrap(cxt, placedFeatures, worldCarvers);
        MarsBiomes.bootstrap(cxt, placedFeatures, worldCarvers);
    }

    static ResourceKey<Biome> key(String name) {
        return Constants.key(Registries.BIOME, name);
    }

    static Biome.BiomeBuilder baseSpaceBiome(float downfall) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(-2.0F)
                .downfall(downfall);
    }

    static void evolvedMonsters(MobSpawnSettings.Builder builder, int zombieWeight, int skeletonWeight) {
        //builder.addSpawn(GalacticraftEntityType.EVOLVED_SPIDER.get(), 100, ConstantInt.of(4));
        builder.addSpawn(GalacticraftEntityType.EVOLVED_ZOMBIE.get(), zombieWeight, ConstantInt.of(4));
        builder.addSpawn(GalacticraftEntityType.EVOLVED_SKELETON.get(), skeletonWeight, ConstantInt.of(4));
        //builder.addSpawn(GalacticraftEntityType.EVOLVED_ENDERMAN.get(), 10, UniformInt.of(1, 4));
        //builder.addSpawn(GalacticraftEntityType.EVOLVED_WITCH.get(), 5, ConstantInt.of(1));
    }
}
