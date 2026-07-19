/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.galaxies.CelestialBodyType;
import io.kalishak.galacticraftlegacy.galaxies.CelestialObject;
import io.kalishak.galacticraftlegacy.transfer.node.NodeNetwork;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.VehicleCraftingPage;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.VehicleCraftingPages;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingSlotType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipe;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.PlanetaryTransition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class GalacticraftRegistries {
    public static final Registry<CelestialObject> CELESTIAL_OBJECT = new RegistryBuilder<>(Keys.CELESTIAL_OBJECT)
            .defaultKey(Constants.id("sol"))
            .create();
    public static final Registry<MapCodec<? extends NodeNetwork.PackedNode>> PACKED_NODE_TYPE = new RegistryBuilder<>(Keys.PACKED_NODE_TYPE)
            .create();
    public static final Registry<MapCodec<? extends PlanetaryTransition>> PLANETARY_TRANSITION_TYPE = new RegistryBuilder<>(Keys.PLANETARY_TRANSITION_TYPE)
            .create();

    @SubscribeEvent
    public static void newRegistries(NewRegistryEvent event) {
        event.register(CELESTIAL_OBJECT);
        event.register(PACKED_NODE_TYPE);
        event.register(PLANETARY_TRANSITION_TYPE);
    }

    @SubscribeEvent
    public static void newDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Keys.CHECKLIST, ChecklistEntry.DIRECT_CODEC, ChecklistEntry.DIRECT_CODEC, builder -> builder.sync(true).defaultKey(Checklist.EQUIP_OXYGEN_SUIT));
        event.dataPackRegistry(Keys.SCHEMATIC, SchematicVariant.DIRECT_CODEC, SchematicVariant.DIRECT_CODEC, builder -> builder.sync(true).defaultKey(SchematicVariants.TIER_2_ROCKET));
        event.dataPackRegistry(Keys.CELESTIAL_BODY_LEVEL_DATA, CelestialBodyLevelData.DIRECT_CODEC, CelestialBodyLevelData.DIRECT_CODEC, builder -> builder.sync(true).defaultKey(CelestialBodyLevelDataEntries.OVERWORLD));
        event.dataPackRegistry(Keys.SPACE_STATION_RECIPE, SpaceStationRecipe.DIRECT_CODEC, SpaceStationRecipe.DIRECT_CODEC, builder -> builder.sync(true));
        event.dataPackRegistry(Keys.VEHICLE_CRAFTING_RECIPE_DATA, VehicleCraftingDataRecipe.DIRECT_CODEC, VehicleCraftingDataRecipe.DIRECT_CODEC, builder -> builder.sync(true));
        event.dataPackRegistry(Keys.VEHICLE_CRAFTING_PAGE, VehicleCraftingPage.DIRECT_CODEC, VehicleCraftingPage.DIRECT_CODEC, builder -> builder.sync(true));
        event.dataPackRegistry(Keys.VEHICLE_CRAFTING_SLOT_TYPE, VehicleCraftingSlotType.DIRECT_CODEC, VehicleCraftingSlotType.DIRECT_CODEC, builder -> builder.sync(true));
    }

    public static class Keys {
        public static final ResourceKey<? extends Registry<CelestialObject>> CELESTIAL_OBJECT = ResourceKey.createRegistryKey(Constants.id("celestial_body"));
        public static final ResourceKey<? extends Registry<CelestialBodyType>> CELESTIAL_BODY_TYPE = ResourceKey.createRegistryKey(Constants.id("celestial_body_type"));
        public static final ResourceKey<Registry<MapCodec<? extends PlanetaryTransition>>> PLANETARY_TRANSITION_TYPE = ResourceKey.createRegistryKey(Constants.id("planetary_transition_type"));
        public static final ResourceKey<Registry<ChecklistEntry>> CHECKLIST = ResourceKey.createRegistryKey(Constants.id("checklist"));
        public static final ResourceKey<Registry<CelestialBodyLevelData>> CELESTIAL_BODY_LEVEL_DATA = ResourceKey.createRegistryKey(Constants.id("celestial_body_level_data"));
        public static final ResourceKey<Registry<MapCodec<? extends NodeNetwork.PackedNode>>> PACKED_NODE_TYPE = ResourceKey.createRegistryKey(Constants.id("packed_node_type"));
        public static final ResourceKey<Registry<SchematicVariant>> SCHEMATIC = ResourceKey.createRegistryKey(Constants.id("schematic"));
        public static final ResourceKey<Registry<SpaceStationRecipe>> SPACE_STATION_RECIPE = ResourceKey.createRegistryKey(Constants.id("space_station_recipe"));
        public static final ResourceKey<Registry<VehicleCraftingDataRecipe>> VEHICLE_CRAFTING_RECIPE_DATA = ResourceKey.createRegistryKey(Constants.id("vehicle_crafting_recipe"));
        public static final ResourceKey<Registry<VehicleCraftingPage>> VEHICLE_CRAFTING_PAGE = ResourceKey.createRegistryKey(Constants.id("vehicle_crafting_page"));
        public static final ResourceKey<Registry<VehicleCraftingSlotType>> VEHICLE_CRAFTING_SLOT_TYPE = ResourceKey.createRegistryKey(Constants.id("vehicle_crafting_slot_type"));
    }
}
