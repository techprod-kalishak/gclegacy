package io.kalishak.galacticraftlegacy.world.level.biome;

import io.kalishak.galacticraftlegacy.data.worldgen.MoonCarvers;
import io.kalishak.galacticraftlegacy.sounds.GalacticraftSounds;
import io.kalishak.galacticraftlegacy.world.level.levelgen.placement.MoonPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
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
    public static final ResourceKey<Biome> MOON_CRATER_OCEAN = GalacticraftBiomes.key("moon_crater_oceans");

    static void bootstrap(BootstrapContext<Biome> cxt, HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        cxt.register(MOON_PLAINS, moonBiomeBase(20, 15, false, placedFeatures, worldCarvers));
        cxt.register(MOON_CRATER_OCEAN, moonBiomeBase(15, 10, true, placedFeatures, worldCarvers));
    }

    static Biome moonBiomeBase(int zombieWeight, int skeletonWeight, boolean hasLargeCraters, HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobSpawnSettings = new MobSpawnSettings.Builder();
        GalacticraftBiomes.evolvedMonsters(mobSpawnSettings, zombieWeight, skeletonWeight);
        BiomeGenerationSettings.Builder biomeGenerationSettings = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        addMoonOres(biomeGenerationSettings);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MoonPlacements.MOON_CRATER);
        biomeGenerationSettings.addCarver(MoonCarvers.CAVE);
        biomeGenerationSettings.addCarver(MoonCarvers.CAVE_EXTRA);

        if (hasLargeCraters) {
            biomeGenerationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MoonPlacements.MOON_CRATER_LARGE);
        }

        return GalacticraftBiomes.baseSpaceBiome(0.2F)
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(GalacticraftSounds.MUSIC_SPACE_RACE))
                .mobSpawnSettings(mobSpawnSettings.build())
                .generationSettings(biomeGenerationSettings.build())
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(422313).build())
                .build();
    }

    static void addMoonOres(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_COPPER_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_COPPER_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_CHEESE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_SAPPHIRE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_TIN_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_TIN_MIDDLE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MoonPlacements.ORE_TIN_LOWER);
    }

    public static int calculateSkyColor() {
        float temp = -2.0F / 3.0F;
        return ARGB.opaque(Mth.hsvToRgb(0.62222224F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F));
    }
}
