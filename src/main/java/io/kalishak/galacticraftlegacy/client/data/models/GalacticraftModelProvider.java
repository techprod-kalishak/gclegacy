/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data.models;

import io.kalishak.galacticraftlegacy.aunified.data.model.ExtendedModelProvider;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftBlockFamilies;
import io.kalishak.galacticraftlegacy.client.data.models.model.*;
import io.kalishak.galacticraftlegacy.client.renderer.special.*;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.GearEquipmentAssets;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

public class GalacticraftModelProvider extends ExtendedModelProvider<GalacticraftBlockModelGenerators, GalacticraftItemModelGenerators> {
    public GalacticraftModelProvider(PackOutput output) {
        super(output, Galacticraft.MODID, GalacticraftBlockModelGenerators::new, GalacticraftItemModelGenerators::new);
    }

    @Override
    protected void registerExtendedModels(GalacticraftBlockModelGenerators blockModels, GalacticraftItemModelGenerators itemModels) {
        blockModels.createTrivialCube(GalacticraftBlocks.ALUMINUM_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.ALUMINUM_BLOCK.get());
        blockModels.createTrivialCube(GalacticraftBlocks.RAW_ALUMINUM_BLOCK.get());
        blockModels.createTrivialCube(GalacticraftBlocks.TIN_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.DEEPSLATE_TIN_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.TIN_BLOCK.get());
        blockModels.createTrivialCube(GalacticraftBlocks.RAW_TIN_BLOCK.get());
        blockModels.createTrivialCube(GalacticraftBlocks.SILICON_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.DEEPSLATE_SILICON_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.RAW_SILICON_BLOCK.get());
        blockModels.createLitMachine(GalacticraftBlocks.COAL_GENERATOR.get());
        blockModels.createRotationalMachine(GalacticraftTexturedModel.BASIC_MACHINE, GalacticraftBlocks.CIRCUIT_FABRICATOR.get());
        blockModels.createRotationalMachine(GalacticraftTexturedModel.BASIC_MACHINE, GalacticraftBlocks.ELECTRIC_FURNACE.get());
        blockModels.createRotationalMachine(GalacticraftTexturedModel.ADVANCED_MACHINE, GalacticraftBlocks.ELECTRIC_ARC_FURNACE.get());
        blockModels.createNonTemplateModelBlock(GalacticraftBlocks.OIL.get());
        blockModels.createNonTemplateModelBlock(GalacticraftBlocks.FUEL.get());
        GalacticraftBlockFamilies.getFamilies().forEach(blockFamily -> blockModels.family(blockFamily.getBaseBlock()).generateFor(blockFamily));
        blockModels.createRotationalMachine(GalacticraftTexturedModel.BASIC_MACHINE, GalacticraftBlocks.COMPRESSOR.get());
        blockModels.createRotationalMachine(GalacticraftTexturedModel.ADVANCED_MACHINE, GalacticraftBlocks.ELECTRIC_COMPRESSOR.get());
        blockModels.createTrivialCube(GalacticraftBlocks.MOON_DIRT.get());
        blockModels.createRotatedMirroredVariantBlock(GalacticraftBlocks.MOON_TURF.get());
        blockModels.createTrivialCube(GalacticraftBlocks.MOON_ROCK.get());
        blockModels.createTrivialCube(GalacticraftBlocks.MOON_CHEESE_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.MOON_COPPER_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.MOON_TIN_ORE.get());
        blockModels.createTrivialCube(GalacticraftBlocks.MOON_SAPPHIRE_ORE.get());
        blockModels.createNonTemplateModelBlock(GalacticraftBlocks.EMPTY_AIR.get(), Blocks.AIR);
        blockModels.createNonTemplateModelBlock(GalacticraftBlocks.OXYGEN_AIR.get(), Blocks.AIR);
        blockModels.createParticleOnlyBlock(GalacticraftBlocks.PARACHEST.get(), Blocks.OAK_PLANKS);
        blockModels.createParticleOnlyBlock(GalacticraftBlocks.PARACHEST_18.get(), Blocks.OAK_PLANKS);
        blockModels.createParticleOnlyBlock(GalacticraftBlocks.PARACHEST_36.get(), Blocks.OAK_PLANKS);
        blockModels.createParticleOnlyBlock(GalacticraftBlocks.PARACHEST_54.get(), Blocks.OAK_PLANKS);
        blockModels.createParticleOnlyBlock(GalacticraftBlocks.MOON_DUNGEON_CHEST.get(), Blocks.OAK_PLANKS);
        blockModels.createParticleOnlyBlock(GalacticraftBlocks.MARS_DUNGEON_CHEST.get(), Blocks.OAK_PLANKS);
        blockModels.createParticleOnlyBlock(GalacticraftBlocks.VENUS_DUNGEON_CHEST.get(), Blocks.OAK_PLANKS);
        blockModels.createCheeseBlock(GalacticraftBlocks.CHEESE.get());
        blockModels.createTrivialBlock(
                GalacticraftBlocks.OXYGEN_DETECTOR.get(),
                TexturedModel.createDefault(
                        block -> TextureMapping.column(TextureMapping.getBlockTexture(block, "_side"), new Material(Constants.id("block/machine_top"))),
                        ModelTemplates.CUBE_COLUMN
                )
        );
        GalacticraftBlocks.FLUID_PIPE.forEach(fluidPipeBlock -> blockModels.pipeLike(fluidPipeBlock.get()));
        blockModels.pipeLike(GalacticraftBlocks.ALUMINUM_WIRE.get());
        blockModels.pipeLike(GalacticraftBlocks.HEAVY_ALUMINUM_WIRE.get());
        blockModels.createCauldron(GalacticraftBlocks.OIL_CAULDRON.get(), GalacticraftBlocks.OIL.get());
        blockModels.createCauldron(GalacticraftBlocks.FUEL_CAULDRON.get(), GalacticraftBlocks.FUEL.get());
        blockModels.createNormalTorch(GalacticraftBlocks.UNLIT_TORCH.get(), GalacticraftBlocks.UNLIT_WALL_TORCH.get());
        blockModels.createNormalTorch(GalacticraftBlocks.UNLIT_COPPER_TORCH.get(), GalacticraftBlocks.UNLIT_COPPER_WALL_TORCH.get());
        blockModels.createLantern(GalacticraftBlocks.UNLIT_LANTERN.get());
        GalacticraftBlocks.UNLIT_COPPER_LANTERN.zipUnwaxedWaxed((unwaxed, waxed) -> blockModels.createCopperLantern(unwaxed.get(), waxed.get()));
        blockModels.createMagneticCraftingTable(GalacticraftBlocks.MAGNETIC_CRAFTING_TABLE.get());
        blockModels.createRotationalMachine(GalacticraftTexturedModel.OXYGEN_COLLECTOR, GalacticraftBlocks.OXYGEN_COLLECTOR.get());
        blockModels.createTrivialCube(GalacticraftBlocks.ASTEROID_ALUMINUM_ORE.get());
        blockModels.createTrivialBlock(GalacticraftBlocks.TIN_DECORATION_CUT_BLOCK.get(), TexturedModel.CUBE_TOP_BOTTOM);
        blockModels.createTrivialCube(GalacticraftBlocks.SPACE_STATION.get());
        blockModels.noBlockGen(GalacticraftBlocks.COMPACT_NASA_WORKBENCH.get());
        blockModels.createNasaWorkbench(GalacticraftBlocks.NASA_WORKBENCH.get());
        blockModels.createNonTemplateModelBlock(GalacticraftBlocks.GRATING.get());
        blockModels.createMeteor(GalacticraftBlocks.FALLEN_METEOR.get());
        blockModels.createPad(GalacticraftBlocks.LANDING_PAD.get());
        blockModels.createPad(GalacticraftBlocks.FUELING_PAD.get());
        blockModels.createNonTemplateModelBlock(GalacticraftBlocks.ASTRO_MINER_BASE.get());
        GalacticraftBlocks.COLORED_TINTED_GLASS_PANE.forEach(block -> blockModels.createBarsAndItem(block.get()));
        blockModels.createBarsAndItem(GalacticraftBlocks.TINTED_GLASS_PANE.get(), TextureMapping.bars(Blocks.TINTED_GLASS));

        blockModels.registerSimpleFlatItemModel(GalacticraftBlocks.GRATING.get());
        itemModels.generateFlatItem(GalacticraftItems.THROWABLE_METEOR_CHUNK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BATTERY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.INFINITE_BATTERY.get(), GalacticraftItems.BATTERY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.FREQUENCY_MODULE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_CLOTH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_PADDING_HELM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_PADDING_CHESTPIECE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_PADDING_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_PADDING_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ISOTHERMAL_FABRIC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ISOTHERMAL_HELM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ISOTHERMAL_CHESTPIECE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ISOTHERMAL_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ISOTHERMAL_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LIGHT_TANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.MEDIUM_TANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.HEAVY_TANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.INFINITE_OXYGEN_TANK.get(), GalacticraftItems.HEAVY_TANK.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.OXYGEN_MASK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.OXYGEN_GEAR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.PROTO_SHIELD_CONTROLLER.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.SHIELD_CONTROLLER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.WRENCH.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        GalacticraftItems.PARACHUTE.forEach(parachute -> itemModels.generateFlatItem(parachute.get(), ModelTemplates.FLAT_ITEM));
        itemModels.generateFlatItem(GalacticraftItems.SENSOR_GLASSES.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.createDungeonLocator(GalacticraftItems.DUNGEON_LOCATOR.get());
        itemModels.generateFlatItem(GalacticraftItems.RAW_ALUMINUM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ALUMINUM_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RAW_TIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TIN_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RAW_SILICON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.SAPPHIRE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BASIC_WAFER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ADVANCED_WAFER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.SOLAR_WAFER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.SINGLE_SOLAR_MODULE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.FULL_SOLAR_PANEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.OXYGEN_VENT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_CONTROLLER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.CANVAS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.FLAG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.createSchematic(GalacticraftItems.SCHEMATIC.get());
        itemModels.createFluidTank(GalacticraftItems.FLUID_TANK.get());
        itemModels.generateFlatItem(GalacticraftItems.OIL_BUCKET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.FUEL_BUCKET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RAW_STEEL.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_INGOT.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_NUGGET.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_SWORD.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateSpear(GalacticraftItems.STEEL_SPEAR.get());
        itemModels.generateFlatItem(GalacticraftItems.STEEL_SHOVEL.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_PICKAXE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_AXE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_HOE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateTrimmableItem(GalacticraftItems.STEEL_HELMET.get(), GearEquipmentAssets.STEEL, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(GalacticraftItems.STEEL_CHESTPLATE.get(), GearEquipmentAssets.STEEL, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(GalacticraftItems.STEEL_LEGGINGS.get(), GearEquipmentAssets.STEEL, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(GalacticraftItems.STEEL_BOOTS.get(), GearEquipmentAssets.STEEL, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_HORSE_ARMOR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_NAUTILUS_ARMOR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RAW_DESH.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DESH_INGOT.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DESH_NUGGET.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DESH_SWORD.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateSpear(GalacticraftItems.DESH_SPEAR.get());
        itemModels.generateFlatItem(GalacticraftItems.DESH_SHOVEL.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DESH_PICKAXE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DESH_AXE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DESH_HOE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateTrimmableItem(GalacticraftItems.DESH_HELMET.get(), GearEquipmentAssets.DESH, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(GalacticraftItems.DESH_CHESTPLATE.get(), GearEquipmentAssets.DESH, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(GalacticraftItems.DESH_LEGGINGS.get(), GearEquipmentAssets.DESH, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(GalacticraftItems.DESH_BOOTS.get(), GearEquipmentAssets.DESH, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateFlatItem(GalacticraftItems.RAW_TITANIUM.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TITANIUM_INGOT.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TITANIUM_NUGGET.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TITANIUM_SWORD.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateSpear(GalacticraftItems.TITANIUM_SPEAR.get());
        itemModels.generateFlatItem(GalacticraftItems.TITANIUM_SHOVEL.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TITANIUM_PICKAXE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TITANIUM_AXE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TITANIUM_HOE.get(),  ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateTrimmableItem(GalacticraftItems.TITANIUM_HELMET.get(), GearEquipmentAssets.TITANIUM, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(GalacticraftItems.TITANIUM_CHESTPLATE.get(), GearEquipmentAssets.TITANIUM, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(GalacticraftItems.TITANIUM_LEGGINGS.get(), GearEquipmentAssets.TITANIUM, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(GalacticraftItems.TITANIUM_BOOTS.get(), GearEquipmentAssets.TITANIUM, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateFlatItem(GalacticraftItems.RAW_LEAD.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LEAD_INGOT.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.CHEESE_CHUNK.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.CHEESE_SLICE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.createKey(GalacticraftItems.MOON_DUNGEON_KEY.get(), FeatureTier.TIER_1);
        itemModels.createKey(GalacticraftItems.MARS_DUNGEON_KEY.get(), FeatureTier.TIER_2);
        itemModels.createKey(GalacticraftItems.VENUS_DUNGEON_KEY.get(), FeatureTier.TIER_3);
        itemModels.generateFlatItem(GalacticraftItems.DEHYDRATED_APPLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DEHYDRATED_CARROT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DEHYDRATED_MELON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DEHYDRATED_PUMPKIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DEHYDRATED_POTATO.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DEHYDRATED_BEETROOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.CANNED_BEEF.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TIN_CANISTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.EVOLVED_SKELETON_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.EVOLVED_ZOMBIE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RAW_METEORIC_IRON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.METEORIC_IRON_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.STEEL_POLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_ALUMINUM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_BRONZE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_COPPER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_DESH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_IRON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_METEORIC_IRON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_TIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_TITANIUM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.COMPRESSED_STEEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.HEAVY_DUTY_PLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_2.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ROCKET_NOSE_CONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ROCKET_FIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ROCKET_ENGINE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ROCKET_BOOSTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.HEAVY_NOSE_CONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.HEAVY_FIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.HEAVY_ROCKET_ENGINE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BUGGY_SEAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BUGGY_WHEEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BUGGY_STORAGE_BOX.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ORION_DRIVE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.createVehicleLike(GalacticraftItems.TIER_1_ROCKET.get(), new Tier1RocketSpecialRenderer.Unbaked());
        itemModels.createVehicleLike(GalacticraftItems.BUGGY.get(), new BuggySpecialRenderer.Unbaked());
        itemModels.createVehicleLike(GalacticraftItems.TIER_2_ROCKET.get(), new Tier2RocketSpecialRenderer.Unbaked());
        itemModels.createVehicleLike(GalacticraftItems.CARGO_ROCKET.get(), new CargoRocketSpecialRenderer.Unbaked());
        itemModels.createVehicleLike(GalacticraftItems.TIER_3_ROCKET.get(), new Tier3RocketSpecialRenderer.Unbaked());
        itemModels.createVehicleLike(GalacticraftItems.ASTRO_MINER.get(), new AstroMinerSpecialRenderer.Unbaked());
    }
}
