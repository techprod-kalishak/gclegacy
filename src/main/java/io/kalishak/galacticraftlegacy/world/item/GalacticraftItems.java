/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredItemRegister;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredWeatheringCopperItems;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.ItemAccessFluidTank;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.component.*;
import io.kalishak.galacticraftlegacy.world.item.equipment.GalacticraftArmorMaterials;
import io.kalishak.galacticraftlegacy.world.item.equipment.trim.GalacticraftTrimMaterials;
import io.kalishak.galacticraftlegacy.world.item.gear.FluidTankItem;
import io.kalishak.galacticraftlegacy.world.item.gear.GearItem;
import io.kalishak.galacticraftlegacy.world.item.gear.OxygenTankItem;
import io.kalishak.galacticraftlegacy.world.item.gear.ShieldControllerItem;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.transfer.InfiniteResourceHandler;
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler;

public final class GalacticraftItems {
    private static final DeferredItemRegister REGISTRY = new DeferredItemRegister(Galacticraft.MODID);

    public static final DeferredItem<BatteryItem> BATTERY = REGISTRY.registerItem(
            "battery",
            BatteryItem::new,
            properties -> properties.stacksTo(1)
    );
    public static final DeferredItem<Item> INFINITE_BATTERY = REGISTRY.registerSimpleItem(
            "infinite_battery",
            properties -> properties
                    .stacksTo(1)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );
    public static final DeferredItem<Item> THERMAL_CLOTH = REGISTRY.registerSimpleItem("thermal_cloth");
    public static final DeferredItem<Item> ISOTHERMAL_FABRIC = REGISTRY.registerSimpleItem("isothermal_fabric");
    public static final DeferredItem<GearItem> THERMAL_PADDING_HELM = REGISTRY.registerItem(
            "thermal_cap",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_CAP, GearEquipmentAssets.THERMAL)
                    .component(GalacticraftDataComponents.TEMPERATURE_MODIFIER, -1.0F)
    );
    public static final DeferredItem<GearItem> THERMAL_PADDING_CHESTPIECE = REGISTRY.registerItem(
            "thermal_shirt",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SHIRT, GearEquipmentAssets.THERMAL)
                    .component(GalacticraftDataComponents.TEMPERATURE_MODIFIER, -1.0F)
    );
    public static final DeferredItem<GearItem> THERMAL_PADDING_LEGGINGS = REGISTRY.registerItem(
            "thermal_leggings",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_LEGGINGS, GearEquipmentAssets.THERMAL)
                    .component(GalacticraftDataComponents.TEMPERATURE_MODIFIER, -1.0F)
    );
    public static final DeferredItem<GearItem> THERMAL_PADDING_BOOTS = REGISTRY.registerItem(
            "thermal_socks",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SOCKS, GearEquipmentAssets.THERMAL)
                    .component(GalacticraftDataComponents.TEMPERATURE_MODIFIER, -1.0F)
    );
    public static final DeferredItem<GearItem> ISOTHERMAL_HELM = REGISTRY.registerItem(
            "isothermal_cap",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_CAP, GearEquipmentAssets.ISOTHERMAL)
                    .component(GalacticraftDataComponents.TEMPERATURE_MODIFIER, 1.0F)
    );
    public static final DeferredItem<GearItem> ISOTHERMAL_CHESTPIECE = REGISTRY.registerItem(
            "isothermal_shirt",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SHIRT, GearEquipmentAssets.ISOTHERMAL)
                    .component(GalacticraftDataComponents.TEMPERATURE_MODIFIER, 1.0F)
    );
    public static final DeferredItem<GearItem> ISOTHERMAL_LEGGINGS = REGISTRY.registerItem(
            "isothermal_leggings",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_LEGGINGS, GearEquipmentAssets.ISOTHERMAL)
                    .component(GalacticraftDataComponents.TEMPERATURE_MODIFIER, 1.0F)
    );
    public static final DeferredItem<GearItem> ISOTHERMAL_BOOTS = REGISTRY.registerItem(
            "isothermal_socks",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SOCKS, GearEquipmentAssets.ISOTHERMAL)
                    .component(GalacticraftDataComponents.TEMPERATURE_MODIFIER, 1.0F)
    );
    public static final DeferredItem<GearItem> LIGHT_TANK = REGISTRY.registerItem(
            "light_oxygen_tank",
            OxygenTankItem::new,
            () -> GearItem.tankProperties(GearEquipmentAssets.LIGHT_TANK)
    );
    public static final DeferredItem<GearItem> MEDIUM_TANK = REGISTRY.registerItem(
            "medium_oxygen_tank",
            OxygenTankItem::new,
            () -> GearItem.tankProperties(GearEquipmentAssets.MEDIUM_TANK)
    );
    public static final DeferredItem<GearItem> HEAVY_TANK = REGISTRY.registerItem(
            "heavy_oxygen_tank",
            OxygenTankItem::new,
            () -> GearItem.tankProperties(GearEquipmentAssets.HEAVY_TANK)
    );
    public static final DeferredItem<GearItem> INFINITE_OXYGEN_TANK = REGISTRY.registerItem(
            "infinite_oxygen_tank",
            GearItem::new,
            () -> GearItem.tankProperties(GearEquipmentAssets.HEAVY_TANK)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );
    public static final DeferredItem<GearItem> OXYGEN_MASK = REGISTRY.registerItem(
            "oxygen_mask",
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.MASK)
    );
    public static final DeferredItem<GearItem> OXYGEN_GEAR = REGISTRY.registerItem(
            "oxygen_gear",
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.GEAR)
    );
    public static final DeferredItem<Item> WRENCH = REGISTRY.registerSimpleItem(
            "wrench",
            properties -> properties.durability(256)
    );
    public static final DeferredItem<GearItem> BLACK_PARACHUTE = REGISTRY.registerItem(
            "black_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.BLACK)
    );
    public static final DeferredItem<GearItem> BLUE_PARACHUTE = REGISTRY.registerItem(
            "blue_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.BLUE)
    );
    public static final DeferredItem<GearItem> BROWN_PARACHUTE = REGISTRY.registerItem(
            "brown_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.BROWN)
    );
    public static final DeferredItem<GearItem> CYAN_PARACHUTE = REGISTRY.registerItem(
            "cyan_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.CYAN)
    );
    public static final DeferredItem<GearItem> GRAY_PARACHUTE = REGISTRY.registerItem(
            "gray_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.GRAY)
    );
    public static final DeferredItem<GearItem> GREEN_PARACHUTE = REGISTRY.registerItem(
            "green_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.GREEN)
    );
    public static final DeferredItem<GearItem> LIGHT_BLUE_PARACHUTE = REGISTRY.registerItem(
            "light_blue_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.LIGHT_BLUE)
    );
    public static final DeferredItem<GearItem> LIGHT_GRAY_PARACHUTE = REGISTRY.registerItem(
            "light_gray_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.LIGHT_GRAY)
    );
    public static final DeferredItem<GearItem> LIME_PARACHUTE = REGISTRY.registerItem(
            "lime_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.LIME)
    );
    public static final DeferredItem<GearItem> MAGENTA_PARACHUTE = REGISTRY.registerItem(
            "magenta_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.MAGENTA)
    );
    public static final DeferredItem<GearItem> ORANGE_PARACHUTE = REGISTRY.registerItem(
            "orange_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.ORANGE)
    );
    public static final DeferredItem<GearItem> PINK_PARACHUTE = REGISTRY.registerItem(
            "pink_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.PINK)
    );
    public static final DeferredItem<GearItem> PURPLE_PARACHUTE = REGISTRY.registerItem(
            "purple_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.PURPLE)
    );
    public static final DeferredItem<GearItem> RED_PARACHUTE = REGISTRY.registerItem(
            "red_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.RED)
    );
    public static final DeferredItem<GearItem> WHITE_PARACHUTE = REGISTRY.registerItem(
            "white_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.WHITE)
    );
    public static final DeferredItem<GearItem> YELLOW_PARACHUTE = REGISTRY.registerItem(
            "yellow_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.YELLOW)
    );
    public static final DeferredItem<GearItem> PROTO_SHIELD_CONTROLLER = REGISTRY.registerItem(
            "proto_shield_controller",
            ShieldControllerItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.SHIELD)
                    .stacksTo(1)
                    .component(GalacticraftDataComponents.SHIELD_CONTROLLER, new ShieldController(300))
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .component(GalacticraftDataComponents.ITEM_WITH_DESCRIPTION, new ItemWithDescription(Constants.id("shield_controller").toLanguageKey("item", "desc")))
    );
    public static final DeferredItem<GearItem> SHIELD_CONTROLLER = REGISTRY.registerItemWithDescription(
            "shield_controller",
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.SHIELD)
    );
    public static final DeferredItem<Item> SENSOR_GLASSES = REGISTRY.registerSimpleItem(
            "sensor_glasses",
            properties -> properties
                    .stacksTo(1)
                    .component(
                            DataComponents.EQUIPPABLE,
                            Equippable.builder(EquipmentSlot.HEAD)
                                    .setAllowedEntities(EntityType.PLAYER)
                                    .setAsset(GearEquipmentAssets.SENSOR_GLASSES).build()
                    )
    );
    public static final DeferredItem<StructureFinderItem> DUNGEON_LOCATOR = REGISTRY.registerItem(
            "dungeon_locator",
            StructureFinderItem::new,
            properties -> properties.stacksTo(1)
    );
    public static final DeferredItem<Item> ALUMINUM_INGOT = REGISTRY.registerSimpleItem("aluminum_ingot");
    public static final DeferredItem<Item> RAW_ALUMINUM = REGISTRY.registerSimpleItem("raw_aluminum");
    public static final DeferredItem<Item> TIN_INGOT = REGISTRY.registerSimpleItem("tin_ingot");
    public static final DeferredItem<Item> RAW_TIN = REGISTRY.registerSimpleItem("raw_tin");
    public static final DeferredItem<Item> RAW_SILICON = REGISTRY.registerSimpleItem("raw_silicon");
    public static final DeferredItem<Item> SAPPHIRE = REGISTRY.registerSimpleItem("sapphire", properties -> properties.rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> BASIC_WAFER = REGISTRY.registerSimpleItem("basic_wafer");
    public static final DeferredItem<Item> ADVANCED_WAFER = REGISTRY.registerSimpleItem("advanced_wafer");
    public static final DeferredItem<Item> SOLAR_WAFER = REGISTRY.registerSimpleItem("solar_wafer");
    public static final DeferredItem<Item> FLAG = REGISTRY.registerItem("flag", FlagItem::new, properties -> properties.stacksTo(1));
    public static final DeferredItem<SchematicItem> SCHEMATIC = REGISTRY.registerItem(
            "schematic",
            SchematicItem::new,
            properties -> properties
                    .stacksTo(1)
                    .delayedHolderComponent(GalacticraftDataComponents.SCHEMATIC.get(), SchematicVariants.TIER_2_ROCKET)
    );
    public static final DeferredItem<FluidTankItem> FLUID_TANK = REGISTRY.registerItem(
            "fluid_tank",
            FluidTankItem::new,
            () -> new Item.Properties()
                    .stacksTo(1)
                    .component(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY)
    );
    public static final DeferredItem<BucketItem> OIL_BUCKET = REGISTRY.registerItem(
            "oil_bucket",
            properties -> new BucketItem(GalacticraftFluids.OIL.value(), properties)
    );
    public static final DeferredItem<BucketItem> FUEL_BUCKET = REGISTRY.registerItem(
            "fuel_bucket",
            properties -> new BucketItem(GalacticraftFluids.FUEL.value(), properties)
    );

    public static final DeferredItem<Item> RAW_STEEL = REGISTRY.registerSimpleItem("raw_steel");
    public static final DeferredItem<Item> STEEL_INGOT = REGISTRY.registerSimpleItem(
            "steel_ingot",
            properties -> properties.trimMaterial(GalacticraftTrimMaterials.STEEL)
    );
    public static final DeferredItem<Item> STEEL_NUGGET = REGISTRY.registerSimpleItem("steel_nugget");
    //STEEL ARMOR & TOOL SET
    public static final DeferredItem<Item> STEEL_HELMET = REGISTRY.registerSimpleItem(
            "steel_helmet",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.STEEL, ArmorType.HELMET)
    );
    public static final DeferredItem<Item> STEEL_CHESTPLATE = REGISTRY.registerSimpleItem(
            "steel_chestplate",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.STEEL, ArmorType.CHESTPLATE)
    );
    public static final DeferredItem<Item> STEEL_LEGGINGS = REGISTRY.registerSimpleItem(
            "steel_leggings",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.STEEL, ArmorType.LEGGINGS)
    );
    public static final DeferredItem<Item> STEEL_BOOTS = REGISTRY.registerSimpleItem(
            "steel_boots",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.STEEL, ArmorType.BOOTS)
    );
    public static final DeferredItem<Item> STEEL_HORSE_ARMOR = REGISTRY.registerSimpleItem(
            "steel_horse_armor",
            properties -> properties.horseArmor(GalacticraftArmorMaterials.STEEL)
    );
    public static final DeferredItem<Item> STEEL_NAUTILUS_ARMOR = REGISTRY.registerSimpleItem(
            "steel_nautilus_armor",
            properties -> properties.nautilusArmor(GalacticraftArmorMaterials.STEEL)
    );
    public static final DeferredItem<Item> STEEL_SWORD = REGISTRY.registerSimpleItem(
            "steel_sword",
            properties -> properties.sword(GalacticraftToolMaterials.STEEL, 3.0F, -2.4F)
    );
    //TODO Balance Steel Spear
    public static final DeferredItem<Item> STEEL_SPEAR = REGISTRY.registerSimpleItem(
            "steel_spear",
            properties -> properties.spear(GalacticraftToolMaterials.STEEL, 0.95F, 0.95F, 0.6F, 2.5F, 8.0F, 6.75F, 5.1F, 11.25F, 4.6F)
    );
    public static final DeferredItem<ShovelItem> STEEL_SHOVEL = REGISTRY.registerItem(
            "steel_shovel",
            properties -> new ShovelItem(GalacticraftToolMaterials.STEEL, 3.0F, -2.4F, properties)
    );
    public static final DeferredItem<Item> STEEL_PICKAXE = REGISTRY.registerSimpleItem(
            "steel_pickaxe",
            properties -> properties.pickaxe(GalacticraftToolMaterials.STEEL, 1.0F, -2.8F)
    );
    public static final DeferredItem<AxeItem> STEEL_AXE = REGISTRY.registerItem(
            "steel_axe",
            properties -> new AxeItem(GalacticraftToolMaterials.STEEL, 3.0F, -3.0F, properties)
    );
    public static final DeferredItem<HoeItem> STEEL_HOE = REGISTRY.registerItem(
            "steel_hoe",
            properties -> new HoeItem(GalacticraftToolMaterials.STEEL, -4.0F, 0.0F, properties)
    );

    public static final DeferredItem<Item> RAW_DESH = REGISTRY.registerSimpleItem("raw_desh");
    public static final DeferredItem<Item> DESH_INGOT = REGISTRY.registerSimpleItem(
            "desh_ingot",
            properties -> properties.trimMaterial(GalacticraftTrimMaterials.DESH)
    );
    public static final DeferredItem<Item> DESH_NUGGET = REGISTRY.registerSimpleItem("desh_nugget");
    //DESH ARMOR & TOOL SET
    public static final DeferredItem<Item> DESH_HELMET = REGISTRY.registerSimpleItem(
            "desh_helmet",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.DESH, ArmorType.HELMET)
    );
    public static final DeferredItem<Item> DESH_CHESTPLATE = REGISTRY.registerSimpleItem(
            "desh_chestplate",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.DESH, ArmorType.CHESTPLATE)
    );
    public static final DeferredItem<Item> DESH_LEGGINGS = REGISTRY.registerSimpleItem(
            "desh_leggings",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.DESH, ArmorType.LEGGINGS)
    );
    public static final DeferredItem<Item> DESH_BOOTS = REGISTRY.registerSimpleItem(
            "desh_boots",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.DESH, ArmorType.BOOTS)
    );
    public static final DeferredItem<Item> DESH_SWORD = REGISTRY.registerSimpleItem(
            "desh_sword",
            properties -> properties.sword(GalacticraftToolMaterials.DESH, 3.0F, -2.4F)
    );
    //TODO Balance Desh Spear
    public static final DeferredItem<Item> DESH_SPEAR = REGISTRY.registerSimpleItem(
            "desh_spear",
            properties -> properties.spear(GalacticraftToolMaterials.DESH, 0.95F, 0.95F, 0.6F, 2.5F, 8.0F, 6.75F, 5.1F, 11.25F, 4.6F)
    );
    public static final DeferredItem<ShovelItem> DESH_SHOVEL = REGISTRY.registerItem(
            "desh_shovel",
            properties -> new ShovelItem(GalacticraftToolMaterials.DESH, 3.0F, -2.4F, properties)
    );
    public static final DeferredItem<Item> DESH_PICKAXE = REGISTRY.registerSimpleItem(
            "desh_pickaxe",
            properties -> properties.pickaxe(GalacticraftToolMaterials.DESH, 1.0F, -2.8F)
    );
    public static final DeferredItem<AxeItem> DESH_AXE = REGISTRY.registerItem(
            "desh_axe",
            properties -> new AxeItem(GalacticraftToolMaterials.DESH, 3.0F, -3.0F, properties)
    );
    public static final DeferredItem<HoeItem> DESH_HOE = REGISTRY.registerItem(
            "desh_hoe",
            properties -> new HoeItem(GalacticraftToolMaterials.DESH, -4.0F, 0.0F, properties)
    );

    public static final DeferredItem<Item> RAW_TITANIUM = REGISTRY.registerSimpleItem("raw_titanium");
    public static final DeferredItem<Item> TITANIUM_INGOT = REGISTRY.registerSimpleItem(
            "titanium_ingot",
            properties -> properties.trimMaterial(GalacticraftTrimMaterials.TITANIUM)
    );
    public static final DeferredItem<Item> TITANIUM_NUGGET = REGISTRY.registerSimpleItem("titanium_nugget");
    //TITANIUM ARMOR & TOOL SET
    //TODO change wooden stick to desh
    public static final DeferredItem<Item> TITANIUM_HELMET = REGISTRY.registerSimpleItem(
            "titanium_helmet",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.TITANIUM, ArmorType.HELMET)
    );
    public static final DeferredItem<Item> TITANIUM_CHESTPLATE = REGISTRY.registerSimpleItem(
            "titanium_chestplate",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.TITANIUM, ArmorType.CHESTPLATE)
    );
    public static final DeferredItem<Item> TITANIUM_LEGGINGS = REGISTRY.registerSimpleItem(
            "titanium_leggings",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.TITANIUM, ArmorType.LEGGINGS)
    );
    public static final DeferredItem<Item> TITANIUM_BOOTS = REGISTRY.registerSimpleItem(
            "titanium_boots",
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.TITANIUM, ArmorType.BOOTS)
    );
    public static final DeferredItem<Item> TITANIUM_SWORD = REGISTRY.registerSimpleItem(
            "titanium_sword",
            properties -> properties.sword(GalacticraftToolMaterials.TITANIUM, 3.0F, -2.4F)
    );
    //TODO Balance Titanium Spear
    public static final DeferredItem<Item> TITANIUM_SPEAR = REGISTRY.registerSimpleItem(
            "titanium_spear",
            properties -> properties.spear(GalacticraftToolMaterials.TITANIUM, 0.95F, 0.95F, 0.6F, 2.5F, 8.0F, 6.75F, 5.1F, 11.25F, 4.6F)
    );
    public static final DeferredItem<ShovelItem> TITANIUM_SHOVEL = REGISTRY.registerItem(
            "titanium_shovel",
            properties -> new ShovelItem(GalacticraftToolMaterials.TITANIUM, 3.0F, -2.4F, properties)
    );
    public static final DeferredItem<Item> TITANIUM_PICKAXE = REGISTRY.registerSimpleItem(
            "titanium_pickaxe",
            properties -> properties.pickaxe(GalacticraftToolMaterials.TITANIUM, 1.0F, -2.8F)
    );
    public static final DeferredItem<AxeItem> TITANIUM_AXE = REGISTRY.registerItem(
            "titanium_axe",
            properties -> new AxeItem(GalacticraftToolMaterials.TITANIUM, 3.0F, -3.0F, properties)
    );
    public static final DeferredItem<HoeItem> TITANIUM_HOE = REGISTRY.registerItem(
            "titanium_hoe",
            properties -> new HoeItem(GalacticraftToolMaterials.TITANIUM, -4.0F, 0.0F, properties)
    );
    public static final DeferredItem<Item> RAW_LEAD = REGISTRY.registerSimpleItem("raw_lead");
    public static final DeferredItem<Item> LEAD_INGOT = REGISTRY.registerSimpleItem(
            "lead_ingot",
            properties -> properties.trimMaterial(GalacticraftTrimMaterials.LEAD)
    );
    public static final DeferredItem<Item> LEAD_NUGGET = REGISTRY.registerSimpleItem("lead_nugget");
    public static final DeferredItem<Item> CHEESE_CHUNK = REGISTRY.registerSimpleItem(
            "cheese_chunk",
            properties -> properties
                    .trimMaterial(GalacticraftTrimMaterials.CHEESE)
                    .food(GalacticraftFoods.CHEESE)
    );
    public static final DeferredItem<Item> CHEESE_SLICE = REGISTRY.registerSimpleItem(
            "cheese_slice",
            properties -> properties.food(GalacticraftFoods.CHEESE)
    );
    public static final DeferredItem<Item> MOON_DUNGEON_KEY = REGISTRY.registerSimpleItem(
            "moon_dungeon_key",
            properties -> properties.stacksTo(1).component(GalacticraftDataComponents.KEY_LOCK, KeyLock.preGenTier(FeatureTier.TIER_1))
    );
    public static final DeferredItem<Item> MARS_DUNGEON_KEY = REGISTRY.registerSimpleItem(
            "mars_dungeon_key",
            properties -> properties.stacksTo(1).component(GalacticraftDataComponents.KEY_LOCK, KeyLock.preGenTier(FeatureTier.TIER_2))
    );
    public static final DeferredItem<Item> VENUS_DUNGEON_KEY = REGISTRY.registerSimpleItem(
            "venus_dungeon_key",
            properties -> properties.stacksTo(1).component(GalacticraftDataComponents.KEY_LOCK, KeyLock.preGenTier(FeatureTier.TIER_3))
    );
    public static final DeferredItem<Item> TIN_CANISTER = REGISTRY.registerSimpleItem(
            "tin_canister"
    );
    public static final DeferredItem<Item> DEHYDRATED_APPLE = REGISTRY.registerSimpleItem(
            "dehydrated_apple",
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_APPLE)
    );
    public static final DeferredItem<Item> DEHYDRATED_CARROT = REGISTRY.registerSimpleItem(
            "dehydrated_carrot",
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_CARROT)
    );
    public static final DeferredItem<Item> DEHYDRATED_MELON = REGISTRY.registerSimpleItem(
            "dehydrated_melon",
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_MELON)
    );
    public static final DeferredItem<Item> DEHYDRATED_PUMPKIN = REGISTRY.registerSimpleItem(
            "dehydrated_pumpkin",
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_PUMPKIN)
    );
    public static final DeferredItem<Item> DEHYDRATED_POTATO = REGISTRY.registerSimpleItem(
            "dehydrated_potato",
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_POTATO)
    );
    public static final DeferredItem<Item> DEHYDRATED_BEETROOT = REGISTRY.registerSimpleItem(
            "dehydrated_beetroot",
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_BEET)
    );
    public static final DeferredItem<Item> CANNED_BEEF = REGISTRY.registerSimpleItem(
            "canned_beef",
            () -> CannedFood.createProperties(GalacticraftFoods.CANNED_BEEF)
    );
    public static final DeferredItem<SpawnEggItem> EVOLVED_SKELETON_SPAWN_EGG = REGISTRY.registerItem(
            "evolved_skeleton_spawn_egg",
            SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(GalacticraftEntityType.EVOLVED_SKELETON.get())
    );
    public static final DeferredItem<SpawnEggItem> EVOLVED_ZOMBIE_SPAWN_EGG = REGISTRY.registerItem(
            "evolved_zombie_spawn_egg",
            SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(GalacticraftEntityType.EVOLVED_ZOMBIE.get())
    );

    public static final DeferredItem<BlockItem> ALUMINUM_WIRE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ALUMINUM_WIRE);
    public static final DeferredItem<BlockItem> HEAVY_ALUMINUM_WIRE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.HEAVY_ALUMINUM_WIRE);
    public static final DeferredItem<BlockItem> WHITE_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.WHITE_PIPE);
    public static final DeferredItem<BlockItem> ORANGE_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ORANGE_PIPE);
    public static final DeferredItem<BlockItem> MAGENTA_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MAGENTA_PIPE);
    public static final DeferredItem<BlockItem> LIGHT_BLUE_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.LIGHT_BLUE_PIPE);
    public static final DeferredItem<BlockItem> YELLOW_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.YELLOW_PIPE);
    public static final DeferredItem<BlockItem> LIME_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.LIME_PIPE);
    public static final DeferredItem<BlockItem> PINK_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.PINK_PIPE);
    public static final DeferredItem<BlockItem> GRAY_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.GRAY_PIPE);
    public static final DeferredItem<BlockItem> LIGHT_GRAY_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.LIGHT_GRAY_PIPE);
    public static final DeferredItem<BlockItem> CYAN_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.CYAN_PIPE);
    public static final DeferredItem<BlockItem> PURPLE_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.PURPLE_PIPE);
    public static final DeferredItem<BlockItem> BLUE_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.BLUE_PIPE);
    public static final DeferredItem<BlockItem> BROWN_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.BROWN_PIPE);
    public static final DeferredItem<BlockItem> GREEN_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.GREEN_PIPE);
    public static final DeferredItem<BlockItem> RED_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.RED_PIPE);
    public static final DeferredItem<BlockItem> BLACK_PIPE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.BLACK_PIPE);
    public static final DeferredItem<BlockItem> GRATING = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.GRATING);
    public static final DeferredItem<BlockItem> OXYGEN_DETECTOR = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.OXYGEN_DETECTOR);
    public static final DeferredItem<BlockItem> CHEESE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.CHEESE);
    public static final DeferredItem<BlockItem> COAL_GENERATOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.COAL_GENERATOR);
    public static final DeferredItem<BlockItem> CIRCUIT_FABRICATOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.CIRCUIT_FABRICATOR);
    public static final DeferredItem<BlockItem> COMPRESSOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.COMPRESSOR);
    public static final DeferredItem<BlockItem> ELECTRIC_COMPRESSOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.ELECTRIC_COMPRESSOR);
    public static final DeferredItem<BlockItem> ELECTRIC_FURNACE = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.ELECTRIC_FURNACE);
    public static final DeferredItem<BlockItem> MOON_DIRT = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_DIRT);
    public static final DeferredItem<BlockItem> MOON_TURF = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_TURF);
    public static final DeferredItem<BlockItem> MOON_ROCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_ROCK);
    public static final DeferredItem<BlockItem> MOON_CHEESE_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_CHEESE_ORE);
    public static final DeferredItem<BlockItem> MOON_COPPER_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_COPPER_ORE);
    public static final DeferredItem<BlockItem> MOON_TIN_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_TIN_ORE);
    public static final DeferredItem<BlockItem> MOON_SAPPHIRE_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_SAPPHIRE_ORE);
    public static final DeferredItem<BlockItem> MOON_BRICKS = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_BRICKS);
    public static final DeferredItem<BlockItem> MOON_BRICK_STAIRS = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> MOON_BRICK_SLAB = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_BRICK_SLAB);
    public static final DeferredItem<BlockItem> MOON_BRICK_WALL = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_BRICK_WALL);
    public static final DeferredItem<BlockItem> MOON_DUNGEON_CHEST = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MOON_DUNGEON_CHEST);
    public static final DeferredItem<BlockItem> MARS_DUNGEON_CHEST = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.MARS_DUNGEON_CHEST);
    public static final DeferredItem<BlockItem> VENUS_DUNGEON_CHEST = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.VENUS_DUNGEON_CHEST);
    public static final DeferredItem<BlockItem> ALUMINUM_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ALUMINUM_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_ALUMINUM_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE);
    public static final DeferredItem<BlockItem> RAW_ALUMINUM_BLOCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.RAW_ALUMINUM_BLOCK);
    public static final DeferredItem<BlockItem> ALUMINUM_BLOCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ALUMINUM_BLOCK);
    public static final DeferredItem<BlockItem> TIN_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.TIN_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_TIN_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.DEEPSLATE_TIN_ORE);
    public static final DeferredItem<BlockItem> RAW_TIN_BLOCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.RAW_TIN_BLOCK);
    public static final DeferredItem<BlockItem> TIN_BLOCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.TIN_BLOCK);
    public static final DeferredItem<BlockItem> SILICON_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.SILICON_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_SILICON_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.DEEPSLATE_SILICON_ORE);
    public static final DeferredItem<BlockItem> RAW_SILICON_BLOCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.RAW_SILICON_BLOCK);
    public static final DeferredItem<StandingAndWallBlockItem> UNLIT_TORCH = REGISTRY.registerBlockItem(
            GalacticraftBlocks.UNLIT_TORCH,
            (standingBlock, properties) -> new StandingAndWallBlockItem(
                    standingBlock,
                    GalacticraftBlocks.UNLIT_WALL_TORCH.get(),
                    Direction.DOWN,
                    properties
            )
    );
    public static final DeferredItem<StandingAndWallBlockItem> UNLIT_COPPER_TORCH = REGISTRY.registerBlockItem(
            GalacticraftBlocks.UNLIT_COPPER_TORCH,
            (standingBlock, properties) -> new StandingAndWallBlockItem(
                    standingBlock,
                    GalacticraftBlocks.UNLIT_COPPER_WALL_TORCH.get(),
                    Direction.DOWN,
                    properties
            )
    );
    public static final DeferredItem<BlockItem> UNLIT_LANTERN = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.UNLIT_LANTERN);
    public static final DeferredWeatheringCopperItems UNLIT_COPPER_LANTERNS = DeferredWeatheringCopperItems.create(GalacticraftBlocks.UNLIT_COPPER_LANTERN, REGISTRY::registerSimpleBlockItem);

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftItems::registerItemCapabilities);
    }

    private static void registerItemCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.Energy.ITEM,
                (item, cxt) -> new ItemAccessEnergyHandler(cxt, GalacticraftDataComponents.STORED_ENERGY.get(), 15000, 25),
                BATTERY
        );
        event.registerItem(
                Capabilities.Energy.ITEM,
                (item, cxt) -> InfiniteEnergyHandler.INSTANCE,
                INFINITE_BATTERY
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (item, cxt) -> new ItemAccessFluidTank(cxt, GalacticraftDataComponents.FLUID_TANK.get(), 900, resource -> resource.is(GalacticraftFluids.OXYGEN)),
                LIGHT_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (item, cxt) -> new ItemAccessFluidTank(cxt, GalacticraftDataComponents.FLUID_TANK.get(), 1800, resource -> resource.is(GalacticraftFluids.OXYGEN)),
                MEDIUM_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (item, cxt) -> new ItemAccessFluidTank(cxt, GalacticraftDataComponents.FLUID_TANK.get(), 2700, resource -> resource.is(GalacticraftFluids.OXYGEN)),
                HEAVY_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (item, cxt) -> new InfiniteResourceHandler<>(FluidResource.of(GalacticraftFluids.OXYGEN)),
                INFINITE_OXYGEN_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (item, cxt) -> new ItemAccessFluidHandler(cxt, GalacticraftDataComponents.FLUID_TANK.get(), 8000),
                FLUID_TANK
        );
    }
}
