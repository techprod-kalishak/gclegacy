/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.client.data.*;
import io.kalishak.galacticraftlegacy.client.data.models.GalacticraftCustomModelProvider;
import io.kalishak.galacticraftlegacy.client.data.models.GalacticraftEquipmentAssetProvider;
import io.kalishak.galacticraftlegacy.component.flag.BuiltinFlagMaterials;
import io.kalishak.galacticraftlegacy.data.advancement.GalacticraftAdvancements;
import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import io.kalishak.galacticraftlegacy.data.loot.GalacticraftBlockLootSubProvider;
import io.kalishak.galacticraftlegacy.data.loot.GalacticraftChestLootSubProvider;
import io.kalishak.galacticraftlegacy.data.loot.GalacticraftEntityLootSubProvider;
import io.kalishak.galacticraftlegacy.data.recipes.GalacticraftRecipeProvider;
import io.kalishak.galacticraftlegacy.data.tag.*;
import io.kalishak.galacticraftlegacy.data.worldgen.GalacticraftCarvers;
import io.kalishak.galacticraftlegacy.data.worldgen.GalacticraftMaterialRules;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.BuiltinChecklist;
import io.kalishak.galacticraftlegacy.registry.BuiltinGalaxies;
import io.kalishak.galacticraftlegacy.registry.BuiltinSchematicVariants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
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
import io.kalishak.galacticraftlegacy.world.level.levelgen.placement.GalacticraftPlacements;
import io.kalishak.galacticraftlegacy.world.timeline.GalacticraftTimelines;
import io.kalishak.galacticraftlegacy.world.timeline.GalacticraftWorldClocks;
import net.minecraft.DetectedVersion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GalacticraftData {
    @SubscribeEvent
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
        event.createProvider(GalacticraftDataMaps.Provider::new);
        event.createProvider(GalacticraftBiomeTagsProvider::new);
        event.createProvider(GalacticraftCelestialTagsProvider::new);
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

    @SubscribeEvent
    public static void gatherRegistryEntries(GatherDataRegistryEntriesEvent event) {
        event.add(Registries.BIOME, GalacticraftBiomes::bootstrap);
        event.add(Registries.CARVER, GalacticraftCarvers::bootstrap);
        event.add(Registries.FEATURE, GalacticraftFeatures::bootstrap);
        event.add(Registries.DAMAGE_TYPE, GalacticraftDamageTypes::bootstrap);
        event.add(Registries.DENSITY_FUNCTION, GalacticraftNoiseRouterData::bootstrap);
        event.add(Registries.DIMENSION_TYPE, GalacticraftDimensionTypes::bootstrap);
        event.add(Registries.LEVEL_STEM, GalacticraftLevelStem::bootstrap);
        event.add(Registries.MATERIAL_RULE, GalacticraftMaterialRules::bootstrap);
        event.add(Registries.NOISE, GalacticraftNoises::bootstrap);
        event.add(Registries.NOISE_SETTINGS, GalacticraftNoiseGeneratorSettings::bootstrap);
        event.add(Registries.PLACED_FEATURE, GalacticraftPlacements::bootstrap);
        event.add(Registries.SULFUR_CUBE_ARCHETYPE, GalacticraftSulfurCubeArchetypes::bootstrap);
        event.add(Registries.TIMELINE, GalacticraftTimelines::bootstrap);
        event.add(Registries.TRIM_MATERIAL, GalacticraftTrimMaterials::bootstrap);
        event.add(Registries.WORLD_CLOCK, GalacticraftWorldClocks::bootstrap);
        event.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GalacticraftBiomeModifiers::bootstrap);
        event.add(GalacticraftRegistries.Keys.FLAG_MATERIAL, BuiltinFlagMaterials::bootstrap);
        event.add(GalacticraftRegistries.Keys.CELESTIAL, BuiltinGalaxies::bootstrap);
        event.add(GalacticraftRegistries.Keys.CHECKLIST, BuiltinChecklist::bootstrap);
        event.add(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE, VehicleCraftingSlotTypes::bootstrap);
        event.add(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA, VehicleCraftingDataRecipes::bootstrap);
        event.add(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_PAGE, VehicleCraftingPages::bootstrap);
        event.add(GalacticraftRegistries.Keys.SCHEMATIC, BuiltinSchematicVariants::bootstrap);
        event.add(GalacticraftRegistries.Keys.SPACE_STATION_RECIPE, SpaceStationRecipe::bootstrap);
        event.advancement(GalacticraftAdvancements::new);
        event.lootTable(
                new LootTableProvider.SubProviderEntry(GalacticraftBlockLootSubProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(GalacticraftChestLootSubProvider::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(GalacticraftEntityLootSubProvider::new, LootContextParamSets.ENTITY)
        );
        event.recipe(GalacticraftRecipeProvider::new);
    }

    @SubscribeEvent
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
