/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.Comparator;

public final class GalacticraftCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Galacticraft.MODID);
    private static final Comparator<Holder<SchematicVariant>> SCHEMATIC_SORTER = Comparator.comparing(
            Holder::value, Comparator.comparingInt(schematic -> schematic.tier().getLevel())
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = REGISTRY.register(
            "items",
            CreativeModeTab.builder()
                    .icon(GalacticraftItems.OXYGEN_MASK::toStack)
                    .displayItems(GalacticraftCreativeModeTabs::buildItems)
                    .title(GalacticraftComponents.CREATIVE_MODE_TAB_ITEMS)
                    ::build
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = REGISTRY.register(
            "blocks",
            CreativeModeTab.builder()
                    .icon(GalacticraftItems.COAL_GENERATOR::toStack)
                    .displayItems(GalacticraftCreativeModeTabs::buildBlocks)
                    .title(GalacticraftComponents.CREATIVE_MODE_TAB_BLOCKS)
                    ::build
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftCreativeModeTabs::populateVanillaCreativeTabs);
    }

    private static void buildItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(GalacticraftItems.OXYGEN_MASK);
        output.accept(GalacticraftItems.OXYGEN_GEAR);
        emptyAndFilled(output, GalacticraftItems.LIGHT_TANK, GalacticraftFluids.OXYGEN);
        emptyAndFilled(output, GalacticraftItems.MEDIUM_TANK, GalacticraftFluids.OXYGEN);
        emptyAndFilled(output, GalacticraftItems.HEAVY_TANK, GalacticraftFluids.OXYGEN);
        output.accept(GalacticraftItems.INFINITE_OXYGEN_TANK);
        output.accept(GalacticraftItems.SENSOR_GLASSES);
        GalacticraftItems.PARACHUTE.forEach(output::accept);
        output.accept(GalacticraftItems.PROTO_SHIELD_CONTROLLER);
        output.accept(GalacticraftItems.SHIELD_CONTROLLER);
        output.accept(GalacticraftItems.TIN_CANISTER);
        output.accept(GalacticraftItems.FLUID_TANK);
        emptyAndFilled(output, GalacticraftItems.FLUID_TANK, GalacticraftFluids.OXYGEN);
        emptyAndFilled(output, GalacticraftItems.FLUID_TANK, GalacticraftFluids.OIL);
        emptyAndFilled(output, GalacticraftItems.FLUID_TANK, GalacticraftFluids.FUEL);
        output.accept(GalacticraftItems.RAW_SILICON);
        output.accept(GalacticraftItems.SOLAR_WAFER);
        output.accept(GalacticraftItems.BASIC_WAFER);
        output.accept(GalacticraftItems.ADVANCED_WAFER);
        output.accept(GalacticraftItems.DEHYDRATED_APPLE);
        output.accept(GalacticraftItems.DEHYDRATED_CARROT);
        output.accept(GalacticraftItems.DEHYDRATED_MELON);
        output.accept(GalacticraftItems.DEHYDRATED_PUMPKIN);
        output.accept(GalacticraftItems.DEHYDRATED_POTATO);
        output.accept(GalacticraftItems.DEHYDRATED_BEETROOT);
        output.accept(GalacticraftItems.CANNED_BEEF);
        output.accept(GalacticraftItems.CHEESE_SLICE);
        emptyAndCharged(output, GalacticraftItems.BATTERY);
        output.accept(GalacticraftItems.INFINITE_BATTERY);
        output.accept(GalacticraftItems.CHEESE_CHUNK);
        output.accept(GalacticraftItems.RAW_METEORIC_IRON);
        output.accept(GalacticraftItems.SAPPHIRE);
        output.accept(GalacticraftItems.FLAG);
        output.accept(GalacticraftItems.DUNGEON_LOCATOR);
        output.accept(GalacticraftItems.RAW_DESH);
        output.accept(GalacticraftItems.OIL_BUCKET);
        output.accept(GalacticraftItems.FUEL_BUCKET);
        itemDisplayParameters.holders().lookup(GalacticraftRegistries.Keys.SCHEMATIC).ifPresent(registry -> {
            generateSchematics(output, registry);
        });
        output.accept(GalacticraftItems.STEEL_HELMET);
        output.accept(GalacticraftItems.STEEL_CHESTPLATE);
        output.accept(GalacticraftItems.STEEL_LEGGINGS);
        output.accept(GalacticraftItems.STEEL_BOOTS);
        output.accept(GalacticraftItems.STEEL_HORSE_ARMOR);
        output.accept(GalacticraftItems.STEEL_NAUTILUS_ARMOR);
        output.accept(GalacticraftItems.DESH_HELMET);
        output.accept(GalacticraftItems.DESH_CHESTPLATE);
        output.accept(GalacticraftItems.DESH_LEGGINGS);
        output.accept(GalacticraftItems.DESH_BOOTS);
        output.accept(GalacticraftItems.THERMAL_PADDING_HELM);
        output.accept(GalacticraftItems.THERMAL_PADDING_CHESTPIECE);
        output.accept(GalacticraftItems.THERMAL_PADDING_LEGGINGS);
        output.accept(GalacticraftItems.THERMAL_PADDING_BOOTS);
        output.accept(GalacticraftItems.TITANIUM_HELMET);
        output.accept(GalacticraftItems.TITANIUM_CHESTPLATE);
        output.accept(GalacticraftItems.TITANIUM_LEGGINGS);
        output.accept(GalacticraftItems.TITANIUM_BOOTS);
        output.accept(GalacticraftItems.ISOTHERMAL_HELM);
        output.accept(GalacticraftItems.ISOTHERMAL_CHESTPIECE);
        output.accept(GalacticraftItems.ISOTHERMAL_LEGGINGS);
        output.accept(GalacticraftItems.ISOTHERMAL_BOOTS);
        output.accept(GalacticraftItems.COMPRESSED_COPPER);
        output.accept(GalacticraftItems.COMPRESSED_TIN);
        output.accept(GalacticraftItems.COMPRESSED_ALUMINUM);
        output.accept(GalacticraftItems.COMPRESSED_STEEL);
        output.accept(GalacticraftItems.COMPRESSED_BRONZE);
        output.accept(GalacticraftItems.COMPRESSED_IRON);
        output.accept(GalacticraftItems.HEAVY_DUTY_PLATE);
        output.accept(GalacticraftItems.COMPRESSED_METEORIC_IRON);
        output.accept(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_2);
        output.accept(GalacticraftItems.COMPRESSED_DESH);
        output.accept(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_3);
        output.accept(GalacticraftItems.COMPRESSED_TITANIUM);
        output.accept(GalacticraftItems.RAW_STEEL);
        output.accept(GalacticraftItems.STEEL_INGOT);
        output.accept(GalacticraftItems.STEEL_NUGGET);
        output.accept(GalacticraftItems.RAW_TIN);
        output.accept(GalacticraftItems.TIN_INGOT);
        output.accept(GalacticraftItems.RAW_ALUMINUM);
        output.accept(GalacticraftItems.ALUMINUM_INGOT);
        output.accept(GalacticraftItems.DESH_INGOT);
        output.accept(GalacticraftItems.DESH_NUGGET);
        output.accept(GalacticraftItems.RAW_TITANIUM);
        output.accept(GalacticraftItems.TITANIUM_INGOT);
        output.accept(GalacticraftItems.TITANIUM_NUGGET);
        output.accept(GalacticraftItems.METEORIC_IRON_INGOT);
        output.accept(GalacticraftItems.RAW_LEAD);
        output.accept(GalacticraftItems.LEAD_INGOT);
        output.accept(GalacticraftItems.WRENCH);
        output.accept(GalacticraftItems.STEEL_PICKAXE);
        output.accept(GalacticraftItems.STEEL_AXE);
        output.accept(GalacticraftItems.STEEL_SHOVEL);
        output.accept(GalacticraftItems.STEEL_HOE);
        output.accept(GalacticraftItems.STEEL_SWORD);
        output.accept(GalacticraftItems.STEEL_SPEAR);
        output.accept(GalacticraftItems.DESH_PICKAXE);
        output.accept(GalacticraftItems.DESH_AXE);
        output.accept(GalacticraftItems.DESH_HOE);
        output.accept(GalacticraftItems.DESH_SHOVEL);
        output.accept(GalacticraftItems.DESH_SWORD);
        output.accept(GalacticraftItems.DESH_SPEAR);
        output.accept(GalacticraftItems.TITANIUM_SHOVEL);
        output.accept(GalacticraftItems.TITANIUM_PICKAXE);
        output.accept(GalacticraftItems.TITANIUM_AXE);
        output.accept(GalacticraftItems.TITANIUM_HOE);
        output.accept(GalacticraftItems.TITANIUM_SWORD);
        output.accept(GalacticraftItems.TITANIUM_SPEAR);
    }

    private static void buildBlocks(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(GalacticraftItems.OXYGEN_COLLECTOR);
        output.accept(GalacticraftItems.OXYGEN_DETECTOR);
        output.accept(GalacticraftItems.ALUMINUM_WIRE);
        output.accept(GalacticraftItems.HEAVY_ALUMINUM_WIRE);
        GalacticraftItems.FLUID_PIPE.forEach(fluidPipe -> {
            if (fluidPipe == GalacticraftItems.FLUID_PIPE.white()) {
                output.accept(fluidPipe);
            } else {
                output.accept(fluidPipe, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
            }
        });
        output.accept(GalacticraftItems.TIN_ORE);
        output.accept(GalacticraftItems.DEEPSLATE_TIN_ORE);
        output.accept(GalacticraftItems.ALUMINUM_ORE);
        output.accept(GalacticraftItems.DEEPSLATE_ALUMINUM_ORE);
        output.accept(GalacticraftItems.SILICON_ORE);
        output.accept(GalacticraftItems.DEEPSLATE_SILICON_ORE);
        output.accept(GalacticraftItems.RAW_TIN_BLOCK);
        output.accept(GalacticraftItems.TIN_BLOCK);
        output.accept(GalacticraftItems.RAW_ALUMINUM_BLOCK);
        output.accept(GalacticraftItems.ALUMINUM_BLOCK);
        output.accept(GalacticraftItems.RAW_SILICON_BLOCK);
        output.accept(GalacticraftItems.MAGNETIC_CRAFTING_TABLE);
        output.accept(GalacticraftItems.COAL_GENERATOR);
        output.accept(GalacticraftItems.COMPRESSOR);
        output.accept(GalacticraftItems.ELECTRIC_COMPRESSOR);
        output.accept(GalacticraftItems.CIRCUIT_FABRICATOR);
        output.accept(GalacticraftItems.ELECTRIC_FURNACE);
        output.accept(GalacticraftItems.ELECTRIC_ARC_FURNACE);
        output.accept(GalacticraftItems.MOON_COPPER_ORE);
        output.accept(GalacticraftItems.MOON_TIN_ORE);
        output.accept(GalacticraftItems.MOON_CHEESE_ORE);
        output.accept(GalacticraftItems.MOON_DIRT);
        output.accept(GalacticraftItems.MOON_ROCK);
        output.accept(GalacticraftItems.MOON_TURF);
        output.accept(GalacticraftItems.MOON_SAPPHIRE_ORE);
        output.accept(GalacticraftItems.MOON_BRICKS);
        output.accept(GalacticraftItems.MOON_BRICK_STAIRS);
        output.accept(GalacticraftItems.MOON_BRICK_SLAB);
        output.accept(GalacticraftItems.MOON_BRICK_WALL);
        output.accept(GalacticraftItems.CHEESE);
        output.accept(GalacticraftItems.GRATING);
        output.accept(GalacticraftItems.ASTEROID_ROCK);
        output.accept(GalacticraftItems.ASTEROID_ROCK_SLAB);
        output.accept(GalacticraftItems.ASTEROID_ROCK_STAIRS);
        output.accept(GalacticraftItems.ASTEROID_ROCK_WALL);
        output.accept(GalacticraftItems.ASTEROID_ALUMINUM_ORE);

        output.accept(GalacticraftItems.UNLIT_TORCH, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
        output.accept(GalacticraftItems.UNLIT_COPPER_TORCH, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
        output.accept(GalacticraftItems.UNLIT_LANTERN, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
        GalacticraftItems.UNLIT_COPPER_LANTERN.forEach(itemStack -> output.accept(itemStack, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY));
    }

    private static void populateVanillaCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.SPAWN_EGGS)) {
            event.accept(GalacticraftItems.EVOLVED_SKELETON_SPAWN_EGG);
            event.accept(GalacticraftItems.EVOLVED_ZOMBIE_SPAWN_EGG);
        }
    }

    private static void emptyAndCharged(CreativeModeTab.Output output, ItemLike item) {
        ItemStack stack = new ItemStack(item);
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        if (energyHandler != null) {
            stack.set(GalacticraftDataComponents.STORED_ENERGY, energyHandler.getCapacityAsInt());
            output.accept(stack);
        }
    }

    private static void emptyAndFilled(CreativeModeTab.Output output, ItemLike item, Holder<Fluid> fluid) {
        ItemStack stack = new ItemStack(item);
        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));

        if (fluidHandler != null) {
            FluidStack fluidStack = new FluidStack(fluid, fluidHandler.getCapacityAsInt(0, FluidResource.of(fluid)));
            stack.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(fluidStack));
            output.accept(stack);
        }
    }

    private static void generateSchematics(CreativeModeTab.Output output, HolderLookup.RegistryLookup<SchematicVariant> schematicLookup) {
        schematicLookup.listElements().sorted(SCHEMATIC_SORTER).forEach(holder -> {
            ItemStack itemstack = GalacticraftItems.SCHEMATIC.toStack();
            itemstack.set(GalacticraftDataComponents.SCHEMATIC, holder);
            output.accept(itemstack);
        });
    }
}
