/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.client.item.ColorByFluid;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.numeric.DungeonLocatorAngle;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.range.FluidAmountProperty;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.select.SchematicTierProperty;
import io.kalishak.galacticraftlegacy.client.data.models.model.GalacticraftTexturedModel;
import io.kalishak.galacticraftlegacy.client.renderer.special.KeySpecialRenderer;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.GearEquipmentAssets;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.equipment.trim.GalacticraftMaterialAssetGroup;
import io.kalishak.galacticraftlegacy.world.item.equipment.trim.GalacticraftTrimMaterials;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.wire.HeavyWireBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class GalacticraftModelProvider extends ModelProvider {
    public static final List<ItemModelGenerators.TrimMaterialData> TRIM_MATERIAL_MODELS = List.of(
            new ItemModelGenerators.TrimMaterialData(GalacticraftMaterialAssetGroup.STEEL, GalacticraftTrimMaterials.STEEL),
            new ItemModelGenerators.TrimMaterialData(GalacticraftMaterialAssetGroup.CHEESE, GalacticraftTrimMaterials.CHEESE),
            new ItemModelGenerators.TrimMaterialData(GalacticraftMaterialAssetGroup.DESH, GalacticraftTrimMaterials.DESH),
            new ItemModelGenerators.TrimMaterialData(GalacticraftMaterialAssetGroup.TITANIUM, GalacticraftTrimMaterials.TITANIUM),
            new ItemModelGenerators.TrimMaterialData(GalacticraftMaterialAssetGroup.LEAD, GalacticraftTrimMaterials.LEAD)
    );

    public GalacticraftModelProvider(PackOutput output) {
        super(output, Galacticraft.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
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
        litMachine(blockModels, GalacticraftBlocks.COAL_GENERATOR.get());
        machine(blockModels, GalacticraftBlocks.CIRCUIT_FABRICATOR.get());
        machine(blockModels, GalacticraftBlocks.ELECTRIC_FURNACE.get());
        blockModels.createNonTemplateModelBlock(GalacticraftBlocks.OIL.get());
        blockModels.createNonTemplateModelBlock(GalacticraftBlocks.FUEL.get());
        blockModels.family(GalacticraftBlocks.MOON_BRICKS.get()).generateFor(GalacticraftBlockFamilies.MOON_BRICKS);
        machine(blockModels, GalacticraftBlocks.COMPRESSOR.get());
        machine(blockModels, GalacticraftBlocks.ELECTRIC_COMPRESSOR.get());

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
        grating(blockModels, GalacticraftBlocks.GRATING.get());
        cheese(blockModels, GalacticraftBlocks.CHEESE.get());
        blockModels.createTrivialBlock(
                GalacticraftBlocks.OXYGEN_DETECTOR.get(),
                TexturedModel.createDefault(
                        block -> TextureMapping.column(TextureMapping.getBlockTexture(block, "_side"), new Material(Constants.id("block/machine_top"))),
                        ModelTemplates.CUBE_COLUMN
                )
        );
        pipeLike(blockModels, GalacticraftBlocks.ALUMINUM_WIRE.get());
        pipeLike(blockModels, GalacticraftBlocks.HEAVY_ALUMINUM_WIRE.get());
        pipeLike(blockModels, GalacticraftBlocks.WHITE_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.ORANGE_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.MAGENTA_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.LIGHT_BLUE_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.YELLOW_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.LIME_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.PINK_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.GRAY_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.LIGHT_GRAY_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.CYAN_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.PURPLE_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.BLUE_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.BROWN_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.GREEN_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.RED_PIPE.get());
        pipeLike(blockModels, GalacticraftBlocks.BLACK_PIPE.get());
        createCauldron(blockModels, GalacticraftBlocks.OIL_CAULDRON.get(), GalacticraftBlocks.OIL.get());
        createCauldron(blockModels, GalacticraftBlocks.FUEL_CAULDRON.get(), GalacticraftBlocks.FUEL.get());
        blockModels.createNormalTorch(GalacticraftBlocks.UNLIT_TORCH.get(), GalacticraftBlocks.UNLIT_WALL_TORCH.get());
        blockModels.createNormalTorch(GalacticraftBlocks.UNLIT_COPPER_TORCH.get(), GalacticraftBlocks.UNLIT_COPPER_WALL_TORCH.get());
        blockModels.createLantern(GalacticraftBlocks.UNLIT_LANTERN.get());
        GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxedMapping().forEach(blockModels::createCopperLantern);

        itemModels.generateFlatItem(GalacticraftItems.BATTERY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.INFINITE_BATTERY.get(), GalacticraftItems.BATTERY.get(), ModelTemplates.FLAT_ITEM);
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
        itemModels.generateFlatItem(GalacticraftItems.BLACK_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BLUE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BROWN_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.CYAN_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.GRAY_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.GREEN_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LIGHT_BLUE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LIGHT_GRAY_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LIME_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.MAGENTA_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ORANGE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.PINK_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.PURPLE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RED_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.WHITE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.YELLOW_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.SENSOR_GLASSES.get(),  ModelTemplates.FLAT_ITEM);
        generateDungeonLocator(itemModels, GalacticraftItems.DUNGEON_LOCATOR.get());
        itemModels.generateFlatItem(GalacticraftItems.RAW_ALUMINUM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ALUMINUM_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RAW_TIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.TIN_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RAW_SILICON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.SAPPHIRE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BASIC_WAFER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ADVANCED_WAFER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.SOLAR_WAFER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.FLAG.get(), ModelTemplates.FLAT_ITEM);
        schematic(itemModels, GalacticraftItems.SCHEMATIC.get());
        fluidTank(itemModels, GalacticraftItems.FLUID_TANK.get());
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
        itemModels.generateFlatItem(GalacticraftItems.LEAD_NUGGET.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.CHEESE_CHUNK.get(),  ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.CHEESE_SLICE.get(), ModelTemplates.FLAT_ITEM);
        createKey(itemModels, GalacticraftItems.MOON_DUNGEON_KEY.get(), FeatureTier.TIER_1);
        createKey(itemModels, GalacticraftItems.MARS_DUNGEON_KEY.get(), FeatureTier.TIER_2);
        createKey(itemModels, GalacticraftItems.VENUS_DUNGEON_KEY.get(), FeatureTier.TIER_3);
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
    }

    private void grating(BlockModelGenerators blockModels, Block gratingBlock) {
        blockModels.registerSimpleFlatItemModel(gratingBlock.asItem());

        Material textures = TextureMapping.getBlockTexture(gratingBlock);
        TextureMapping mapping = new TextureMapping().put(TextureSlot.PARTICLE,  textures).put(TextureSlot.TEXTURE, textures);
        Identifier modelId = ModelTemplates.create(TextureSlot.PARTICLE, TextureSlot.TEXTURE)
                .extend()
                .parent(Constants.id("block/grating_template"))
                .build()
                .create(gratingBlock, mapping, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(gratingBlock, BlockModelGenerators.plainVariant(modelId)));
    }

    private void cheese(BlockModelGenerators blockModels, Block cheeseBlock) {
        blockModels.createCakeBlock();
        blockModels.registerSimpleFlatItemModel(cheeseBlock.asItem());
        TextureMapping mainMapping = new TextureMapping()
                .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(cheeseBlock, "_side"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(cheeseBlock, "_top"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(cheeseBlock, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(cheeseBlock, "_side"));
        TextureMapping innerMapping = mainMapping
                .copy()
                .put(TextureSlot.INSIDE, TextureMapping.getBlockTexture(cheeseBlock, "_inner"));
        Identifier mainModel = ModelTemplates.create(TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE)
                .extend()
                .parent(Identifier.withDefaultNamespace("block/cake"))
                .build()
                .create(cheeseBlock, mainMapping, blockModels.modelOutput);
        Function<Integer, Identifier> gen = bitesCount -> ModelTemplates.create("cheese", "_slice" + bitesCount, TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE)
                .extend()
                .parent(Identifier.withDefaultNamespace("block/cake_slice" + bitesCount))
                .build().create(cheeseBlock, innerMapping, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(cheeseBlock)
                .with(PropertyDispatch.initial(BlockStateProperties.BITES)
                        .select(0, BlockModelGenerators.plainVariant(mainModel))
                        .select(1, BlockModelGenerators.plainVariant(gen.apply(1)))
                        .select(2, BlockModelGenerators.plainVariant(gen.apply(2)))
                        .select(3, BlockModelGenerators.plainVariant(gen.apply(3)))
                        .select(4, BlockModelGenerators.plainVariant(gen.apply(4)))
                        .select(5, BlockModelGenerators.plainVariant(gen.apply(5)))
                        .select(6, BlockModelGenerators.plainVariant(gen.apply(6)))
                )
        );
    }

    public void schematic(ItemModelGenerators gen, Item item) {
        Identifier itemModel = ModelLocationUtils.getModelLocation(item);
        ItemModel.Unbaked tier1_model = ItemModelUtils.plainModel(gen.createFlatItemModel(item, "_tier_1", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked tier2_model = ItemModelUtils.plainModel(gen.createFlatItemModel(item, "_tier_2", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked tier3_model = ItemModelUtils.plainModel(gen.createFlatItemModel(item, "_tier_3", ModelTemplates.FLAT_ITEM));

        ModelTemplates.FLAT_ITEM.create(itemModel, TextureMapping.layer0(item), gen.modelOutput);

        gen.itemModelOutput.accept(
                item,
                ItemModelUtils.conditional(
                        ItemModelUtils.hasComponent(GalacticraftDataComponents.SCHEMATIC.get()),
                        ItemModelUtils.select(
                                new SchematicTierProperty(),
                                ItemModelUtils.when(FeatureTier.TIER_1, tier1_model),
                                ItemModelUtils.when(FeatureTier.TIER_2, tier2_model),
                                ItemModelUtils.when(FeatureTier.TIER_3, tier3_model)
                        ),
                        tier1_model
                )
        );
    }

    public void fluidTank(ItemModelGenerators gen, Item item) {
        ItemModel.Unbaked emptyTank = ItemModelUtils.plainModel(gen.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked partialModel_1 = ItemModelUtils.tintedModel(gen.createFlatItemModel(item, "_partial_1", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_2 = ItemModelUtils.tintedModel(gen.createFlatItemModel(item, "_partial_2", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_3 = ItemModelUtils.tintedModel(gen.createFlatItemModel(item, "_partial_3", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_4 = ItemModelUtils.tintedModel(gen.createFlatItemModel(item, "_partial_4", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_5 = ItemModelUtils.tintedModel(gen.createFlatItemModel(item, "_partial_5", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_6 = ItemModelUtils.tintedModel(gen.createFlatItemModel(item, "_partial_6", ModelTemplates.FLAT_ITEM), new ColorByFluid());

        gen.itemModelOutput.accept(
                item,
                ItemModelUtils.composite(
                        emptyTank,
                        ItemModelUtils.rangeSelect(
                                new FluidAmountProperty(),
                                emptyTank,
                                ItemModelUtils.override(partialModel_1, 0.13F),
                                ItemModelUtils.override(partialModel_2, 0.28F),
                                ItemModelUtils.override(partialModel_3, 0.42F),
                                ItemModelUtils.override(partialModel_4, 0.57F),
                                ItemModelUtils.override(partialModel_5, 0.71F),
                                ItemModelUtils.override(partialModel_6, 0.85F)
                        )
                )
        );
    }

    public void generateDungeonLocator(ItemModelGenerators gen, Item item) {
        gen.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.rangeSelect(new DungeonLocatorAngle(), 32.0F, gen.createCompassModels(item))
                );
    }

    private void litMachine(BlockModelGenerators gen, Block block) {
        MultiVariant regularVariant = BlockModelGenerators.plainVariant(GalacticraftTexturedModel.SIMPLE_MACHINE.create(block, gen.modelOutput));
        Material litTexture = TextureMapping.getBlockTexture(block, "_front_on");
        MultiVariant litVariant = BlockModelGenerators.plainVariant(GalacticraftTexturedModel.SIMPLE_MACHINE.get(block).updateTextures(mapping -> mapping.put(TextureSlot.NORTH, litTexture)).createWithSuffix(block, "_on", gen.modelOutput));
        gen.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, regularVariant))
                        .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }
    private void machine(BlockModelGenerators gen, Block block) {
        gen.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(
                        block,
                        BlockModelGenerators.plainVariant(GalacticraftTexturedModel.SIMPLE_MACHINE.create(block, gen.modelOutput))
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }

    private Identifier generatePipeBaseModel(Block block, BiConsumer<Identifier, ModelInstance> maker) {
        Identifier parent = Constants.id("block/pipe_" + (block instanceof HeavyWireBlock ? "dense_" : "") + "template");
        ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder().parent(parent).requiredTextureSlot(TextureSlot.TEXTURE);

        return builder.build().create(block, TextureMapping.defaultTexture(block), maker);
    }

    private Identifier generatePipeLegModel(Block block, Direction direction, BiConsumer<Identifier, ModelInstance> maker) {
        Identifier parent = Constants.id("block/pipe_" + (block instanceof HeavyWireBlock ? "dense_" : "") + "leg_template");
        ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder().parent(parent).requiredTextureSlot(TextureSlot.TEXTURE);

        return builder.build().createWithSuffix(block, "_" + direction.getName(), TextureMapping.defaultTexture(block), maker);
    }

    private void pipeLike(BlockModelGenerators gen, Block block) {
        Identifier baseModel = generatePipeBaseModel(block, gen.modelOutput);
        gen.registerSimpleFlatItemModel(block.asItem());
        Map<Direction, Identifier> modelPerFace = new EnumMap<>(Direction.class);

        for (Direction direction : Direction.values()) {
            modelPerFace.put(direction, generatePipeLegModel(block, direction, gen.modelOutput));
        }

        gen.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(plainVariant(baseModel))
                .with(
                        condition()
                                .term(BlockStateProperties.NORTH, true),
                        plainVariant(modelPerFace.get(Direction.NORTH))
                )
                .with(
                        condition()
                                .term(BlockStateProperties.EAST, true),
                        plainVariant(modelPerFace.get(Direction.EAST))
                                .with(Y_ROT_90)
                )
                .with(
                        condition()
                                .term(BlockStateProperties.SOUTH, true),
                        plainVariant(modelPerFace.get(Direction.EAST))
                                .with(Y_ROT_180)
                )
                .with(
                        condition()
                                .term(BlockStateProperties.WEST, true),
                        plainVariant(modelPerFace.get(Direction.EAST))
                                .with(Y_ROT_270)
                )
                .with(
                        condition()
                                .term(BlockStateProperties.UP, true),
                        plainVariant(modelPerFace.get(Direction.UP))
                                .with(X_ROT_270)
                )
                .with(
                        condition()
                                .term(BlockStateProperties.DOWN, true),
                        plainVariant(modelPerFace.get(Direction.UP))
                                .with(X_ROT_90)
                )
        );
    }

    private void createKey(ItemModelGenerators itemModelGenerators, Item key, FeatureTier featureTier) {
        Identifier model = itemModelGenerators.createFlatItemModel(key, ExtendedModelTemplateBuilder.builder().parent(Constants.id("item/key_template")).build());
        ItemModel.Unbaked unbakedModel = ItemModelUtils.specialModel(model, new KeySpecialRenderer.Unbaked(featureTier));
        itemModelGenerators.itemModelOutput.accept(key, unbakedModel);
    }

    private void createCauldron(BlockModelGenerators gen, Block block, Block liquidBlock) {
        gen.blockStateOutput.accept(createSimpleBlock(
                block,
                plainVariant(ModelTemplates.CAULDRON_FULL.create(block, TextureMapping.cauldron(TextureMapping.getBlockTexture(liquidBlock, "_still")), gen.modelOutput)))
        );
    }
}
