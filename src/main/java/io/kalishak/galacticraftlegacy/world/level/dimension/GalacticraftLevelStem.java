/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.level.biome.GalacticraftBiomes;
import io.kalishak.galacticraftlegacy.world.level.biome.SpaceBiomeSource;
import io.kalishak.galacticraftlegacy.world.level.levelgen.GalacticraftNoiseGeneratorSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGenerators;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.feature.VoidStartPlatformFeature;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("unused")
public class GalacticraftLevelStem {
    public static final ResourceKey<LevelStem> EARTH_ORBIT = Constants.key(Registries.LEVEL_STEM, "earth_orbit");
    public static final ResourceKey<LevelStem> MOON = Constants.key(Registries.LEVEL_STEM, "moon");
    public static final ResourceKey<LevelStem> MARS = Constants.key(Registries.LEVEL_STEM, "mars");
    public static final ResourceKey<LevelStem> ASTEROIDS = Constants.key(Registries.LEVEL_STEM, "asteroids");
    public static final ResourceKey<LevelStem> VENUS = Constants.key(Registries.LEVEL_STEM, "venus");

    public static void bootstrap(BootstrapContext<LevelStem> cxt) {
        HolderGetter<Biome> biomes = cxt.lookup(Registries.BIOME);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = cxt.lookup(Registries.NOISE_SETTINGS);
        HolderGetter<DimensionType> dimensions = cxt.lookup(Registries.DIMENSION_TYPE);

        cxt.register(
                EARTH_ORBIT, new LevelStem(
                        dimensions.getOrThrow(GalacticraftDimensionTypes.OVERWORLD_ORBIT),
                        new FlatLevelSource(new FlatLevelGeneratorSettings(Optional.empty(), biomes.getOrThrow(GalacticraftBiomes.SPACE), List.of())
                )
        ));
        cxt.register(
                MOON,
                noiseLevelStem(
                        biomes,
                        noiseSettings::getOrThrow,
                        dimensions::getOrThrow,
                        SpaceBiomeSource::moonBiomes,
                        GalacticraftNoiseGeneratorSettings.MOON,
                        GalacticraftDimensionTypes.MOON
                )
        );
    }

    private static LevelStem noiseLevelStem(HolderGetter<Biome> biomes,
                                            Function<ResourceKey<NoiseGeneratorSettings>, Holder<NoiseGeneratorSettings>> noiseSettings,
                                            Function<ResourceKey<DimensionType>, Holder<DimensionType>> dimensions,
                                            SpaceBiomeSource.BiomeResolver<Biome> resolver,
                                            ResourceKey<NoiseGeneratorSettings> noiseKey,
                                            ResourceKey<DimensionType> dimensionKey
    ) {
        ChunkGenerator chunkGenerator = new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromList(resolver.create(biomes::getOrThrow)), noiseSettings.apply(noiseKey));

        return new LevelStem(dimensions.apply(dimensionKey), chunkGenerator);
    }
}
