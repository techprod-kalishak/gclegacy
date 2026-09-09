/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.client.data.*;
import io.kalishak.galacticraftlegacy.client.data.models.GalacticraftEquipmentAssetProvider;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftModelProvider;
import io.kalishak.galacticraftlegacy.client.data.models.GalacticraftCustomModelProvider;
import io.kalishak.galacticraftlegacy.data.advancement.GalacticraftAdvancementProvider;
import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import io.kalishak.galacticraftlegacy.data.loot.GalacticraftLootTableProvider;
import io.kalishak.galacticraftlegacy.data.recipes.GalacticraftRecipeProvider;
import io.kalishak.galacticraftlegacy.data.tag.*;
import io.kalishak.galacticraftlegacy.data.worldgen.GalacticraftCarvers;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.Checklist;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.damagesource.GalacticraftDamageTypes;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.VehicleCraftingPages;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipes;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingSlotTypes;
import io.kalishak.galacticraftlegacy.world.item.equipment.trim.GalacticraftTrimMaterials;
import io.kalishak.galacticraftlegacy.world.level.biome.GalacticraftBiomes;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensionTypes;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftLevelStem;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import io.kalishak.galacticraftlegacy.world.level.levelgen.GalacticraftBiomeModifiers;
import io.kalishak.galacticraftlegacy.world.level.levelgen.GalacticraftNoiseGeneratorSettings;
import io.kalishak.galacticraftlegacy.world.level.levelgen.GalacticraftNoiseRouterData;
import io.kalishak.galacticraftlegacy.world.level.levelgen.GalacticraftNoises;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.GalacticraftFeatures;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.ores.DenseOreVeins;
import io.kalishak.galacticraftlegacy.world.level.levelgen.placement.GalacticraftPlacements;
import io.kalishak.galacticraftlegacy.world.timeline.GalacticraftTimelines;
import io.kalishak.galacticraftlegacy.world.timeline.GalacticraftWorldClocks;
import net.minecraft.DetectedVersion;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;

public class GalacticraftData {
    private static final RegistrySetBuilder SET_BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, GalacticraftBiomes::bootstrap)
            .add(Registries.CONFIGURED_CARVER, GalacticraftCarvers::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, GalacticraftFeatures::bootstrap)
            .add(Registries.DAMAGE_TYPE, GalacticraftDamageTypes::bootstrap)
            .add(Registries.DENSITY_FUNCTION, GalacticraftNoiseRouterData::bootstrap)
            .add(Registries.DIMENSION_TYPE, GalacticraftDimensionTypes::bootstrap)
            .add(Registries.LEVEL_STEM, GalacticraftLevelStem::bootstrap)
            .add(Registries.NOISE, GalacticraftNoises::bootstrap)
            .add(Registries.NOISE_SETTINGS, GalacticraftNoiseGeneratorSettings::bootstrap)
            .add(Registries.PLACED_FEATURE, GalacticraftPlacements::bootstrap)
            .add(Registries.SULFUR_CUBE_ARCHETYPE, GalacticraftSulfurCubeArchetypes::bootstrap)
            .add(Registries.TIMELINE, GalacticraftTimelines::bootstrap)
            .add(Registries.TRIM_MATERIAL, GalacticraftTrimMaterials::bootstrap)
            .add(Registries.WORLD_CLOCK, GalacticraftWorldClocks::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GalacticraftBiomeModifiers::bootstrap)
            .add(GalacticraftRegistries.Keys.CHECKLIST, Checklist::bootstrap)
            .add(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE, VehicleCraftingSlotTypes::bootstrap)
            .add(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA, VehicleCraftingDataRecipes::bootstrap)
            .add(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_PAGE, VehicleCraftingPages::bootstrap)
            .add(GalacticraftRegistries.Keys.VEIN_TYPE, DenseOreVeins::bootstrap)
            .add(GalacticraftRegistries.Keys.SCHEMATIC, SchematicVariants::bootstrap)
            .add(GalacticraftRegistries.Keys.SPACE_STATION_RECIPE, SpaceStationRecipe::bootstrap);

    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(PackMetadataGenerator::new)
                .add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(
                        GalacticraftComponents.DATAPACK_DESCRIPTION.asComponent(),
                        new InclusiveRange<>(DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA))));
        event.createProvider(GalacticraftSpritesProvider::new);
        event.createProvider(GalacticraftLanguageProvider::new);
        event.createProvider(GalacticraftModelProvider::new);
        event.createProvider(GalacticraftCustomModelProvider::new);
        event.createProvider(GalacticraftEquipmentAssetProvider::new);
        event.createProvider(GalacticraftSoundProvider::new);
        event.createProvider(GalacticraftParticleProvider::new);
        event.createDatapackRegistryObjects(SET_BUILDER, Set.of(Galacticraft.MODID));
        event.createProvider(GalacticraftDataMaps.Provider::new);
        event.createProvider(GalacticraftLootTableProvider::create);
        event.createProvider(GalacticraftAdvancementProvider::create);
        event.createProvider(GalacticraftRecipeProvider.Runner::new);
        event.createProvider(GalacticraftBiomeTagsProvider::new);
        event.createProvider(GalacticraftChecklistTagsProvider::new);
        event.createProvider(GalacticraftDamageTypeTags::new);
        event.createProvider(GalacticraftDimensionTypeTags::new);
        event.createProvider(GalacticraftEntityTypeTagsProvider::new);
        event.createProvider(GalacticraftFluidTagsProvider::new);
        event.createBlockAndItemTags(GalacticraftBlockTagsProvider::new, GalacticraftItemTagsProvider::new);
        event.createProvider(GalacticraftTimelinesTagsProvider::new);

        DataGenerator.PackGenerator adventureMode = event.getGenerator().getBuiltinDatapack(true, "galacticraftlegacy", "adventure_mode");
        adventureMode.addProvider(packOutput -> PackMetadataGenerator.forFeaturePack(packOutput, GalacticraftComponents.ADVENTURE_MODE_DESCRIPTION.asComponent(), FeatureFlagSet.of(Galacticraft.ADVENTURE_MODE)));
    }

    public static void addClassicAssets(AddPackFindersEvent event) {
        event.addPackFinders(
                Constants.id("developers_art"),
                PackType.CLIENT_RESOURCES,
                GalacticraftComponents.CLASSIC_ASSETS_DESCRIPTION.asComponent(),
                PackSource.BUILT_IN,
                false,
                Pack.Position.BOTTOM
        );
    }
}
