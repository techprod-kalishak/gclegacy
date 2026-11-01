/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.components.flag.BuiltinFlagMaterials;
import io.kalishak.galacticraftlegacy.components.flag.FlagMaterial;
import io.kalishak.galacticraftlegacy.components.flag.FlagTypeProvider;
import io.kalishak.galacticraftlegacy.galaxies.celestial.Celestial;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.transfer.node.NodeNetwork;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.VehicleCraftingPage;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingSlotType;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.PlanetaryTransition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewDatapackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class GalacticraftRegistries {
    public static final Registry<MapCodec<? extends Celestial>> CELESTIAL_TYPE = new RegistryBuilder<>(Keys.CELESTIAL_TYPE).create();
    public static final Registry<MapCodec<? extends FlagTypeProvider>> FLAG_TYPE = new RegistryBuilder<>(Keys.FLAG_TYPE).create();
    public static final Registry<MapCodec<? extends NodeNetwork.PackedNode>> PACKED_NODE_TYPE = new RegistryBuilder<>(Keys.PACKED_NODE_TYPE).create();
    public static final Registry<MapCodec<? extends PlanetaryTransition>> PLANETARY_TRANSITION_TYPE = new RegistryBuilder<>(Keys.PLANETARY_TRANSITION_TYPE).create();

    @SubscribeEvent
    public static void newRegistries(NewRegistryEvent event) {
        event.register(CELESTIAL_TYPE);
        event.register(FLAG_TYPE);
        event.register(PACKED_NODE_TYPE);
        event.register(PLANETARY_TRANSITION_TYPE);
    }

    @SubscribeEvent
    public static void newDatapackRegistries(NewDatapackRegistryEvent event) {
        event.worldRegistry(Keys.CELESTIAL, Celestial.DIRECT_CODEC, Celestial.DIRECT_CODEC);
        event.<ChecklistEntry>worldRegistry(builder -> builder
                .key(Keys.CHECKLIST)
                .codec(ChecklistEntry.DIRECT_CODEC)
                .networkCodec(ChecklistEntry.DIRECT_CODEC)
                .configurator(config -> config.sync(true).defaultKey(Checklist.EQUIP_OXYGEN_SUIT))
        );
        event.<FlagMaterial>worldRegistry(builder -> builder
                .key(Keys.FLAG_MATERIAL)
                .codec(FlagMaterial.DIRECT_CODEC)
                .networkCodec(FlagMaterial.DIRECT_CODEC)
                .configurator(config -> config.sync(true).defaultKey(BuiltinFlagMaterials.CLASSIC))
        );
        event.<SchematicVariant>worldRegistry(builder -> builder
                .key(Keys.SCHEMATIC)
                .codec(SchematicVariant.DIRECT_CODEC)
                .networkCodec(SchematicVariant.DIRECT_CODEC)
                .configurator(config -> config.sync(true).defaultKey(SchematicVariants.TIER_2_ROCKET))
        );

        event.worldRegistry(Keys.SPACE_STATION_RECIPE, SpaceStationRecipe.DIRECT_CODEC, SpaceStationRecipe.DIRECT_CODEC);
        event.worldRegistry(Keys.VEHICLE_CRAFTING_RECIPE_DATA, VehicleCraftingDataRecipe.DIRECT_CODEC, VehicleCraftingDataRecipe.DIRECT_CODEC);
        event.worldRegistry(Keys.VEHICLE_CRAFTING_PAGE, VehicleCraftingPage.DIRECT_CODEC, VehicleCraftingPage.DIRECT_CODEC);
        event.worldRegistry(Keys.VEHICLE_CRAFTING_SLOT_TYPE, VehicleCraftingSlotType.DIRECT_CODEC, VehicleCraftingSlotType.DIRECT_CODEC);
    }

    public static class Keys {
        public static final ResourceKey<Registry<Celestial>> CELESTIAL = ResourceKey.createRegistryKey(Constants.id("celestial"));
        public static final ResourceKey<Registry<MapCodec<? extends Celestial>>> CELESTIAL_TYPE = ResourceKey.createRegistryKey(Constants.id("celestial_type"));
        public static final ResourceKey<Registry<MapCodec<? extends PlanetaryTransition>>> PLANETARY_TRANSITION_TYPE = ResourceKey.createRegistryKey(Constants.id("planetary_transition_type"));
        public static final ResourceKey<Registry<ChecklistEntry>> CHECKLIST = ResourceKey.createRegistryKey(Constants.id("checklist"));
        public static final ResourceKey<Registry<FlagMaterial>> FLAG_MATERIAL = ResourceKey.createRegistryKey(Constants.id("flag_material"));
        public static final ResourceKey<Registry<MapCodec<? extends FlagTypeProvider>>> FLAG_TYPE = ResourceKey.createRegistryKey(Constants.id("flag_type"));
        public static final ResourceKey<Registry<MapCodec<? extends NodeNetwork.PackedNode>>> PACKED_NODE_TYPE = ResourceKey.createRegistryKey(Constants.id("packed_node_type"));
        public static final ResourceKey<Registry<SchematicVariant>> SCHEMATIC = ResourceKey.createRegistryKey(Constants.id("schematic"));
        public static final ResourceKey<Registry<SpaceStationRecipe>> SPACE_STATION_RECIPE = ResourceKey.createRegistryKey(Constants.id("space_station_recipe"));
        public static final ResourceKey<Registry<VehicleCraftingDataRecipe>> VEHICLE_CRAFTING_RECIPE_DATA = ResourceKey.createRegistryKey(Constants.id("vehicle_crafting_recipe"));
        public static final ResourceKey<Registry<VehicleCraftingPage>> VEHICLE_CRAFTING_PAGE = ResourceKey.createRegistryKey(Constants.id("vehicle_crafting_page"));
        public static final ResourceKey<Registry<VehicleCraftingSlotType>> VEHICLE_CRAFTING_SLOT_TYPE = ResourceKey.createRegistryKey(Constants.id("vehicle_crafting_slot_type"));
    }
}
