/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.timeline.Timeline;

public final class GalacticraftTags {
    private static <R> TagKey<R> tagKey(ResourceKey<? extends Registry<R>> registryKey, String tagKey) {
        return TagKey.create(registryKey, Constants.id(tagKey));
    }

    private static <R> TagKey<R> shared(ResourceKey<? extends Registry<R>> registryKey, String tagKey) {
        return TagKey.create(registryKey, Identifier.fromNamespaceAndPath("c", tagKey));
    }
    
    private static BlockItemTagId combinedTagKey(String tagKey) {
        return new BlockItemTagId(
                tagKey(Registries.BLOCK, tagKey),
                tagKey(Registries.ITEM, tagKey)
        );
    }

    public static class Biomes {
        public static final TagKey<Biome> IS_MOON = tagKey(Registries.BIOME, "is_moon");
        public static final TagKey<Biome> IS_ORBIT = tagKey(Registries.BIOME, "is_orbit");
        public static final TagKey<Biome> IS_MARS = tagKey(Registries.BIOME, "is_mars");
        public static final TagKey<Biome> IS_ASTEROIDS = tagKey(Registries.BIOME, "is_asteroids");
        public static final TagKey<Biome> IS_VENUS = tagKey(Registries.BIOME, "is_venus");

        public static final TagKey<Biome> HAS_BASIC_FEATURES = tagKey(Registries.BIOME, "has_basic_features");
        public static final TagKey<Biome> HAS_ADVANCED_FEATURES = tagKey(Registries.BIOME, "has_advanced_features");
        public static final TagKey<Biome> HAS_ULTIMATE_FEATURES = tagKey(Registries.BIOME, "has_ultimate_features");
    }

    public static class Blocks {
        public static final TagKey<Block> METEOR_BLOCK_REPLACEABLE = tagKey(Registries.BLOCK, "meteor_block_replaceable");
        public static final TagKey<Block> BASE_STONE_ASTEROID = BlockItems.BASE_STONE_ASTEROID.block();
        public static final TagKey<Block> BASE_STONE_MOON = BlockItems.BASE_STONE_MOON.block();
        public static final TagKey<Block> BASE_STONE_MARS = BlockItems.BASE_STONE_MARS.block();
        public static final TagKey<Block> BREATHABLE_AIR = tagKey(Registries.BLOCK, "breathable_air");
        public static final TagKey<Block> CRUDE_OIL_POOL_REPLACEABLE = tagKey(Registries.BLOCK, "crude_oil_pool_replaceable");
        public static final TagKey<Block> CONNECTS_TO_TINTED_GLASS_PANES = tagKey(Registries.BLOCK, "connects_to_tinted_glass_panes");
        public static final TagKey<Block> MOON_CARVER_REPLACEABLES = tagKey(Registries.BLOCK, "moon_carver_replaceables");
        public static final TagKey<Block> INCORRECT_FOR_DESH_TOOL = tagKey(Registries.BLOCK, "incorrect_for_desh_tool");
        public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = tagKey(Registries.BLOCK, "incorrect_for_steel_tool");
        public static final TagKey<Block> INCORRECT_FOR_TITANIUM_TOOL = tagKey(Registries.BLOCK, "incorrect_for_titanium_tool");
        public static final TagKey<Block> INFINIBURN_OPEN_SPACE = tagKey(Registries.BLOCK, "infiniburn_open_space");
        public static final TagKey<Block> INFINIBURN_VENUS = tagKey(Registries.BLOCK, "infiniburn_venus");
        public static final TagKey<Block> MACHINE = BlockItems.MACHINE.block();
        public static final TagKey<Block> MACHINE_BASIC = BlockItems.MACHINE_BASIC.block();
        public static final TagKey<Block> MACHINE_ADVANCED = BlockItems.MACHINE_ADVANCED.block();
        public static final TagKey<Block> NASA_WORKBENCHES = BlockItems.NASA_WORKBENCHES.block();
        public static final TagKey<Block> ORES_ALUMINUM = BlockItems.ORES_ALUMINUM.block();
        public static final TagKey<Block> ORES_CHEESE = BlockItems.ORES_CHEESE.block();
        public static final TagKey<Block> ORES_DESH = BlockItems.ORES_DESH.block();
        public static final TagKey<Block> ORES_LEAD = BlockItems.ORES_LEAD.block();
        public static final TagKey<Block> ORES_SAPPHIRE = BlockItems.ORES_SAPPHIRE.block();
        public static final TagKey<Block> ORES_SILICON = BlockItems.ORES_SILICON.block();
        public static final TagKey<Block> ORES_SOLAR = BlockItems.ORES_SOLAR.block();
        public static final TagKey<Block> ORES_TIN = BlockItems.ORES_TIN.block();
        public static final TagKey<Block> SEALABLE = tagKey(Registries.BLOCK, "sealable");
        public static final TagKey<Block> SEALABLE_FROM_BOTTOM = tagKey(Registries.BLOCK, "sealable/from_bottom");
        public static final TagKey<Block> SENSOR_GLASSES_DETECTABLE = tagKey(Registries.BLOCK, "sensor_glasses_detectable");
        public static final TagKey<Block> STORAGE_BLOCKS_ALUMINUM = BlockItems.STORAGE_BLOCKS_ALUMINUM.block();
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_ALUMINUM = BlockItems.STORAGE_BLOCKS_RAW_ALUMINUM.block();
        public static final TagKey<Block> STORAGE_BLOCKS_METEORIC_IRON = BlockItems.STORAGE_BLOCKS_METEORIC_IRON.block();
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_METEORIC_IRON = BlockItems.STORAGE_BLOCKS_RAW_METEORIC_IRON.block();
        public static final TagKey<Block> STORAGE_BLOCKS_TIN = BlockItems.STORAGE_BLOCKS_TIN.block();
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_TIN = BlockItems.STORAGE_BLOCKS_RAW_TIN.block();
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_SILICON = BlockItems.STORAGE_BLOCKS_RAW_SILICON.block();
        public static final TagKey<Block> LIT_TORCHES = tagKey(Registries.BLOCK, "lit_torches");
        public static final TagKey<Block> LIT_TORCHES_STANDING = BlockItems.LIT_TORCHES_STANDING.block();
        public static final TagKey<Block> LIT_TORCHES_WALL = tagKey(Registries.BLOCK, "lit_torches/wall");
    }

    public static class BlockItems {
        public static final BlockItemTagId METEOR_BLOCK_REPLACEABLE = combinedTagKey("block_meteor_replaceable");
        public static final BlockItemTagId BASE_STONE_ASTEROID = combinedTagKey("base_stone_asteroid");
        public static final BlockItemTagId BASE_STONE_MOON = combinedTagKey("base_stone_moon");
        public static final BlockItemTagId BASE_STONE_MARS = combinedTagKey("base_stone_mars");
        public static final BlockItemTagId BREATHABLE_AIR = combinedTagKey("breathable_air");
        public static final BlockItemTagId CRUDE_OIL_POOL_REPLACEABLE = combinedTagKey("crude_oil_pool_replaceable");
        public static final BlockItemTagId MOON_CARVER_REPLACEABLES = combinedTagKey("moon_carver_replaceables");
        public static final BlockItemTagId INCORRECT_FOR_DESH_TOOL = combinedTagKey("incorrect_for_desh_tool");
        public static final BlockItemTagId INCORRECT_FOR_STEEL_TOOL = combinedTagKey("incorrect_for_steel_tool");
        public static final BlockItemTagId INCORRECT_FOR_TITANIUM_TOOL = combinedTagKey("incorrect_for_titanium_tool");
        public static final BlockItemTagId INFINIBURN_OPEN_SPACE = combinedTagKey("infiniburn_open_space");
        public static final BlockItemTagId INFINIBURN_VENUS = combinedTagKey("infiniburn_open_space");
        public static final BlockItemTagId MACHINE = combinedTagKey("machine");
        public static final BlockItemTagId MACHINE_BASIC = combinedTagKey("machine/basic");
        public static final BlockItemTagId MACHINE_ADVANCED = combinedTagKey("machine/advanced");
        public static final BlockItemTagId NASA_WORKBENCHES = combinedTagKey("nasa_workbenches");
        public static final BlockItemTagId ORES_ALUMINUM = combinedTagKey("ores/aluminum");
        public static final BlockItemTagId ORES_CHEESE = combinedTagKey("ores/cheese");
        public static final BlockItemTagId ORES_DESH = combinedTagKey("ores/desh");
        public static final BlockItemTagId ORES_LEAD = combinedTagKey("ores/lead");
        public static final BlockItemTagId ORES_SAPPHIRE = combinedTagKey("ores/sapphire");
        public static final BlockItemTagId ORES_SILICON = combinedTagKey("ores/silicon");
        public static final BlockItemTagId ORES_SOLAR = combinedTagKey("ores/solar");
        public static final BlockItemTagId ORES_TIN = combinedTagKey("ores/tin");
        public static final BlockItemTagId SEALABLE = combinedTagKey("sealable");
        public static final BlockItemTagId SEALABLE_FROM_BOTTOM = combinedTagKey("sealable/from_bottom");
        public static final BlockItemTagId SENSOR_GLASSES_DETECTABLE = combinedTagKey("sensor_glasses_detectable");
        public static final BlockItemTagId STORAGE_BLOCKS_ALUMINUM = combinedTagKey("storage_blocks/aluminum");
        public static final BlockItemTagId STORAGE_BLOCKS_RAW_ALUMINUM = combinedTagKey("storage_blocks/raw_aluminum");
        public static final BlockItemTagId STORAGE_BLOCKS_DESH = combinedTagKey("storage_blocks/desh");
        public static final BlockItemTagId STORAGE_BLOCKS_RAW_DESH = combinedTagKey("storage_blocks/raw_desh");
        public static final BlockItemTagId STORAGE_BLOCKS_METEORIC_IRON = combinedTagKey("storage_blocks/meteoric_iron");
        public static final BlockItemTagId STORAGE_BLOCKS_RAW_METEORIC_IRON = combinedTagKey("storage_blocks/raw_meteoric_iron");
        public static final BlockItemTagId STORAGE_BLOCKS_TIN = combinedTagKey("storage_blocks/tin");
        public static final BlockItemTagId STORAGE_BLOCKS_RAW_TIN = combinedTagKey("storage_blocks/raw_tin");
        public static final BlockItemTagId STORAGE_BLOCKS_RAW_SILICON = combinedTagKey("storage_blocks/raw_silicon");
        public static final BlockItemTagId LIT_TORCHES = combinedTagKey("lit_torches");
        public static final BlockItemTagId LIT_TORCHES_STANDING = combinedTagKey("lit_torches/standing");
    }

    public static class Checklist {
        public static final TagKey<ChecklistEntry> OVERWORLD_CHECKLIST = tagKey(GalacticraftRegistries.Keys.CHECKLIST, "overworld");
        public static final TagKey<ChecklistEntry> MOON_CHECKLIST = tagKey(GalacticraftRegistries.Keys.CHECKLIST, "moon");
        public static final TagKey<ChecklistEntry> SATELLITE_CHECKLIST = tagKey(GalacticraftRegistries.Keys.CHECKLIST, "satellite");
        public static final TagKey<ChecklistEntry> MARS_CHECKLIST = tagKey(GalacticraftRegistries.Keys.CHECKLIST, "mars");
        public static final TagKey<ChecklistEntry> ASTEROIDS_CHECKLIST = tagKey(GalacticraftRegistries.Keys.CHECKLIST, "asteroids");
        public static final TagKey<ChecklistEntry> VENUS_CHECKLIST = tagKey(GalacticraftRegistries.Keys.CHECKLIST, "venus");
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> BYPASSES_SHIELD_CONTROLLER = tagKey(Registries.DAMAGE_TYPE, "bypasses_shield_controller");
    }

    public static class DimensionTypes {
        public static final TagKey<DimensionType> GRAVITY_OVERRIDDEN = tagKey(Registries.DIMENSION_TYPE, "gravity_overridden");
        public static final TagKey<DimensionType> NEEDS_FREQUENCY_MODULE = tagKey(Registries.DIMENSION_TYPE, "needs_frequency_module");
        public static final TagKey<DimensionType> REQUIRES_CRYOCHAMBER = tagKey(Registries.DIMENSION_TYPE, "requires_cryochamber");
        public static final TagKey<DimensionType> HAS_DISABLED_ROCKETS = tagKey(Registries.DIMENSION_TYPE, "has_disabled_rockets");
        public static final TagKey<DimensionType> HAS_METEORS = tagKey(Registries.DIMENSION_TYPE, "has_meteors");
        public static final TagKey<DimensionType> SPACE_MOB_HABITABLE = tagKey(Registries.DIMENSION_TYPE, "space_mob_habitable");
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BYPASSES_CELESTIAL_GRAVITY = tagKey(Registries.ENTITY_TYPE, "bypasses_celestial_gravity");
        public static final TagKey<EntityType<?>> CAN_EQUIP_GEAR = tagKey(Registries.ENTITY_TYPE, "can_equip_gear");
        public static final TagKey<EntityType<?>> CAN_EQUIP_PARACHUTE = tagKey(Registries.ENTITY_TYPE, "can_equip_parachute");
        public static final TagKey<EntityType<?>> SPACE_MOB = tagKey(Registries.ENTITY_TYPE, "space_mob");
    }

    public static class Fluids {
        public static final TagKey<Fluid> FLAMMABLE_LIQUID = tagKey(Registries.FLUID, "flammable_liquid");
        public static final TagKey<Fluid> FLOWS_GRATING = tagKey(Registries.FLUID, "flows_grating");
        public static final TagKey<Fluid> IS_OXYGEN = shared(Registries.FLUID, "is_oxygen");
        public static final TagKey<Fluid> IS_OIL = shared(Registries.FLUID, "is_oil");
        public static final TagKey<Fluid> IS_FUEL = shared(Registries.FLUID, "is_fuel");
    }

    public static class Items {
        public static final TagKey<Item> GEMS_SAPPHIRE = shared(Registries.ITEM, "gems/sapphire");
        public static final TagKey<Item> INGOTS_ALUMINUM = shared(Registries.ITEM, "ingots/aluminum");
        public static final TagKey<Item> INGOTS_BRONZE = shared(Registries.ITEM, "ingots/bronze");
        public static final TagKey<Item> INGOTS_DESH = shared(Registries.ITEM, "ingots/desh");
        public static final TagKey<Item> INGOTS_IRIDIUM = shared(Registries.ITEM, "ingots/iridium");
        public static final TagKey<Item> INGOTS_STEEL = shared(Registries.ITEM, "ingots/steel");
        public static final TagKey<Item> INGOTS_TIN = shared(Registries.ITEM, "ingots/tin");
        public static final TagKey<Item> INGOTS_TITANIUM = shared(Registries.ITEM, "ingots/titanium");
        public static final TagKey<Item> INGOTS_LEAD = shared(Registries.ITEM, "ingots/lead");
        public static final TagKey<Item> NUGGETS_DESH = shared(Registries.ITEM, "nuggets/desh");
        public static final TagKey<Item> NUGGETS_STEEL = shared(Registries.ITEM, "nuggets/steel");
        public static final TagKey<Item> NUGGETS_TITANIUM = shared(Registries.ITEM, "nuggets/titanium");
        public static final TagKey<Item> PARACHUTE = tagKey(Registries.ITEM, "parachute");
        public static final TagKey<Item> PLATES = shared(Registries.ITEM, "plates");
        public static final TagKey<Item> PLATE_ALUMINUM = shared(Registries.ITEM, "plates/aluminum");
        public static final TagKey<Item> PLATE_BRONZE = shared(Registries.ITEM, "plates/bronze");
        public static final TagKey<Item> PLATE_COPPER = shared(Registries.ITEM, "plates/copper");
        public static final TagKey<Item> PLATE_DESH = shared(Registries.ITEM, "plates/desh");
        public static final TagKey<Item> PLATE_IRON = shared(Registries.ITEM, "plates/iron");
        public static final TagKey<Item> PLATE_METEORIC_IRON = shared(Registries.ITEM, "plates/meteoric_iron");
        public static final TagKey<Item> PLATE_TIN = shared(Registries.ITEM, "plates/tin");
        public static final TagKey<Item> PLATE_TITANIUM = shared(Registries.ITEM, "plates/titanium");
        public static final TagKey<Item> PLATE_STEEL = shared(Registries.ITEM, "plates/steel");
        public static final TagKey<Item> PLATE_HEAVY_DUTY = shared(Registries.ITEM, "plates/heavy_duty");
        public static final TagKey<Item> PLATE_HEAVY_DUTY_2 = shared(Registries.ITEM, "plates/heavy_duty_tier_2");
        public static final TagKey<Item> PLATE_HEAVY_DUTY_3 = shared(Registries.ITEM, "plates/heavy_duty_tier_3");
        public static final TagKey<Item> ORES_ALUMINUM = shared(Registries.ITEM, "ores/aluminum");
        public static final TagKey<Item> ORES_CHEESE = shared(Registries.ITEM, "ores/cheese");
        public static final TagKey<Item> ORES_IRIDIUM = shared(Registries.ITEM, "ores/iridium");
        public static final TagKey<Item> ORES_SAPPHIRE = shared(Registries.ITEM, "ores/sapphire");
        public static final TagKey<Item> ORES_SILICON = shared(Registries.ITEM, "ores/silicon");
        public static final TagKey<Item> ORES_TIN = shared(Registries.ITEM, "ores/tin");
        public static final TagKey<Item> RAW_MATERIALS_ALUMINUM = shared(Registries.ITEM, "raw_materials/aluminum");
        public static final TagKey<Item> RAW_MATERIALS_CHEESE = shared(Registries.ITEM, "raw_materials/cheese");
        public static final TagKey<Item> RAW_MATERIALS_DESH = shared(Registries.ITEM, "raw_materials/desh");
        public static final TagKey<Item> RAW_MATERIALS_IRIDIUM = shared(Registries.ITEM, "raw_materials/iridium");
        public static final TagKey<Item> RAW_MATERIALS_LEAD = shared(Registries.ITEM, "raw_materials/lead");
        public static final TagKey<Item> RAW_MATERIALS_SILICON = shared(Registries.ITEM, "raw_materials/silicon");
        public static final TagKey<Item> RAW_MATERIALS_STEEL = shared(Registries.ITEM, "raw_materials/steel");
        public static final TagKey<Item> RAW_MATERIALS_TIN = shared(Registries.ITEM, "raw_materials/tin");
        public static final TagKey<Item> RAW_MATERIALS_TITANIUM = shared(Registries.ITEM, "raw_materials/titanium");
        public static final TagKey<Item> REPAIRS_DESH_ARMOR = shared(Registries.ITEM, "repairs_desh_armor");
        public static final TagKey<Item> REPAIRS_STEEL_ARMOR = shared(Registries.ITEM, "repairs_steel_armor");
        public static final TagKey<Item> REPAIRS_TITANIUM_ARMOR = shared(Registries.ITEM, "repairs_titanium_armor");
        public static final TagKey<Item> REPAIRS_DESH_TOOL = shared(Registries.ITEM, "desh_tool_materials");
        public static final TagKey<Item> REPAIRS_STEEL_TOOL = shared(Registries.ITEM, "steel_tool_materials");
        public static final TagKey<Item> REPAIRS_TITANIUM_TOOL = shared(Registries.ITEM, "titanium_tool_materials");
        public static final TagKey<Item> VEHICLE_INGREDIENTS = tagKey(Registries.ITEM, "vehicle_ingredient");
        public static final TagKey<Item> VEHICLE_INGREDIENT_CONE = tagKey(Registries.ITEM, "vehicle_ingredient/cone");
        public static final TagKey<Item> VEHICLE_INGREDIENT_PLATING = tagKey(Registries.ITEM, "vehicle_ingredient/plating");
        public static final TagKey<Item> VEHICLE_INGREDIENT_FIN = tagKey(Registries.ITEM, "vehicle_ingredient/fin");
        public static final TagKey<Item> VEHICLE_INGREDIENT_ENGINE = tagKey(Registries.ITEM, "vehicle_ingredient/engine");
        public static final TagKey<Item> VEHICLE_INGREDIENT_BOOSTER = tagKey(Registries.ITEM, "vehicle_ingredient/booster");
        public static final TagKey<Item> VEHICLE_INGREDIENT_STORAGE = tagKey(Registries.ITEM, "vehicle_ingredient/storage");
        public static final TagKey<Item> VEHICLE_INGREDIENT_WHEEL = tagKey(Registries.ITEM, "vehicle_ingredient/wheel");
        public static final TagKey<Item> VEHICLE_INGREDIENT_SEAT = tagKey(Registries.ITEM, "vehicle_ingredient/seat");
        public static final TagKey<Item> VEHICLE_INGREDIENT_MISC = tagKey(Registries.ITEM, "vehicle_ingredient/misc");
        public static final TagKey<Item> STORAGE_BLOCKS_ALUMINUM = shared(Registries.ITEM, "storage_blocks/aluminum");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_ALUMINUM = shared(Registries.ITEM, "storage_blocks/raw_aluminum");
        public static final TagKey<Item> STORAGE_BLOCKS_TIN = shared(Registries.ITEM, "storage_blocks/tin");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_TIN = shared(Registries.ITEM, "storage_blocks/raw_tin");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_SILICON = shared(Registries.ITEM, "storage_blocks/raw_silicon");
        public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_SPACY = tagKey(Registries.ITEM, "sulfur_cube_archetype/spacy");
        public static final TagKey<Item> WRENCH = shared(Registries.ITEM, "tools/wrench");
    }

    public static class Structures {
        public static final TagKey<Structure> IS_DUNGEON = tagKey(Registries.STRUCTURE, "is_dungeon");
    }

    public static class Timelines {
        public static final TagKey<Timeline> IN_ORBIT = tagKey(Registries.TIMELINE, "in_orbit");
        public static final TagKey<Timeline> IN_MOON = tagKey(Registries.TIMELINE, "in_moon");
        public static final TagKey<Timeline> IN_MARS = tagKey(Registries.TIMELINE, "in_mars");
        public static final TagKey<Timeline> IN_ASTEROIDS = tagKey(Registries.TIMELINE, "in_asteroids");
        public static final TagKey<Timeline> IN_VENUS = tagKey(Registries.TIMELINE, "in_venus");
    }
}
