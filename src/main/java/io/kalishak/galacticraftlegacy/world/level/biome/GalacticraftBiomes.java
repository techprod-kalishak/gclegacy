package io.kalishak.galacticraftlegacy.world.level.biome;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface GalacticraftBiomes {
    static void bootstrap(BootstrapContext<Biome> cxt) {
        HolderGetter<PlacedFeature> placedFeatures = cxt.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> worldCarvers = cxt.lookup(Registries.CONFIGURED_CARVER);
        MoonBiomes.bootstrap(cxt, placedFeatures, worldCarvers);
    }

    static ResourceKey<Biome> key(String name) {
        return Constants.key(Registries.BIOME, name);
    }

    static Biome.BiomeBuilder baseSpaceBiome(float downfall) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(-2.0F)
                .downfall(downfall)
                .setAttribute(EnvironmentAttributes.WATER_EVAPORATES, true);
    }

    static void evolvedMonsters(MobSpawnSettings.Builder builder, int zombieWeight, int skeletonWeight) {
        builder.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 4, 4));
        builder.addSpawn(MobCategory.MONSTER, zombieWeight, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 4, 4));
        builder.addSpawn(MobCategory.MONSTER, skeletonWeight, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4));
        builder.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4));
        builder.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1));
    }
}
