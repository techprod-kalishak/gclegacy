/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.GalacticraftBlockItemIds;
import io.kalishak.galacticraftlegacy.references.GalacticraftItemIds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftItemTagsProvider extends BlockTagCopyingItemTagProvider {
    public GalacticraftItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, Galacticraft.MODID);
    }

    private void copy(BlockItemTagId tagId) {
        copy(tagId.block(), tagId.item());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(GalacticraftTags.BlockItems.BASE_STONE_MOON);
        copy(GalacticraftTags.BlockItems.MACHINE);
        copy(GalacticraftTags.BlockItems.MACHINE_BASIC);
        copy(GalacticraftTags.BlockItems.MACHINE_ADVANCED);
        copy(GalacticraftTags.BlockItems.ORES_ALUMINUM);
        copy(GalacticraftTags.BlockItems.ORES_CHEESE);
        copy(GalacticraftTags.BlockItems.ORES_SAPPHIRE);
        copy(GalacticraftTags.BlockItems.ORES_SILICON);
        copy(GalacticraftTags.BlockItems.ORES_TIN);
        copy(GalacticraftTags.BlockItems.STORAGE_BLOCKS_ALUMINUM);
        copy(GalacticraftTags.BlockItems.STORAGE_BLOCKS_RAW_ALUMINUM);
        copy(GalacticraftTags.BlockItems.STORAGE_BLOCKS_RAW_SILICON);
        copy(GalacticraftTags.BlockItems.STORAGE_BLOCKS_TIN);
        copy(GalacticraftTags.BlockItems.STORAGE_BLOCKS_RAW_TIN);

        tag(ItemTags.SULFUR_CUBE_SWALLOWABLE)
                .addTag(GalacticraftTags.Items.SULFUR_CUBE_ARCHETYPE_SPACY);
        tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
                .add(GalacticraftBlockItemIds.ALUMINUM_BLOCK.item())
                .add(GalacticraftBlockItemIds.RAW_ALUMINUM_BLOCK.item())
                .add(GalacticraftBlockItemIds.TIN_BLOCK.item())
                .add(GalacticraftBlockItemIds.RAW_TIN_BLOCK.item())
                .add(GalacticraftBlockItemIds.RAW_SILICON_BLOCK.item())
                .addTag(GalacticraftTags.BlockItems.ORES_SILICON.item())
                .addTag(GalacticraftTags.BlockItems.ORES_CHEESE.item())
                .addTag(GalacticraftTags.BlockItems.ORES_SAPPHIRE.item())
                .addOptionalTag(GalacticraftTags.BlockItems.ORES_ALUMINUM.item())
                .addOptionalTag(GalacticraftTags.BlockItems.ORES_TIN.item());
        tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
                .add(GalacticraftBlockItemIds.MOON_DUNGEON_BRICKS.item());
        tag(GalacticraftTags.Items.SULFUR_CUBE_ARCHETYPE_SPACY)
                .add(GalacticraftBlockItemIds.MOON_TURF.item())
                .add(GalacticraftBlockItemIds.MOON_DIRT.item())
                .add(GalacticraftBlockItemIds.MOON_ROCK.item());
        tag(ItemTags.AXES)
                .add(GalacticraftItemIds.DESH_AXE)
                .add(GalacticraftItemIds.STEEL_AXE)
                .add(GalacticraftItemIds.TITANIUM_AXE);
        tag(ItemTags.CHEST_ARMOR)
                .add(GalacticraftItemIds.DESH_CHESTPLATE)
                .add(GalacticraftItemIds.STEEL_CHESTPLATE)
                .add(GalacticraftItemIds.TITANIUM_CHESTPLATE);
        tag(ItemTags.FOOT_ARMOR)
                .add(GalacticraftItemIds.DESH_BOOTS)
                .add(GalacticraftItemIds.STEEL_BOOTS)
                .add(GalacticraftItemIds.TITANIUM_BOOTS);
        tag(GalacticraftTags.Items.GEMS_SAPPHIRE)
                .add(GalacticraftItemIds.SAPPHIRE);
        tag(Tags.Items.GEMS)
                .addTag(GalacticraftTags.Items.GEMS_SAPPHIRE);
        tag(ItemTags.HEAD_ARMOR)
                .add(GalacticraftItemIds.DESH_HELMET)
                .add(GalacticraftItemIds.STEEL_HELMET)
                .add(GalacticraftItemIds.TITANIUM_HELMET);
        tag(ItemTags.HOES)
                .add(GalacticraftItemIds.DESH_HOE)
                .add(GalacticraftItemIds.STEEL_HOE)
                .add(GalacticraftItemIds.TITANIUM_HOE);
        tag(ItemTags.LEG_ARMOR)
                .add(GalacticraftItemIds.DESH_LEGGINGS)
                .add(GalacticraftItemIds.STEEL_LEGGINGS)
                .add(GalacticraftItemIds.TITANIUM_LEGGINGS);
        tag(ItemTags.PICKAXES)
                .add(GalacticraftItemIds.DESH_PICKAXE)
                .add(GalacticraftItemIds.STEEL_PICKAXE)
                .add(GalacticraftItemIds.TITANIUM_PICKAXE);
        tag(ItemTags.SHOVELS)
                .add(GalacticraftItemIds.DESH_SHOVEL)
                .add(GalacticraftItemIds.STEEL_SHOVEL)
                .add(GalacticraftItemIds.TITANIUM_SHOVEL);
        tag(ItemTags.SPEARS)
                .add(GalacticraftItemIds.DESH_SPEAR)
                .add(GalacticraftItemIds.STEEL_SPEAR)
                .add(GalacticraftItemIds.TITANIUM_SPEAR);
        tag(ItemTags.SWORDS)
                .add(GalacticraftItemIds.DESH_SWORD)
                .add(GalacticraftItemIds.STEEL_SWORD)
                .add(GalacticraftItemIds.TITANIUM_SWORD);
        tag(Tags.Items.BUCKETS)
                .add(GalacticraftItemIds.OIL_BUCKET)
                .add(GalacticraftItemIds.FUEL_BUCKET);
        tag(Tags.Items.INGOTS)
                .addTag(GalacticraftTags.Items.INGOTS_ALUMINUM)
                .addTag(GalacticraftTags.Items.INGOTS_DESH)
                .addTag(GalacticraftTags.Items.INGOTS_LEAD)
                .addTag(GalacticraftTags.Items.INGOTS_STEEL)
                .addTag(GalacticraftTags.Items.INGOTS_TIN)
                .addTag(GalacticraftTags.Items.INGOTS_TITANIUM);
        tag(Tags.Items.NUGGETS)
                .addTag(GalacticraftTags.Items.NUGGETS_DESH)
                .addTag(GalacticraftTags.Items.NUGGETS_STEEL)
                .addTag(GalacticraftTags.Items.NUGGETS_TITANIUM);
        tag(GalacticraftTags.Items.PLATES)
                .addTag(GalacticraftTags.Items.PLATE_ALUMINUM)
                .addTag(GalacticraftTags.Items.PLATE_BRONZE)
                .addTag(GalacticraftTags.Items.PLATE_COPPER)
                .addTag(GalacticraftTags.Items.PLATE_DESH)
                .addTag(GalacticraftTags.Items.PLATE_IRON)
                .addTag(GalacticraftTags.Items.PLATE_METEORIC_IRON)
                .addTag(GalacticraftTags.Items.PLATE_TIN)
                .addTag(GalacticraftTags.Items.PLATE_TITANIUM)
                .addTag(GalacticraftTags.Items.PLATE_STEEL)
                .addTag(GalacticraftTags.Items.PLATE_HEAVY_DUTY)
                .addTag(GalacticraftTags.Items.PLATE_HEAVY_DUTY_2)
                .addTag(GalacticraftTags.Items.PLATE_HEAVY_DUTY_3);
        tag(Tags.Items.RAW_MATERIALS)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_CHEESE)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_ALUMINUM)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_DESH)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_IRIDIUM)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_LEAD)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_SILICON)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_STEEL)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_TIN)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_TITANIUM);
        tag(Tags.Items.TOOLS)
                .addTag(GalacticraftTags.Items.WRENCH);
        tag(GalacticraftTags.Items.INGOTS_ALUMINUM)
                .add(GalacticraftItemIds.ALUMINUM_INGOT);
        tag(GalacticraftTags.Items.INGOTS_DESH)
                .add(GalacticraftItemIds.DESH_INGOT);
        tag(GalacticraftTags.Items.INGOTS_IRIDIUM)
                .add(GalacticraftItemIds.METEORIC_IRON_INGOT);
        tag(GalacticraftTags.Items.INGOTS_LEAD)
                .add(GalacticraftItemIds.LEAD_INGOT);
        tag(GalacticraftTags.Items.INGOTS_STEEL)
                .add(GalacticraftItemIds.STEEL_INGOT);
        tag(GalacticraftTags.Items.INGOTS_TIN)
                .add(GalacticraftItemIds.TIN_INGOT);
        tag(GalacticraftTags.Items.INGOTS_TITANIUM)
                .add(GalacticraftItemIds.TITANIUM_INGOT);
        tag(GalacticraftTags.Items.NUGGETS_DESH)
                .add(GalacticraftItemIds.DESH_NUGGET);
        tag(GalacticraftTags.Items.NUGGETS_STEEL)
                .add(GalacticraftItemIds.STEEL_NUGGET);
        tag(GalacticraftTags.Items.NUGGETS_TITANIUM)
                .add(GalacticraftItemIds.TITANIUM_NUGGET);
        TagAppender<Item> parachutes = tag(GalacticraftTags.Items.PARACHUTE);
        GalacticraftItemIds.PARACHUTE.map(parachutes::add);
        tag(GalacticraftTags.Items.PLATE_ALUMINUM)
                .add(GalacticraftItemIds.COMPRESSED_ALUMINUM);
        tag(GalacticraftTags.Items.PLATE_BRONZE)
                .add(GalacticraftItemIds.COMPRESSED_BRONZE);
        tag(GalacticraftTags.Items.PLATE_COPPER)
                .add(GalacticraftItemIds.COMPRESSED_COPPER);
        tag(GalacticraftTags.Items.PLATE_DESH)
                .add(GalacticraftItemIds.COMPRESSED_DESH);
        tag(GalacticraftTags.Items.PLATE_IRON)
                .add(GalacticraftItemIds.COMPRESSED_IRON);
        tag(GalacticraftTags.Items.PLATE_METEORIC_IRON)
                .add(GalacticraftItemIds.COMPRESSED_METEORIC_IRON);
        tag(GalacticraftTags.Items.PLATE_TIN)
                .add(GalacticraftItemIds.COMPRESSED_TIN);
        tag(GalacticraftTags.Items.PLATE_TITANIUM)
                .add(GalacticraftItemIds.COMPRESSED_TITANIUM);
        tag(GalacticraftTags.Items.PLATE_STEEL)
                .add(GalacticraftItemIds.COMPRESSED_STEEL);
        tag(GalacticraftTags.Items.PLATE_HEAVY_DUTY)
                .add(GalacticraftItemIds.HEAVY_DUTY_PLATE);
        tag(GalacticraftTags.Items.PLATE_HEAVY_DUTY_2)
                .add(GalacticraftItemIds.T2_HEAVY_DUTY_PLATE);
        tag(GalacticraftTags.Items.PLATE_HEAVY_DUTY_3)
                .add(GalacticraftItemIds.T3_HEAVY_DUTY_PLATE);
        tag(GalacticraftTags.Items.RAW_MATERIALS_ALUMINUM)
                .add(GalacticraftItemIds.RAW_ALUMINUM);
        tag(GalacticraftTags.Items.RAW_MATERIALS_CHEESE)
                .add(GalacticraftItemIds.CHEESE_CHUNK);
        tag(GalacticraftTags.Items.RAW_MATERIALS_DESH)
                .add(GalacticraftItemIds.RAW_DESH);
        tag(GalacticraftTags.Items.RAW_MATERIALS_LEAD)
                .add(GalacticraftItemIds.RAW_LEAD);
        tag(GalacticraftTags.Items.RAW_MATERIALS_IRIDIUM)
                .add(GalacticraftItemIds.RAW_METEORIC_IRON);
        tag(GalacticraftTags.Items.RAW_MATERIALS_SILICON)
                .add(GalacticraftItemIds.RAW_SILICON);
        tag(GalacticraftTags.Items.RAW_MATERIALS_STEEL)
                .add(GalacticraftItemIds.RAW_STEEL);
        tag(GalacticraftTags.Items.RAW_MATERIALS_TIN)
                .add(GalacticraftItemIds.RAW_TIN);
        tag(GalacticraftTags.Items.RAW_MATERIALS_TITANIUM)
                .add(GalacticraftItemIds.RAW_TITANIUM);
        tag(GalacticraftTags.Items.REPAIRS_DESH_ARMOR)
                .addTag(GalacticraftTags.Items.INGOTS_DESH);
        tag(GalacticraftTags.Items.REPAIRS_STEEL_ARMOR)
                .addTag(GalacticraftTags.Items.INGOTS_STEEL);
        tag(GalacticraftTags.Items.REPAIRS_TITANIUM_ARMOR)
                .addTag(GalacticraftTags.Items.INGOTS_TITANIUM);
        tag(GalacticraftTags.Items.REPAIRS_DESH_TOOL)
                .addTag(GalacticraftTags.Items.INGOTS_DESH);
        tag(GalacticraftTags.Items.REPAIRS_STEEL_TOOL)
                .addTag(GalacticraftTags.Items.INGOTS_STEEL);
        tag(GalacticraftTags.Items.REPAIRS_TITANIUM_TOOL)
                .addTag(GalacticraftTags.Items.INGOTS_TITANIUM);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENTS)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_CONE)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_FIN)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_ENGINE)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_BOOSTER)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_WHEEL)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_SEAT)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_PLATING)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_STORAGE)
                .addTag(GalacticraftTags.Items.VEHICLE_INGREDIENT_MISC);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_CONE)
                .add(GalacticraftItemIds.ROCKET_NOSE_CONE)
                .add(GalacticraftItemIds.HEAVY_NOSE_CONE);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_FIN)
                .add(GalacticraftItemIds.ROCKET_FIN)
                .add(GalacticraftItemIds.HEAVY_FIN);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_ENGINE)
                .add(GalacticraftItemIds.ROCKET_ENGINE)
                .add(GalacticraftItemIds.HEAVY_ROCKET_ENGINE);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_BOOSTER)
                .add(GalacticraftItemIds.ROCKET_BOOSTER);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_WHEEL)
                .add(GalacticraftItemIds.BUGGY_WHEEL);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_SEAT)
                .add(GalacticraftItemIds.BUGGY_SEAT);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_PLATING)
                .add(GalacticraftItemIds.HEAVY_DUTY_PLATE)
                .add(GalacticraftItemIds.T2_HEAVY_DUTY_PLATE)
                .add(GalacticraftItemIds.T3_HEAVY_DUTY_PLATE);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_STORAGE)
                .add(GalacticraftItemIds.BUGGY_STORAGE_BOX)
                .addTag(Tags.Items.CHESTS);
        tag(GalacticraftTags.Items.VEHICLE_INGREDIENT_MISC)
                .add(GalacticraftItemIds.ADVANCED_WAFER)
                .add(GalacticraftItemIds.COMPRESSED_ALUMINUM)
                .add(GalacticraftItemIds.STEEL_POLE);
        tag(GalacticraftTags.Items.WRENCH)
                .add(GalacticraftItemIds.WRENCH);
    }
}
