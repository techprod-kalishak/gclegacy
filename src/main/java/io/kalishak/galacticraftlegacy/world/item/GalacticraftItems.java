/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.references.GalacticraftItemIds;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredItemRegister;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.ItemAccessFluidTank;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlotGroup;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import io.kalishak.galacticraftlegacy.world.item.component.*;
import io.kalishak.galacticraftlegacy.world.item.equipment.GalacticraftArmorMaterials;
import io.kalishak.galacticraftlegacy.world.item.equipment.trim.GalacticraftTrimMaterials;
import io.kalishak.galacticraftlegacy.world.item.gear.FluidTankItem;
import io.kalishak.galacticraftlegacy.world.item.gear.GearItem;
import io.kalishak.galacticraftlegacy.world.item.gear.OxygenTankItem;
import io.kalishak.galacticraftlegacy.world.item.gear.ShieldControllerItem;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.WeatheringCopperCollection;
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

import java.util.List;
import java.util.Optional;

public final class GalacticraftItems {
    private static final DeferredItemRegister REGISTRY = new DeferredItemRegister(Galacticraft.MODID);
    public static final DeferredItem<BatteryItem> BATTERY = REGISTRY.registerItem(
            GalacticraftItemIds.BATTERY,
            BatteryItem::new,
            properties -> properties.stacksTo(1)
    );
    public static final DeferredItem<Item> INFINITE_BATTERY = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.INFINITE_BATTERY,
            properties -> properties
                    .stacksTo(1)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );
    public static final DeferredItem<Item> FREQUENCY_MODULE = REGISTRY.registerItem(
            "frequency_module",
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.FREQUENCY_MODULE)
                    .component(
                            GalacticraftDataComponents.ITEM_WITH_DESCRIPTION,
                            new ItemWithDescription(
                                    GalacticraftComponents.FREQUENCY_MODULE_DESC,
                                    0,
                                    Optional.of(Style.EMPTY.applyFormat(ChatFormatting.AQUA))
                            )
                    )
    );
    public static final DeferredItem<Item> THERMAL_CLOTH = REGISTRY.registerSimpleItem(GalacticraftItemIds.THERMAL_CLOTH);
    public static final DeferredItem<Item> ISOTHERMAL_FABRIC = REGISTRY.registerSimpleItem(GalacticraftItemIds.ISOTHERMAL_FABRIC);
    public static final DeferredItem<GearItem> THERMAL_PADDING_HELM = REGISTRY.registerItem(
            GalacticraftItemIds.THERMAL_CAP,
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_CAP, GearEquipmentAssets.THERMAL, -0.25F)
    );
    public static final DeferredItem<GearItem> THERMAL_PADDING_CHESTPIECE = REGISTRY.registerItem(
            GalacticraftItemIds.THERMAL_SHIRT,
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SHIRT, GearEquipmentAssets.THERMAL, -0.25F)
    );
    public static final DeferredItem<GearItem> THERMAL_PADDING_LEGGINGS = REGISTRY.registerItem(
            GalacticraftItemIds.THERMAL_LEGGINGS,
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_LEGGINGS, GearEquipmentAssets.THERMAL, -0.25F)
    );
    public static final DeferredItem<GearItem> THERMAL_PADDING_BOOTS = REGISTRY.registerItem(
            GalacticraftItemIds.THERMAL_SOCKS,
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SOCKS, GearEquipmentAssets.THERMAL, -0.25F)
    );
    public static final DeferredItem<GearItem> ISOTHERMAL_HELM = REGISTRY.registerItem(
            GalacticraftItemIds.ISOTHERMAL_CAP,
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_CAP, GearEquipmentAssets.ISOTHERMAL, 0.25F)
    );
    public static final DeferredItem<GearItem> ISOTHERMAL_CHESTPIECE = REGISTRY.registerItem(
            GalacticraftItemIds.ISOTHERMAL_SHIRT,
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SHIRT, GearEquipmentAssets.ISOTHERMAL, 0.25F)
    );
    public static final DeferredItem<GearItem> ISOTHERMAL_LEGGINGS = REGISTRY.registerItem(
            GalacticraftItemIds.ISOTHERMAL_LEGGINGS,
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_LEGGINGS, GearEquipmentAssets.ISOTHERMAL, 0.25F)
    );
    public static final DeferredItem<GearItem> ISOTHERMAL_BOOTS = REGISTRY.registerItem(
            GalacticraftItemIds.ISOTHERMAL_SOCKS,
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SOCKS, GearEquipmentAssets.ISOTHERMAL, 0.25F)
    );
    public static final DeferredItem<GearItem> LIGHT_TANK = REGISTRY.registerItem(
            GalacticraftItemIds.LIGHT_OXYGEN_TANK,
            OxygenTankItem::new,
            () -> GearItem.tankProperties(GearEquipmentAssets.LIGHT_TANK)
    );
    public static final DeferredItem<GearItem> MEDIUM_TANK = REGISTRY.registerItem(
            GalacticraftItemIds.MEDIUM_OXYGEN_TANK,
            OxygenTankItem::new,
            () -> GearItem.tankProperties(GearEquipmentAssets.MEDIUM_TANK)
    );
    public static final DeferredItem<GearItem> HEAVY_TANK = REGISTRY.registerItem(
            GalacticraftItemIds.HEAVY_OXYGEN_TANK,
            OxygenTankItem::new,
            () -> GearItem.tankProperties(GearEquipmentAssets.HEAVY_TANK)
    );
    public static final DeferredItem<GearItem> INFINITE_OXYGEN_TANK = REGISTRY.registerItem(
            GalacticraftItemIds.INFINITE_OXYGEN_TANK,
            GearItem::new,
            () -> GearItem.tankProperties(GearEquipmentAssets.HEAVY_TANK)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );
    public static final DeferredItem<GearItem> OXYGEN_MASK = REGISTRY.registerItem(
            GalacticraftItemIds.OXYGEN_MASK,
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.MASK)
    );
    public static final DeferredItem<GearItem> OXYGEN_GEAR = REGISTRY.registerItem(
            GalacticraftItemIds.OXYGEN_GEAR,
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.GEAR)
    );
    public static final DeferredItem<Item> WRENCH = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.WRENCH,
            properties -> properties.durability(256)
    );
    public static final ColorCollection<DeferredItem<GearItem>> PARACHUTE = REGISTRY.registerColoredItems(
            GalacticraftItemIds.PARACHUTE,
            GearItem::new,
            GearItem::parachute
    );
    public static final DeferredItem<GearItem> PROTO_SHIELD_CONTROLLER = REGISTRY.registerItem(
            GalacticraftItemIds.PROTO_SHIELD_CONTROLLER,
            ShieldControllerItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.SHIELD)
                    .stacksTo(1)
                    .component(
                            GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS,
                            GearAttributeModifiers.builder()
                                    .add(
                                            GalacticraftAttributes.CORROSION_PROTECTION,
                                            new AttributeModifier(
                                                    Constants.id("corrosion_protection"),
                                                    1.0D,
                                                    AttributeModifier.Operation.ADD_VALUE
                                            ),
                                            GearEquipmentSlotGroup.GEAR
                                    )
                                    .build()
                    )
                    .component(GalacticraftDataComponents.SHIELD_CONTROLLER, new ShieldController(300))
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .component(GalacticraftDataComponents.ITEM_WITH_DESCRIPTION, new ItemWithDescription(Constants.id("shield_controller").toLanguageKey("item", "desc")))
    );
    public static final DeferredItem<GearItem> SHIELD_CONTROLLER = REGISTRY.registerItemWithDescription(
            GalacticraftItemIds.SHIELD_CONTROLLER,
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.SHIELD)
                    .component(
                            GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS,
                            GearAttributeModifiers.builder()
                                    .add(
                                            GalacticraftAttributes.CORROSION_PROTECTION,
                                            new AttributeModifier(
                                                    Constants.id("corrosion_protection"),
                                                    1.0D,
                                                    AttributeModifier.Operation.ADD_VALUE
                                            ),
                                            GearEquipmentSlotGroup.GEAR
                                    )
                                    .build()
                    )
    );
    public static final DeferredItem<Item> SENSOR_GLASSES = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.SENSOR_GLASSES,
            properties -> properties
                    .stacksTo(1)
                    .component(
                            DataComponents.EQUIPPABLE,
                            Equippable.builder(EquipmentSlot.HEAD)
                                    .setAllowedEntities(EntityTypes.PLAYER)
                                    .setAsset(GearEquipmentAssets.SENSOR_GLASSES).build()
                    )
    );
    public static final DeferredItem<StructureFinderItem> DUNGEON_LOCATOR = REGISTRY.registerItem(
            GalacticraftItemIds.DUNGEON_LOCATOR,
            StructureFinderItem::new,
            properties -> properties.stacksTo(1)
    );
    public static final DeferredItem<Item> ALUMINUM_INGOT = REGISTRY.registerSimpleItem(GalacticraftItemIds.ALUMINUM_INGOT);
    public static final DeferredItem<Item> RAW_ALUMINUM = REGISTRY.registerSimpleItem(GalacticraftItemIds.RAW_ALUMINUM);
    public static final DeferredItem<Item> RAW_METEORIC_IRON = REGISTRY.registerSimpleItem(GalacticraftItemIds.RAW_METEORIC_IRON);
    public static final DeferredItem<Item> METEORIC_IRON_INGOT = REGISTRY.registerSimpleItem(GalacticraftItemIds.METEORIC_IRON_INGOT);
    public static final DeferredItem<Item> TIN_INGOT = REGISTRY.registerSimpleItem(GalacticraftItemIds.TIN_INGOT);
    public static final DeferredItem<Item> RAW_TIN = REGISTRY.registerSimpleItem(GalacticraftItemIds.RAW_TIN);
    public static final DeferredItem<Item> RAW_SILICON = REGISTRY.registerSimpleItem(GalacticraftItemIds.RAW_SILICON);
    public static final DeferredItem<Item> SAPPHIRE = REGISTRY.registerSimpleItemWithDescription(GalacticraftItemIds.SAPPHIRE, properties -> properties.rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> BASIC_WAFER = REGISTRY.registerSimpleItem(GalacticraftItemIds.BASIC_WAFER);
    public static final DeferredItem<Item> ADVANCED_WAFER = REGISTRY.registerSimpleItem(GalacticraftItemIds.ADVANCED_WAFER);
    public static final DeferredItem<Item> SOLAR_WAFER = REGISTRY.registerSimpleItem(GalacticraftItemIds.SOLAR_WAFER);
    public static final DeferredItem<Item> FLAG = REGISTRY.registerItem(GalacticraftItemIds.FLAG, FlagItem::new, properties -> properties.stacksTo(1));
    public static final DeferredItem<SchematicItem> SCHEMATIC = REGISTRY.registerItem(
            GalacticraftItemIds.NASA_WORKBENCH_SCHEMATIC,
            SchematicItem::new,
            properties -> properties
                    .stacksTo(1)
                    .delayedHolderComponent(GalacticraftDataComponents.SCHEMATIC.get(), SchematicVariants.TIER_2_ROCKET)
    );
    public static final DeferredItem<FluidTankItem> FLUID_TANK = REGISTRY.registerItem(
            GalacticraftItemIds.FLUID_TANK,
            FluidTankItem::new,
            () -> new Item.Properties()
                    .stacksTo(1)
                    .component(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY)
    );
    public static final DeferredItem<BucketItem> OIL_BUCKET = REGISTRY.registerItem(
            GalacticraftItemIds.OIL_BUCKET,
            properties -> new BucketItem(GalacticraftFluids.OIL.value(), properties)
    );
    public static final DeferredItem<BucketItem> FUEL_BUCKET = REGISTRY.registerItem(
            GalacticraftItemIds.FUEL_BUCKET,
            properties -> new BucketItem(GalacticraftFluids.FUEL.value(), properties)
    );

    public static final DeferredItem<Item> RAW_STEEL = REGISTRY.registerSimpleItem(GalacticraftItemIds.RAW_STEEL);
    public static final DeferredItem<Item> STEEL_INGOT = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_INGOT,
            properties -> properties.trimMaterial(GalacticraftTrimMaterials.STEEL)
    );
    public static final DeferredItem<Item> STEEL_NUGGET = REGISTRY.registerSimpleItem(GalacticraftItemIds.STEEL_NUGGET);
    //STEEL ARMOR & TOOL SET
    public static final DeferredItem<Item> STEEL_HELMET = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_HELMET,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.STEEL, ArmorType.HELMET)
    );
    public static final DeferredItem<Item> STEEL_CHESTPLATE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_CHESTPLATE,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.STEEL, ArmorType.CHESTPLATE)
    );
    public static final DeferredItem<Item> STEEL_LEGGINGS = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_LEGGINGS,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.STEEL, ArmorType.LEGGINGS)
    );
    public static final DeferredItem<Item> STEEL_BOOTS = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_BOOTS,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.STEEL, ArmorType.BOOTS)
    );
    public static final DeferredItem<Item> STEEL_HORSE_ARMOR = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_HORSE_ARMOR,
            properties -> properties.horseArmor(GalacticraftArmorMaterials.STEEL)
    );
    public static final DeferredItem<Item> STEEL_NAUTILUS_ARMOR = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_NAUTILUS_ARMOR,
            properties -> properties.nautilusArmor(GalacticraftArmorMaterials.STEEL)
    );
    public static final DeferredItem<Item> STEEL_SWORD = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_SWORD,
            properties -> properties.sword(GalacticraftToolMaterials.STEEL, 3.0F, -2.4F)
    );
    public static final DeferredItem<Item> STEEL_SPEAR = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_SPEAR,
            properties -> properties.spear(GalacticraftToolMaterials.STEEL, 0.85F, 1.05F, 0.8F, 2.0F, 6.5F, 4.5F, 4.4F, 9.5F, 3.9F)
    );
    public static final DeferredItem<ShovelItem> STEEL_SHOVEL = REGISTRY.registerItem(
            GalacticraftItemIds.STEEL_SHOVEL,
            properties -> new ShovelItem(GalacticraftToolMaterials.STEEL, 3.0F, -2.4F, properties)
    );
    public static final DeferredItem<Item> STEEL_PICKAXE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.STEEL_PICKAXE,
            properties -> properties.pickaxe(GalacticraftToolMaterials.STEEL, 1.0F, -2.8F)
    );
    public static final DeferredItem<AxeItem> STEEL_AXE = REGISTRY.registerItem(
            GalacticraftItemIds.STEEL_AXE,
            properties -> new AxeItem(GalacticraftToolMaterials.STEEL, 3.0F, -3.0F, properties)
    );
    public static final DeferredItem<HoeItem> STEEL_HOE = REGISTRY.registerItem(
            GalacticraftItemIds.STEEL_HOE,
            properties -> new HoeItem(GalacticraftToolMaterials.STEEL, -4.0F, 0.0F, properties)
    );

    public static final DeferredItem<Item> RAW_DESH = REGISTRY.registerSimpleItem(GalacticraftItemIds.RAW_DESH);
    public static final DeferredItem<Item> DESH_INGOT = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DESH_INGOT,
            properties -> properties.trimMaterial(GalacticraftTrimMaterials.DESH)
    );
    public static final DeferredItem<Item> DESH_NUGGET = REGISTRY.registerSimpleItem(GalacticraftItemIds.DESH_NUGGET);
    //DESH ARMOR & TOOL SET
    public static final DeferredItem<Item> DESH_HELMET = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DESH_HELMET,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.DESH, ArmorType.HELMET)
    );
    public static final DeferredItem<Item> DESH_CHESTPLATE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DESH_CHESTPLATE,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.DESH, ArmorType.CHESTPLATE)
    );
    public static final DeferredItem<Item> DESH_LEGGINGS = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DESH_LEGGINGS,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.DESH, ArmorType.LEGGINGS)
    );
    public static final DeferredItem<Item> DESH_BOOTS = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DESH_BOOTS,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.DESH, ArmorType.BOOTS)
    );
    public static final DeferredItem<Item> DESH_SWORD = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DESH_SWORD,
            properties -> properties.sword(GalacticraftToolMaterials.DESH, 3.0F, -2.4F)
    );
    public static final DeferredItem<Item> DESH_SPEAR = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DESH_SPEAR,
            properties -> properties.spear(GalacticraftToolMaterials.DESH, 1.15F, 0.95F, 0.7F, 2.7F, 8.5F, 4.6F, 5.5F, 10.0F, 4.8F)
    );
    public static final DeferredItem<ShovelItem> DESH_SHOVEL = REGISTRY.registerItem(
            GalacticraftItemIds.DESH_SHOVEL,
            properties -> new ShovelItem(GalacticraftToolMaterials.DESH, 3.0F, -2.4F, properties)
    );
    public static final DeferredItem<Item> DESH_PICKAXE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DESH_PICKAXE,
            properties -> properties.pickaxe(GalacticraftToolMaterials.DESH, 1.0F, -2.8F)
    );
    public static final DeferredItem<AxeItem> DESH_AXE = REGISTRY.registerItem(
            GalacticraftItemIds.DESH_AXE,
            properties -> new AxeItem(GalacticraftToolMaterials.DESH, 3.0F, -3.0F, properties)
    );
    public static final DeferredItem<HoeItem> DESH_HOE = REGISTRY.registerItem(
            GalacticraftItemIds.DESH_HOE,
            properties -> new HoeItem(GalacticraftToolMaterials.DESH, -4.0F, 0.0F, properties)
    );

    public static final DeferredItem<Item> RAW_TITANIUM = REGISTRY.registerSimpleItem(GalacticraftItemIds.RAW_TITANIUM);
    public static final DeferredItem<Item> TITANIUM_INGOT = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TITANIUM_INGOT,
            properties -> properties.trimMaterial(GalacticraftTrimMaterials.TITANIUM)
    );
    public static final DeferredItem<Item> TITANIUM_NUGGET = REGISTRY.registerSimpleItem(GalacticraftItemIds.TITANIUM_NUGGET);
    //TITANIUM ARMOR & TOOL SET
    //TODO change wooden stick to desh
    public static final DeferredItem<Item> TITANIUM_HELMET = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TITANIUM_HELMET,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.TITANIUM, ArmorType.HELMET)
    );
    public static final DeferredItem<Item> TITANIUM_CHESTPLATE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TITANIUM_CHESTPLATE,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.TITANIUM, ArmorType.CHESTPLATE)
    );
    public static final DeferredItem<Item> TITANIUM_LEGGINGS = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TITANIUM_LEGGINGS,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.TITANIUM, ArmorType.LEGGINGS)
    );
    public static final DeferredItem<Item> TITANIUM_BOOTS = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TITANIUM_BOOTS,
            properties -> properties.humanoidArmor(GalacticraftArmorMaterials.TITANIUM, ArmorType.BOOTS)
    );
    public static final DeferredItem<Item> TITANIUM_SWORD = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TITANIUM_SWORD,
            properties -> properties.sword(GalacticraftToolMaterials.TITANIUM, 3.0F, -2.4F)
    );
    public static final DeferredItem<Item> TITANIUM_SPEAR = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TITANIUM_SPEAR,
            properties -> properties.spear(GalacticraftToolMaterials.TITANIUM, 0.85F, 1.25F, 0.4F, 3.5F, 8.7F, 8.5F, 5.2F, 14.0F, 5.0F)
    );
    public static final DeferredItem<ShovelItem> TITANIUM_SHOVEL = REGISTRY.registerItem(
            GalacticraftItemIds.TITANIUM_SHOVEL,
            properties -> new ShovelItem(GalacticraftToolMaterials.TITANIUM, 3.0F, -2.4F, properties)
    );
    public static final DeferredItem<Item> TITANIUM_PICKAXE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TITANIUM_PICKAXE,
            properties -> properties.pickaxe(GalacticraftToolMaterials.TITANIUM, 1.0F, -2.8F)
    );
    public static final DeferredItem<AxeItem> TITANIUM_AXE = REGISTRY.registerItem(
            GalacticraftItemIds.TITANIUM_AXE,
            properties -> new AxeItem(GalacticraftToolMaterials.TITANIUM, 3.0F, -3.0F, properties)
    );
    public static final DeferredItem<HoeItem> TITANIUM_HOE = REGISTRY.registerItem(
            GalacticraftItemIds.TITANIUM_HOE,
            properties -> new HoeItem(GalacticraftToolMaterials.TITANIUM, -4.0F, 0.0F, properties)
    );
    public static final DeferredItem<Item> RAW_LEAD = REGISTRY.registerSimpleItem(GalacticraftItemIds.RAW_LEAD);
    public static final DeferredItem<Item> LEAD_INGOT = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.LEAD_INGOT,
            properties -> properties.trimMaterial(GalacticraftTrimMaterials.LEAD)
    );
    public static final DeferredItem<Item> CHEESE_CHUNK = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.CHEESE_CHUNK,
            properties -> properties
                    .trimMaterial(GalacticraftTrimMaterials.CHEESE)
                    .food(GalacticraftFoods.CHEESE)
    );
    public static final DeferredItem<Item> CHEESE_SLICE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.CHEESE_SLICE,
            properties -> properties.food(GalacticraftFoods.CHEESE)
    );
    public static final DeferredItem<Item> MOON_DUNGEON_KEY = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.MOON_DUNGEON_KEY,
            properties -> properties.stacksTo(1).component(GalacticraftDataComponents.KEY_LOCK, KeyLock.preGenTier(FeatureTier.TIER_1))
    );
    public static final DeferredItem<Item> MARS_DUNGEON_KEY = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.MARS_DUNGEON_KEY,
            properties -> properties.stacksTo(1).component(GalacticraftDataComponents.KEY_LOCK, KeyLock.preGenTier(FeatureTier.TIER_2))
    );
    public static final DeferredItem<Item> VENUS_DUNGEON_KEY = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.VENUS_DUNGEON_KEY,
            properties -> properties.stacksTo(1).component(GalacticraftDataComponents.KEY_LOCK, KeyLock.preGenTier(FeatureTier.TIER_3))
    );
    public static final DeferredItem<Item> TIN_CANISTER = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.TIN_CANISTER
    );
    public static final DeferredItem<Item> DEHYDRATED_APPLE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DEHYDRATED_APPLE,
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_APPLE)
    );
    public static final DeferredItem<Item> DEHYDRATED_CARROT = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DEHYDRATED_CARROT,
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_CARROT)
    );
    public static final DeferredItem<Item> DEHYDRATED_MELON = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DEHYDRATED_MELON,
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_MELON)
    );
    public static final DeferredItem<Item> DEHYDRATED_PUMPKIN = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DEHYDRATED_PUMPKIN,
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_PUMPKIN)
    );
    public static final DeferredItem<Item> DEHYDRATED_POTATO = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DEHYDRATED_POTATO,
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_POTATO)
    );
    public static final DeferredItem<Item> DEHYDRATED_BEETROOT = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.DEHYDRATED_BEETROOT,
            () -> CannedFood.createProperties(GalacticraftFoods.DEHYDRATED_BEET)
    );
    public static final DeferredItem<Item> CANNED_BEEF = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.CANNED_BEEF,
            () -> CannedFood.createProperties(GalacticraftFoods.CANNED_BEEF)
    );
    public static final DeferredItem<Item> COMPRESSED_ALUMINUM = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_ALUMINUM);
    public static final DeferredItem<Item> COMPRESSED_BRONZE = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_BRONZE);
    public static final DeferredItem<Item> COMPRESSED_COPPER = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_COPPER);
    public static final DeferredItem<Item> COMPRESSED_DESH = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_DESH);
    public static final DeferredItem<Item> COMPRESSED_IRON = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_IRON);
    public static final DeferredItem<Item> COMPRESSED_METEORIC_IRON = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_METEORIC_IRON);
    public static final DeferredItem<Item> COMPRESSED_TIN = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_TIN);
    public static final DeferredItem<Item> COMPRESSED_TITANIUM = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_TITANIUM);
    public static final DeferredItem<Item> COMPRESSED_STEEL = REGISTRY.registerSimpleItem(GalacticraftItemIds.COMPRESSED_STEEL);
    public static final DeferredItem<Item> HEAVY_DUTY_PLATE = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.HEAVY_DUTY_PLATE,
            properties -> properties
                    .component(
                            GalacticraftDataComponents.ROCKET_PART,
                            new VehiclePart(
                                    VehicleComponentType.PLATE,
                                    List.of(FeatureTier.TIER_1)
                            )
                    )
    );
    public static final DeferredItem<Item> HEAVY_DUTY_PLATE_TIER_2 = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.T2_HEAVY_DUTY_PLATE,
            properties -> properties
                    .component(
                            GalacticraftDataComponents.ROCKET_PART,
                            new VehiclePart(
                                    VehicleComponentType.PLATE,
                                    List.of(FeatureTier.TIER_2)
                            )
                    )
    );
    public static final DeferredItem<Item> HEAVY_DUTY_PLATE_TIER_3 = REGISTRY.registerSimpleItem(
            GalacticraftItemIds.T3_HEAVY_DUTY_PLATE,
            properties -> properties
                    .component(
                            GalacticraftDataComponents.ROCKET_PART,
                            new VehiclePart(
                                    VehicleComponentType.PLATE,
                                    List.of(FeatureTier.TIER_3)
                            )
                    )
    );
    public static final DeferredItem<SpawnEggItem> EVOLVED_SKELETON_SPAWN_EGG = REGISTRY.registerItem(
            GalacticraftItemIds.EVOLVED_SKELETON_SPAWN_EGG,
            SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(GalacticraftEntityType.EVOLVED_SKELETON.get())
    );
    public static final DeferredItem<SpawnEggItem> EVOLVED_ZOMBIE_SPAWN_EGG = REGISTRY.registerItem(
            GalacticraftItemIds.EVOLVED_ZOMBIE_SPAWN_EGG,
            SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(GalacticraftEntityType.EVOLVED_ZOMBIE.get())
    );
    public static final DeferredItem<ThrowableMeteorItem> THROWABLE_METEOR_CHUNK = REGISTRY.registerItem(
            GalacticraftItemIds.THROWABLE_METEOR_CHUNK,
            ThrowableMeteorItem::new
    );

    public static final DeferredItem<BlockItem> ALUMINUM_WIRE = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.ALUMINUM_WIRE);
    public static final DeferredItem<BlockItem> HEAVY_ALUMINUM_WIRE = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.HEAVY_ALUMINUM_WIRE);
    public static final ColorCollection<DeferredItem<BlockItem>> FLUID_PIPE = REGISTRY.registerBlockItemColorCollection(
            GalacticraftBlocks.FLUID_PIPE,
            BlockItem::new,
            _ -> new Item.Properties()
    );
    public static final DeferredItem<BlockItem> GRATING = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.GRATING);
    public static final DeferredItem<BlockItem> OXYGEN_DETECTOR = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.OXYGEN_DETECTOR);
    public static final DeferredItem<BlockItem> CHEESE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.CHEESE);
    public static final DeferredItem<BlockItem> COAL_GENERATOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.COAL_GENERATOR);
    public static final DeferredItem<BlockItem> CIRCUIT_FABRICATOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.CIRCUIT_FABRICATOR);
    public static final DeferredItem<BlockItem> COMPRESSOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.COMPRESSOR);
    public static final DeferredItem<BlockItem> ELECTRIC_COMPRESSOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.ELECTRIC_COMPRESSOR);
    public static final DeferredItem<BlockItem> ELECTRIC_FURNACE = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.ELECTRIC_FURNACE);
    public static final DeferredItem<BlockItem> ELECTRIC_ARC_FURNACE = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.ELECTRIC_ARC_FURNACE);
    public static final DeferredItem<BlockItem> OXYGEN_COLLECTOR = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.OXYGEN_COLLECTOR);
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
    public static final WeatheringCopperCollection<DeferredItem<BlockItem>> UNLIT_COPPER_LANTERN = REGISTRY.registerWeatheringCopperItems(GalacticraftBlocks.UNLIT_COPPER_LANTERN);
    public static final DeferredItem<BlockItem> MAGNETIC_CRAFTING_TABLE = REGISTRY.registerSimpleBlockItemWithDescription(GalacticraftBlocks.MAGNETIC_CRAFTING_TABLE);
    public static final DeferredItem<BlockItem> ASTEROID_ROCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ASTEROID_ROCK);
    public static final DeferredItem<BlockItem> ASTEROID_ROCK_SLAB = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ASTEROID_ROCK_SLAB);
    public static final DeferredItem<BlockItem> ASTEROID_ROCK_STAIRS = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ASTEROID_ROCK_STAIRS);
    public static final DeferredItem<BlockItem> ASTEROID_ROCK_WALL = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ASTEROID_ROCK_WALL);
    public static final DeferredItem<BlockItem> ASTEROID_ALUMINUM_ORE = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.ASTEROID_ALUMINUM_ORE);
    public static final DeferredItem<FallenMeteorItem> FALLEN_METEOR = REGISTRY.registerBlockItemWithDescription(
            GalacticraftBlocks.FALLEN_METEOR,
            FallenMeteorItem::new,
            () -> new Item.Properties().stacksTo(1)
    );
    public static final DeferredItem<BlockItem> TIN_DECORATION_BLOCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.TIN_DECORATION_BLOCK);
    public static final DeferredItem<BlockItem> TIN_DECORATION_CUT_BLOCK = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.TIN_DECORATION_CUT_BLOCK);
    public static final DeferredItem<BlockItem> TIN_DECORATION_SLAB = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.TIN_DECORATION_SLAB);
    public static final DeferredItem<BlockItem> TIN_DECORATION_STAIRS = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.TIN_DECORATION_STAIRS);
    public static final DeferredItem<BlockItem> TIN_DECORATION_WALL = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.TIN_DECORATION_WALL);

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftItems::registerItemCapabilities);
    }

    private static void registerItemCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.Energy.ITEM,
                (_, cxt) -> new ItemAccessEnergyHandler(cxt, GalacticraftDataComponents.STORED_ENERGY.get(), 15000, 25),
                BATTERY
        );
        event.registerItem(
                Capabilities.Energy.ITEM,
                (_, _) -> InfiniteEnergyHandler.INSTANCE,
                INFINITE_BATTERY
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (_, cxt) -> new ItemAccessFluidTank(cxt, GalacticraftDataComponents.FLUID_TANK.get(), 900, resource -> resource.is(GalacticraftFluids.OXYGEN)),
                LIGHT_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (_, cxt) -> new ItemAccessFluidTank(cxt, GalacticraftDataComponents.FLUID_TANK.get(), 1800, resource -> resource.is(GalacticraftFluids.OXYGEN)),
                MEDIUM_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (_, cxt) -> new ItemAccessFluidTank(cxt, GalacticraftDataComponents.FLUID_TANK.get(), 2700, resource -> resource.is(GalacticraftFluids.OXYGEN)),
                HEAVY_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (_, _) -> new InfiniteResourceHandler<>(FluidResource.of(GalacticraftFluids.OXYGEN)),
                INFINITE_OXYGEN_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (_, cxt) -> new ItemAccessFluidHandler(cxt, GalacticraftDataComponents.FLUID_TANK.get(), 8000),
                FLUID_TANK
        );
    }
}
