/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class GalacticraftLanguageProvider extends LanguageProvider {
    public GalacticraftLanguageProvider(PackOutput output) {
        super(output, Galacticraft.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("item.galacticraftlegacy.tank.tooltip", "Oxygen Remaining: %s");
        add("galacticraftlegacy.networking_failed", "There was an error on the network thread: ");
        add("item.galacticraftlegacy.battery.tooltip", "Energy Stored: %s");
        add("item.galacticraftlegact.fluid_tank.empty", "Tank is empty");
        add("item.galacticraftlegacy.fluid_tank.tooltip", "Fluid in tank %s: %s");
        add("itemGroup.galacticraftlegacy.blocks", "Galacticraft Legacy Blocks");
        add("itemGroup.galacticraftlegacy.items", "Galacticraft Legacy Items");

        add("block.galacticraftlegacy.bed.sleep_in_cryo_chamber", "I should sleep in a Cryo-chamber");

        add("container.coal_generator.generating", "Generating");
        add("container.coal_generator.not_generating", "Not generating");
        add("container.coal_generator.heat_level", "Hull Heat: %s");
        add("block.galacticraftlegacy.oxygen", "Oxygen");
        add("item.galacticraftlegacy.infinite", "Infinite");
        add("item.galacticraftlegacy.creative_only", "Creative Only");
        add("item.galacticraftlegacy.press_shift", "Press LSHIFT for more info");
        add("space_race.galacticraftlegacy.team_flag", "%s team's flag");

        add("gui.recipebook.toggleRecipes.heatable", "Showing Heatable");

        addBlock(GalacticraftBlocks.ALUMINUM_ORE, "Aluminum Ore");
        addBlock(GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE, "Deepslate Aluminum Ore");
        addBlock(GalacticraftBlocks.ALUMINUM_BLOCK, "Aluminum Block");
        addBlock(GalacticraftBlocks.RAW_ALUMINUM_BLOCK, "Raw Aluminum Block");
        addBlock(GalacticraftBlocks.TIN_ORE, "Tin Ore");
        addBlock(GalacticraftBlocks.DEEPSLATE_TIN_ORE, "Deepslate Tin Ore");
        addBlock(GalacticraftBlocks.TIN_BLOCK, "Tin Block");
        addBlock(GalacticraftBlocks.RAW_TIN_BLOCK, "Raw Tin Block");
        addBlock(GalacticraftBlocks.SILICON_ORE, "Silicon Ore");
        addBlock(GalacticraftBlocks.DEEPSLATE_SILICON_ORE, "Deepslate Silicon Ore");
        addBlock(GalacticraftBlocks.RAW_SILICON_BLOCK, "Raw Silicon Block");
        addBlock(GalacticraftBlocks.GRATING, "Grating");
        addBlock(GalacticraftBlocks.CHEESE, "Cheese");
        addBlock(GalacticraftBlocks.OXYGEN_DETECTOR, "Oxygen Detector");
        addBlock(GalacticraftBlocks.OIL, "Oil");
        addBlock(GalacticraftBlocks.FUEL, "Fuel");
        addBlock(GalacticraftBlocks.OIL_CAULDRON, "Oil Cauldron");
        addBlock(GalacticraftBlocks.FUEL_CAULDRON, "Fuel Cauldron");
        addWithDescription(GalacticraftBlocks.COAL_GENERATOR, "Coal Generator", "Coal generator will burn coal (or a Coal Block) for energy. The simplest but least efficient energy production method.");
        addWithDescription(GalacticraftBlocks.CIRCUIT_FABRICATOR, "Circuit Fabricator", "Circuit Fabricator will process basic materials into silicon wafers, used for advanced machines.");
        //addWithDescription(GalacticraftBlocks.COMPRESSOR, "Compressor", "Compressor will process ingots into their compressed equivalents. The most essential machine in Galacticraft!");
        //addWithDescription(GalacticraftBlocks.ELECTIRC_COMPRESSOR, "Compressor", "Electric Compressor will process ingots into their compressed equivalents. Compresses two at a time, making it more effective than its predecessor.");
        addWithDescription(GalacticraftBlocks.ELECTRIC_FURNACE, "Electric Furnace", "Electric Furnace is used as a faster alternative to traditional coal furnaces");
        //ddWithDescription(GalacticraftBlocks.ELECTRIC_ARC_FURNACE, "Electric Arc Furnace", "Electric Arc Furnace is used as a better and faster alternative to both traditional coal and electric furnaces: double output from ores!");
        addBlock(GalacticraftBlocks.PARACHEST, "Parachest");
        addBlock(GalacticraftBlocks.ALUMINUM_WIRE, "Aluminum Wire");
        addBlock(GalacticraftBlocks.HEAVY_ALUMINUM_WIRE, "Heavy Aluminum Wire");
        addBlock(GalacticraftBlocks.WHITE_PIPE, "White Pipe");
        addBlock(GalacticraftBlocks.ORANGE_PIPE, "Orange Pipe");
        addBlock(GalacticraftBlocks.MAGENTA_PIPE, "Magenta Pipe");
        addBlock(GalacticraftBlocks.LIGHT_BLUE_PIPE, "Light Blue Pipe");
        addBlock(GalacticraftBlocks.YELLOW_PIPE, "Yellow Pipe");
        addBlock(GalacticraftBlocks.LIME_PIPE, "Lime Pipe");
        addBlock(GalacticraftBlocks.PINK_PIPE, "Pink Pipe");
        addBlock(GalacticraftBlocks.GRAY_PIPE, "Gray Pipe");
        addBlock(GalacticraftBlocks.LIGHT_GRAY_PIPE, "Light Gray Pipe");
        addBlock(GalacticraftBlocks.CYAN_PIPE, "Cyan Pipe");
        addBlock(GalacticraftBlocks.PURPLE_PIPE, "Purple Pipe");
        addBlock(GalacticraftBlocks.BLUE_PIPE, "Blue Pipe");
        addBlock(GalacticraftBlocks.BROWN_PIPE, "Brown Pipe");
        addBlock(GalacticraftBlocks.GREEN_PIPE, "Green Pipe");
        addBlock(GalacticraftBlocks.RED_PIPE, "Red Pipe");
        addBlock(GalacticraftBlocks.BLACK_PIPE, "Black Pipe");

        addBlock(GalacticraftBlocks.MOON_DIRT, "Moon Dirt");
        addBlock(GalacticraftBlocks.MOON_TURF, "Moon Turf");
        addBlock(GalacticraftBlocks.MOON_ROCK, "Moon Rock");
        addBlock(GalacticraftBlocks.MOON_COPPER_ORE, "Moon Copper Ore");
        addBlock(GalacticraftBlocks.MOON_TIN_ORE, "Moon Tin Ore");
        addBlock(GalacticraftBlocks.MOON_SAPPHIRE_ORE, "Moon Sapphire Ore");
        addBlock(GalacticraftBlocks.MOON_BRICKS, "Moon Bricks");
        addBlock(GalacticraftBlocks.MOON_BRICK_STAIRS, "Moon Brick Stairs");
        addBlock(GalacticraftBlocks.MOON_BRICK_SLAB, "Moon Brick Slab");
        addBlock(GalacticraftBlocks.MOON_BRICK_WALL, "Moon Brick Wall");
        addBlock(GalacticraftBlocks.MOON_DUNGEON_CHEST, "Moon Dungeon Chest");
        addBlock(GalacticraftBlocks.MARS_DUNGEON_CHEST, "Mars Dungeon Chest");
        addBlock(GalacticraftBlocks.VENUS_DUNGEON_CHEST, "Venus Dungeon Chest");
        addBlock(GalacticraftBlocks.UNLIT_TORCH, "Unlit Torch");

        addEntityType(GalacticraftEntityType.FLAG, "Flag");
        addEntityType(GalacticraftEntityType.FALLING_PARACHEST, "Parachest");
        addEntityType(GalacticraftEntityType.SCHEMATIC, "Schematic");

        addItem(GalacticraftItems.BATTERY, "Battery");
        addItem(GalacticraftItems.INFINITE_BATTERY, "Battery");
        addItem(GalacticraftItems.DUNGEON_LOCATOR, "Dungeon Locator");
        addItem(GalacticraftItems.THERMAL_CLOTH, "Thermal Cloth");
        addItem(GalacticraftItems.ISOTHERMAL_FABRIC, "Isothermal Fabric");
        addItem(GalacticraftItems.THERMAL_PADDING_HELM, "Thermal Padding Helm");
        addItem(GalacticraftItems.ISOTHERMAL_HELM, "Isothermal Helm");
        addItem(GalacticraftItems.THERMAL_PADDING_CHESTPIECE, "Thermal Padding Chestpiece");
        addItem(GalacticraftItems.ISOTHERMAL_CHESTPIECE, "Isothermal Chestpiece");
        addItem(GalacticraftItems.THERMAL_PADDING_LEGGINGS, "Thermal Padding Leggings");
        addItem(GalacticraftItems.ISOTHERMAL_LEGGINGS, "Isothermal Leggings");
        addItem(GalacticraftItems.THERMAL_PADDING_BOOTS, "Thermal Padding Boots");
        addItem(GalacticraftItems.ISOTHERMAL_BOOTS, "Isothermal Boots");
        addItem(GalacticraftItems.LIGHT_TANK, "Light Oxygen Tank");
        addItem(GalacticraftItems.MEDIUM_TANK, "Medium Oxygen Tank");
        addItem(GalacticraftItems.HEAVY_TANK, "Heavy Oxygen Tank");
        addItem(GalacticraftItems.INFINITE_OXYGEN_TANK, "Oxygen Tank");
        addItem(GalacticraftItems.OXYGEN_MASK, "Oxygen Mask");
        addItem(GalacticraftItems.OXYGEN_GEAR, "Oxygen Gear");
        addItem(GalacticraftItems.BLACK_PARACHUTE, "Black Parachute");
        addItem(GalacticraftItems.BLUE_PARACHUTE,  "Blue Parachute");
        addItem(GalacticraftItems.BROWN_PARACHUTE,  "Brown Parachute");
        addItem(GalacticraftItems.CYAN_PARACHUTE,  "Cyan Parachute");
        addItem(GalacticraftItems.GRAY_PARACHUTE,  "Gray Parachute");
        addItem(GalacticraftItems.GREEN_PARACHUTE,  "Green Parachute");
        addItem(GalacticraftItems.LIGHT_BLUE_PARACHUTE,  "Light Blue Parachute");
        addItem(GalacticraftItems.LIGHT_GRAY_PARACHUTE,  "Light Gray Parachute");
        addItem(GalacticraftItems.LIME_PARACHUTE,  "Lime Parachute");
        addItem(GalacticraftItems.MAGENTA_PARACHUTE,  "Magenta Parachute");
        addItem(GalacticraftItems.ORANGE_PARACHUTE,  "Orange Parachute");
        addItem(GalacticraftItems.PINK_PARACHUTE,  "Pink Parachute");
        addItem(GalacticraftItems.PURPLE_PARACHUTE,  "Purple Parachute");
        addItem(GalacticraftItems.RED_PARACHUTE,  "Red Parachute");
        addItem(GalacticraftItems.WHITE_PARACHUTE,  "White Parachute");
        addItem(GalacticraftItems.YELLOW_PARACHUTE,  "Yellow Parachute");
        addItem(GalacticraftItems.PROTO_SHIELD_CONTROLLER, "Prototype Shield Controller");
        addWithDescription(GalacticraftItems.SHIELD_CONTROLLER, "Shield Controller", "Shield Controller protects armor from ALL forms of damage - including atmospheric corrosion!");
        addItem(GalacticraftItems.SENSOR_GLASSES, "Sensor Glasses");
        addItem(GalacticraftItems.WRENCH, "Standard Wrench");
        addItem(GalacticraftItems.ALUMINUM_INGOT, "Aluminum Ingot");
        addItem(GalacticraftItems.RAW_ALUMINUM, "Raw Aluminum");
        addItem(GalacticraftItems.TIN_INGOT, "Tin Ingot");
        addItem(GalacticraftItems.RAW_TIN, "Raw Tin");
        addItem(GalacticraftItems.RAW_SILICON, "Silicon");
        addItem(GalacticraftItems.SAPPHIRE, "Lunar Sapphire");
        addItem(GalacticraftItems.BASIC_WAFER, "Basic Wafer");
        addItem(GalacticraftItems.ADVANCED_WAFER, "Advanced Wafer");
        addItem(GalacticraftItems.SOLAR_WAFER, "Solar Wafer");
        addItem(GalacticraftItems.FLAG, "Flag");
        addItem(GalacticraftItems.SCHEMATIC, "Schematics");
        addItem(GalacticraftItems.FLUID_TANK, "Fluid Tank");
        addItem(GalacticraftItems.OIL_BUCKET, "Oil Bucket");
        addItem(GalacticraftItems.FUEL_BUCKET, "Fuel Bucket");
        addItem(GalacticraftItems.RAW_STEEL, "Raw Carbon-Iron");
        addItem(GalacticraftItems.STEEL_INGOT, "Heavy Duty Ingot");
        addItem(GalacticraftItems.STEEL_NUGGET, "Heavy Duty Nugget");
        addItem(GalacticraftItems.STEEL_SWORD, "Heavy Duty Sword");
        addItem(GalacticraftItems.STEEL_SPEAR, "Heavy Duty Spear");
        addItem(GalacticraftItems.STEEL_SHOVEL, "Heavy Duty Shovel");
        addItem(GalacticraftItems.STEEL_PICKAXE, "Heavy Duty Pickaxe");
        addItem(GalacticraftItems.STEEL_AXE, "Heavy Duty Axe");
        addItem(GalacticraftItems.STEEL_HOE, "Heavy Duty Hoe");
        addItem(GalacticraftItems.STEEL_HELMET, "Heavy Duty Helmet");
        addItem(GalacticraftItems.STEEL_CHESTPLATE, "Heavy Duty Chestplate");
        addItem(GalacticraftItems.STEEL_LEGGINGS, "Heavy Duty Leggings");
        addItem(GalacticraftItems.STEEL_BOOTS, "Heavy Duty Boots");
        addItem(GalacticraftItems.STEEL_HORSE_ARMOR, "Heavy Duty Horse Armor");
        addItem(GalacticraftItems.STEEL_NAUTILUS_ARMOR, "Heavy Duty Nautilus Armor");
        addItem(GalacticraftItems.RAW_DESH, "Raw Desh");
        addItem(GalacticraftItems.DESH_INGOT, "Desh Ingot");
        addItem(GalacticraftItems.DESH_NUGGET, "Desh Nugget");
        addItem(GalacticraftItems.DESH_SWORD, "Desh Sword");
        addItem(GalacticraftItems.DESH_SPEAR, "Desh Spear");
        addItem(GalacticraftItems.DESH_SHOVEL, "Desh Shovel");
        addItem(GalacticraftItems.DESH_PICKAXE, "Desh Pickaxe");
        addItem(GalacticraftItems.DESH_AXE, "Desh Axe");
        addItem(GalacticraftItems.DESH_HOE, "Desh Hoe");
        addItem(GalacticraftItems.DESH_HELMET, "Desh Helmet");
        addItem(GalacticraftItems.DESH_CHESTPLATE, "Desh Chestplate");
        addItem(GalacticraftItems.DESH_LEGGINGS, "Desh Leggings");
        addItem(GalacticraftItems.DESH_BOOTS, "Desh Boots");
        addItem(GalacticraftItems.RAW_TITANIUM, "Raw Titanium");
        addItem(GalacticraftItems.TITANIUM_INGOT, "Titanium Ingot");
        addItem(GalacticraftItems.TITANIUM_NUGGET, "Titanium Nugget");
        addItem(GalacticraftItems.TITANIUM_SWORD, "Titanium Sword");
        addItem(GalacticraftItems.TITANIUM_SPEAR, "Titanium Spear");
        addItem(GalacticraftItems.TITANIUM_SHOVEL, "Titanium Shovel");
        addItem(GalacticraftItems.TITANIUM_PICKAXE, "Titanium Pickaxe");
        addItem(GalacticraftItems.TITANIUM_AXE, "Titanium Axe");
        addItem(GalacticraftItems.TITANIUM_HOE, "Titanium Hoe");
        addItem(GalacticraftItems.TITANIUM_HELMET, "Titanium Helmet");
        addItem(GalacticraftItems.TITANIUM_CHESTPLATE, "Titanium Chestplate");
        addItem(GalacticraftItems.TITANIUM_LEGGINGS, "Titanium Leggings");
        addItem(GalacticraftItems.TITANIUM_BOOTS, "Titanium Boots");
        addItem(GalacticraftItems.RAW_LEAD, "Raw Lead");
        addItem(GalacticraftItems.LEAD_INGOT, "Lead Ingot");
        addItem(GalacticraftItems.LEAD_NUGGET, "Lead Nugget");
        addItem(GalacticraftItems.CHEESE_CHUNK, "Cheese Chunk");
        addItem(GalacticraftItems.CHEESE_SLICE, "Cheese Slice");
        addItem(GalacticraftItems.MOON_DUNGEON_KEY, "Moon Dungeon Key");
        addItem(GalacticraftItems.MARS_DUNGEON_KEY, "Mars Dungeon Key");
        addItem(GalacticraftItems.VENUS_DUNGEON_KEY, "Venus Dungeon Key");

        addKey(SchematicVariants.ASTRO_MINER, "title", "Astro Miner");
        addKey(SchematicVariants.CARGO_ROCKET, "title", "Cargo Rocket");
        addKey(SchematicVariants.MOON_BUGGY, "title", "Moon Buggy");
        addKey(SchematicVariants.TIER_2_ROCKET, "title", "Rocket Tier 2");
        addKey(SchematicVariants.TIER_3_ROCKET, "title", "Rocket Tier 3");

        add(GalacticraftTags.Blocks.MACHINE, "Machine");
        add(GalacticraftTags.Blocks.MACHINE_BASIC, "Basic machine");
        add(GalacticraftTags.Blocks.MACHINE_ADVANCED, "Advanced machine");
        add(GalacticraftTags.Blocks.SEALABLE, "Sealable blocks");
        add(GalacticraftTags.Blocks.SENSOR_GLASSES_DETECTABLE, "Detectable by Sensor Glasses");
        add(GalacticraftTags.EntityTypes.CAN_EQUIP_PARACHUTE, "Can equip parachute");
        add(GalacticraftTags.Fluids.FLOWS_GRATING, "Flows through Grating");
        add(GalacticraftTags.Items.PARACHUTE, "Parachute");
        add(GalacticraftTags.Items.WRENCH, "Wrench");
    }

    private <R extends ItemLike> void addWithDescription(Holder<R> entry, String name, String description) {
        add(entry.value().asItem(), name);
        add(entry.unwrapKey().orElseThrow().identifier().toLanguageKey("item", "desc"), description);
    }

    private <R> void addKey(ResourceKey<R> resourceKey, String suffix, String translation) {
        add(Constants.translatable(resourceKey, suffix), translation);
    }
}
