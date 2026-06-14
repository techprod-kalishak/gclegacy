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
        addAdvancement("galacticraft", "Galacticraft", "Explore the Moon... and beyond!");
        addAdvancement("coal_power", "Coal Power", "Craft a Coal Generator");
        addAdvancement("fabricated", "Fabricated", "Craft a circuit fabricator to make wafers");
        addAdvancement("wafers", "Tasty wafers", "Craft a basic wafer. (It's silicon so please don't eat it!)");
        addAdvancement("golden_wafers", "Golden wafers", "Craft an advanced wafer. (You still can't eat it!)");
        addAdvancement("compressed", "Compressed", "Craft a compressor");

        add("pack.galacticraftlegacy.description", "Galacticraft Legacy data pack");
        add("item.galacticraftlegacy.tank.tooltip", "Oxygen Remaining: ");
        add("galacticraftlegacy.networking_failed", "There was an error on the network thread: ");
        add("item.galacticraftlegacy.battery.tooltip", "Energy Stored: ");
        add("item.galacticraftlegact.fluid_tank.empty", "Tank is empty");
        add("item.galacticraftlegacy.fluid_tank.tooltip", "Fluid in tank %s: %s");
        add("itemGroup.galacticraftlegacy.blocks", "Galacticraft Legacy Blocks");
        add("itemGroup.galacticraftlegacy.items", "Galacticraft Legacy Items");
        add("galacticraftlegacy.space_travel.loading", "That's one small step for a player, one giant leap for the server");

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
        addBlock(GalacticraftBlocks.OIL, "Oil");
        addBlock(GalacticraftBlocks.FUEL, "Fuel");
        addBlock(GalacticraftBlocks.OIL_CAULDRON, "Oil Cauldron");
        addBlock(GalacticraftBlocks.FUEL_CAULDRON, "Fuel Cauldron");
        addWithDescription(GalacticraftBlocks.COAL_GENERATOR, "Coal Generator", "Coal generator will burn coal (or a Coal Block) for energy. The simplest but least efficient energy production method.");
        addWithDescription(GalacticraftBlocks.CIRCUIT_FABRICATOR, "Circuit Fabricator", "Circuit Fabricator will process basic materials into silicon wafers, used for advanced machines.");
        addWithDescription(GalacticraftBlocks.COMPRESSOR, "Compressor", "Compressor will process ingots into their compressed equivalents. The most essential machine in Galacticraft!");
        addWithDescription(GalacticraftBlocks.ELECTRIC_COMPRESSOR, "Electric Compressor", "Electric Compressor will process ingots into their compressed equivalents. Compresses two at a time, making it more effective than its predecessor.");
        addWithDescription(GalacticraftBlocks.ELECTRIC_FURNACE, "Electric Furnace", "Electric Furnace is used as a faster alternative to traditional coal furnaces");
        //ddWithDescription(GalacticraftBlocks.ELECTRIC_ARC_FURNACE, "Electric Arc Furnace", "Electric Arc Furnace is used as a better and faster alternative to both traditional coal and electric furnaces: double output from ores!");
        addWithDescription(GalacticraftBlocks.OXYGEN_COLLECTOR, "Oxygen Collector", "Oxygen Collector will store oxygen collected from leaves in the surrounding area.");
        //addWithDescription(GalacticraftBlocks.OXYGEN_BUBBLE_DISTRIBUTOR, "Oxygen Bubble Distributor", "Oxygen Bubble Distributor will emit an oxygen bubble outwards from the center block, using internal tank. This allows safe breathing inside the bubble.");
        //addWithDescription(GalacticraftBlocks.OXYGEN_COMPRESSOR, "Oxygen Compressor", "Oxygen Compressor will load oxygen from internal storage into an oxygen tank.");
        //addWithDescription(GalacticraftBlocks.OXYGEN_DECOMPRESSOR, "Oxygen Decompressor", "Oxygen Decompressor will unload oxygen into internal storage from an oxygen tank.");
        //addWithDescription(GalacticraftBlocks.OXYGEN_SEALER, "Oxygen Sealer", "Oxygen Sealer will check for an enclosed space. If the space is enclosed, it will fill with breathable air.");
        addWithDescription(GalacticraftBlocks.OXYGEN_DETECTOR, "Oxygen Detector", "Oxygen Detector will emit redstone signal when surrounded by oxygen.");
        //addWithDescription(GalacticraftBlocks.REFINERY, "Refinery", "Refinery will take an input of oil and energy, and output fuel used by rockets and vehicles.");
        //addWithDescription(GalacticraftBlocks.FUEL_LOADER, "Fuel Loader", "After being connected to fuel pad, a Fuel Loader will allow fuel to passed into the connected Rocket of vehicle.");
        //addWithDescription(GalacticraftBlocks.CARGO_LOADER, "Cargo Loader", "Cargo Loaders will pass blocks and items onto Rockets and other vehicles with cargo space. Must be attached to Landing/Fueling pad.");
        //addWithDescription(GalacticraftBlocks.CARGO_UNLOADER, "Cargo Unloader", "Cargo Unloaders will extract blocks and items from Rockets and other vehicles with cargo space. Must be attached to Landing/Fueling pad.");
        //addWithDescription(GalacticraftBlocks.COMPACT_NASA_WORKBENCH, "Compact NASA Workbench", "Compact variant of the NASA workbench.");
        //addWithDescription(GalacticraftBlocks.AIR_LOCK_FRAME, "Air Lock Frame", "Air Lock Frames, when arranged in a portal-like formation, with a single block replaced with a controller, will function as a working air lock.");
        //addWithDescription(GalacticraftBlocks.AIR_LOCK_CONTROLLER, "Air Lock Controller", "Air Lock Frames, when arranged in a portal-like formation, with a single block replaced with a controller, will function as a working air lock.");
        //addWithDescription(GalacticraftBlocks.CLEAR_VACCUM_GLASS, "Vaccum Glass (Clear)", "Clear glass for great views of space, can also provide a great view underwater");
        //addWithDescription(GalacticraftBlocks.FRAMED_CLEAR_VACCUM_GLASS, "Vaccum Glass (Clear)", "Clear glass for great views of space, can also provide a great view underwater");
        addWithDescription(GalacticraftBlocks.MAGNETIC_CRAFTING_TABLE, "Magnetic Crafting Table", "Items placed won't fall off - it holds its inventory! Can shift-click items in. Hoppers can insert and remove items, it will attempt to automatically create the last recipe crafted.");
        addBlock(GalacticraftBlocks.PARACHEST, "Parachest");
        addWithDescription(GalacticraftBlocks.ALUMINUM_WIRE, "Aluminum Wire", "Aluminum Wire is used to connect energy sources to energy consuming machines.");
        addWithDescription(GalacticraftBlocks.HEAVY_ALUMINUM_WIRE, "Heavy Aluminum Wire", "Heavy Aluminum Wire is used to connect energy sources to energy consuming machines. Its higher capacity boosts efficiency for Tier 2 machines.");
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
        addBlock(GalacticraftBlocks.UNLIT_COPPER_TORCH, "Unlit Copper Torch");
        addBlock(GalacticraftBlocks.UNLIT_LANTERN, "Unlit Lantern");
        addBlock(GalacticraftBlocks.UNLIT_COPPER_LANTERN.unaffected(), "Unlit Copper Lantern");
        addBlock(GalacticraftBlocks.UNLIT_COPPER_LANTERN.exposed(), "Unlit Exposed Copper Lantern");
        addBlock(GalacticraftBlocks.UNLIT_COPPER_LANTERN.weathered(), "Unlit Weathered Copper Lantern");
        addBlock(GalacticraftBlocks.UNLIT_COPPER_LANTERN.oxidized(), "Unlit Oxidized Copper Lantern");
        addBlock(GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxed(), "Unlit Waxed Copper Lantern");
        addBlock(GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxedExposed(), "Unlit Waxed Exposed Copper Lantern");
        addBlock(GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxedWeathered(), "Unlit Waxed Weathered Copper Lantern");
        addBlock(GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxedOxidized(), "Unlit Waxed Oxidized Copper Lantern");
        addBlock(GalacticraftBlocks.TIN_DECORATION_BLOCK, "Tin Decoration Block");
        addBlock(GalacticraftBlocks.TIN_DECORATION_CUT_BLOCK, "Cut Tin Decoration Block");
        addBlock(GalacticraftBlocks.TIN_DECORATION_SLAB, "Tin Decoration Slab");
        addBlock(GalacticraftBlocks.TIN_DECORATION_STAIRS, "Tin Decoration Stairs");
        addBlock(GalacticraftBlocks.TIN_DECORATION_WALL, "Tin Decoration Wall");
        addBlock(GalacticraftBlocks.SPACE_STATION, "Space Station Holder");
        addBlock(GalacticraftBlocks.ASTEROID_ROCK, "Asteroids Rock");
        addBlock(GalacticraftBlocks.ASTEROID_ROCK_SLAB, "Asteroids Rock Slab");
        addBlock(GalacticraftBlocks.ASTEROID_ROCK_STAIRS, "Asteroids Rock Stairs");
        addBlock(GalacticraftBlocks.ASTEROID_ROCK_WALL, "Asteroids Rock Wall");
        addBlock(GalacticraftBlocks.ASTEROID_ALUMINUM_ORE, "Asteroid Aluminum Ore");
        addWithDescription(GalacticraftBlocks.FALLEN_METEOR, "Fallen Meteor", "Fallen Meteors can be found in many planets/moons which have little or no atmosphere. Will drop Meteoric Iron when broken.");

        addEntityType(GalacticraftEntityType.FLAG, "Flag");
        addEntityType(GalacticraftEntityType.FALLING_PARACHEST, "Parachest");
        addEntityType(GalacticraftEntityType.SCHEMATIC, "Schematic");
        addEntityType(GalacticraftEntityType.EVOLVED_SKELETON, "Evolved Skeleton");
        addEntityType(GalacticraftEntityType.EVOLVED_ZOMBIE, "Evolved Zombie");

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
        addItem(GalacticraftItems.RAW_METEORIC_IRON, "Raw Meteoric Iron");
        addItem(GalacticraftItems.TIN_INGOT, "Tin Ingot");
        addItem(GalacticraftItems.RAW_TIN, "Raw Tin");
        addItem(GalacticraftItems.RAW_SILICON, "Silicon");
        addWithDescription(GalacticraftItems.SAPPHIRE, "Lunar Sapphire", "Lunar Sapphires are the unit of currency when trading with Alien Villagers");
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
        addItem(GalacticraftItems.DEHYDRATED_APPLE, "Dehydrated Apple");
        addItem(GalacticraftItems.DEHYDRATED_CARROT, "Dehydrated Carrot");
        addItem(GalacticraftItems.DEHYDRATED_MELON, "Dehydrated Melon");
        addItem(GalacticraftItems.DEHYDRATED_PUMPKIN, "Dehydrated Pumpkin");
        addItem(GalacticraftItems.DEHYDRATED_POTATO, "Dehydrated Potato");
        addItem(GalacticraftItems.DEHYDRATED_BEETROOT, "Dehydrated Beetroot");
        addItem(GalacticraftItems.CANNED_BEEF, "Canned Beef");
        addItem(GalacticraftItems.TIN_CANISTER, "Tin Canister");
        addItem(GalacticraftItems.COMPRESSED_ALUMINUM, "Compressed Aluminum");
        addItem(GalacticraftItems.COMPRESSED_BRONZE, "Compressed Bronze");
        addItem(GalacticraftItems.COMPRESSED_COPPER, "Compressed Copper");
        addItem(GalacticraftItems.COMPRESSED_DESH, "Compressed Desh");
        addItem(GalacticraftItems.COMPRESSED_IRON, "Compressed Iron");
        addItem(GalacticraftItems.COMPRESSED_METEORIC_IRON, "Compressed Meteoric Iron");
        addItem(GalacticraftItems.COMPRESSED_TIN, "Compressed Tin");
        addItem(GalacticraftItems.COMPRESSED_TITANIUM, "Compressed Titanium");
        addItem(GalacticraftItems.COMPRESSED_STEEL, "Compressed Steel");
        addItem(GalacticraftItems.HEAVY_DUTY_PLATE, "Heavy Duty Plate");
        addItem(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_2, "Heavy Duty Plate Tier 2");
        addItem(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_3, "Heavy Duty Plate Tier 3");

        addItem(GalacticraftItems.EVOLVED_SKELETON_SPAWN_EGG, "Evolved Skeleton Spawn Egg");
        addItem(GalacticraftItems.EVOLVED_ZOMBIE_SPAWN_EGG, "Evolved Zombie Spawn Egg");

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

        add("galacticraftlegacy.configuration.general", "General settings");
        add("galacticraftlegacy.configgui.common.debug_mode", "Debug Mode");
        add("galacticraftlegacy.configgui.common.dimensions_with_disabled_rockets", "Dimensions where rockets cannot launch");
        add("galacticraftlegacy.configgui.common.disable_returning_rockets", "Rockets cannot reach Overworld");
        add("galacticraftlegacy.configgui.common.force_overworld_respawn", "Always respawn in Overworld");
        add("galacticraftlegacy.configgui.common.disable_landers", "Always fall with parachute");

        add("galacticraftlegacy.configuration.environment", "Environmental settings");
        add("galacticraftlegacy.configuration.gui", "User interface settings");
        add("galacticraftlegacy.configgui.client.more_stars", "More stars");
        add("galacticraftlegacy.configgui.client.disable_rocket_particles", "Remove rocket particles");
        add("galacticraftlegacy.configgui.client.disable_vehicle_tpv", "Rocket does not change the camera view");
        add("galacticraftlegacy.configgui.client.energy_unit", "Energy unit");
        add("galacticraftlegacy.configgui.client.oxygen_tanks_pos", "Position of oxygen tanks in UI");
        add("galacticraftlegacy.configgui.client.icons_rotation", "Moving celestial icons");
        add("galacticraftlegacy.configuration.accessibility", "Accessibility settings");
        add("galacticraftlegacy.configgui.client.scroll_sensitivity", "Scroll sensitivity");
        add("galacticraftlegacy.configgui.client.invert_scroll", "Scroll inversion");
        add("galacticraftlegacy.configgui.client.space_race_popup", "Space race pops-up");

        add("galacticraftlegacy.configgui.server.world_borders", "Default world borders");
        add("galacticraftlegacy.configgui.server.space_stations_permissions", "Space station requires invitation");
        add("galacticraftlegacy.configgui.server.disable_space_station_creation", "Disable creation of space stations");
        add("galacticraftlegacy.configgui.server.override_capes", "Override patron capes");
        add("galacticraftlegacy.configgui.server.seal_edge_check", "Oxygen sealers checks");
        add("galacticraftlegacy.configuration.difficulty", "Environmental settings");
        add("galacticraftlegacy.configgui.server.disable_spaceship_explosion", "Rockets can explode");
        add("galacticraftlegacy.configgui.server.disable_meteor_block_breaking", "Falling meteors don't break blocks");
        add("galacticraftlegacy.configgui.server.meteor_spawn_multiplier", "Multiplier of spawned meteors");
        add("galacticraftlegacy.configgui.server.solar_energy_multiplier", "Multiplier of generated energy from solar panels");
        add("galacticraftlegacy.configgui.server.fuel_usage_multiplier", "Multiplier of fuel consumption");
        add("galacticraftlegacy.configgui.server.quick_mode", "Quick mode");
        add("galacticraftlegacy.configgui.server.hard_mode", "Hard mode");
        add("galacticraftlegacy.configgui.server.adventure_mode", "Adventure mode");
        add("galacticraftlegacy.configgui.server.adventure_mode_flags", "Adventure mode flags");
        add("galacticraftlegacy.configuration.entities", "Entities settings");
        add("galacticraftlegacy.configgui.server.suffocation_damage", "Amount of suffocation damage");
        add("galacticraftlegacy.configgui.server.boss_health_modifier", "Increase bosses' health");
    }

    private <R extends ItemLike> void addWithDescription(Holder<R> entry, String name, String description) {
        add(entry.value().asItem(), name);
        add(entry.unwrapKey().orElseThrow().identifier().toLanguageKey("item", "desc"), description);
    }

    private <R> void addKey(ResourceKey<R> resourceKey, String suffix, String translation) {
        add(Constants.translatable(resourceKey, suffix), translation);
    }

    private void addAdvancement(String id, String title, String description) {
        add("advancements.galacticraftlegacy." + id + ".title", title);
        add("advancements.galacticraftlegacy." + id + ".description", description);
    }
}
