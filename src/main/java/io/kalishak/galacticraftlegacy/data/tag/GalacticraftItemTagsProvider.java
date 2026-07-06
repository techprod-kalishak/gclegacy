/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftItemTagsProvider extends ItemTagsProvider {
    public GalacticraftItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.AXES)
                .add(GalacticraftItems.DESH_AXE.get())
                .add(GalacticraftItems.STEEL_AXE.get())
                .add(GalacticraftItems.TITANIUM_AXE.get());
        tag(ItemTags.CHEST_ARMOR)
                .add(GalacticraftItems.DESH_CHESTPLATE.get())
                .add(GalacticraftItems.STEEL_CHESTPLATE.get())
                .add(GalacticraftItems.TITANIUM_CHESTPLATE.get());
        tag(ItemTags.FOOT_ARMOR)
                .add(GalacticraftItems.DESH_BOOTS.get())
                .add(GalacticraftItems.STEEL_BOOTS.get())
                .add(GalacticraftItems.TITANIUM_BOOTS.get());
        tag(GalacticraftTags.Items.GEMS_SAPPHIRE)
                .add(GalacticraftItems.SAPPHIRE.get());
        tag(Tags.Items.GEMS)
                .addTag(GalacticraftTags.Items.GEMS_SAPPHIRE);
        tag(ItemTags.HEAD_ARMOR)
                .add(GalacticraftItems.DESH_HELMET.get())
                .add(GalacticraftItems.STEEL_HELMET.get())
                .add(GalacticraftItems.TITANIUM_HELMET.get());
        tag(ItemTags.HOES)
                .add(GalacticraftItems.DESH_HOE.get())
                .add(GalacticraftItems.STEEL_HOE.get())
                .add(GalacticraftItems.TITANIUM_HOE.get());
        tag(ItemTags.LEG_ARMOR)
                .add(GalacticraftItems.DESH_LEGGINGS.get())
                .add(GalacticraftItems.STEEL_LEGGINGS.get())
                .add(GalacticraftItems.TITANIUM_LEGGINGS.get());
        tag(ItemTags.PICKAXES)
                .add(GalacticraftItems.DESH_PICKAXE.get())
                .add(GalacticraftItems.STEEL_PICKAXE.get())
                .add(GalacticraftItems.TITANIUM_PICKAXE.get());
        tag(ItemTags.SHOVELS)
                .add(GalacticraftItems.DESH_SHOVEL.get())
                .add(GalacticraftItems.STEEL_SHOVEL.get())
                .add(GalacticraftItems.TITANIUM_SHOVEL.get());
        tag(ItemTags.SPEARS)
                .add(GalacticraftItems.DESH_SPEAR.get())
                .add(GalacticraftItems.STEEL_SPEAR.get())
                .add(GalacticraftItems.TITANIUM_SPEAR.get());
        tag(GalacticraftTags.Items.STORAGE_BLOCKS_ALUMINUM)
                .add(GalacticraftItems.ALUMINUM_BLOCK.get());
        tag(GalacticraftTags.Items.STORAGE_BLOCKS_RAW_ALUMINUM)
                .add(GalacticraftItems.RAW_ALUMINUM_BLOCK.get());
        tag(GalacticraftTags.Items.STORAGE_BLOCKS_TIN)
                .add(GalacticraftItems.TIN_BLOCK.get());
        tag(GalacticraftTags.Items.STORAGE_BLOCKS_RAW_TIN)
                .add(GalacticraftItems.RAW_TIN_BLOCK.get());
        tag(GalacticraftTags.Items.STORAGE_BLOCKS_RAW_SILICON)
                .add(GalacticraftItems.RAW_SILICON_BLOCK.get());
        tag(ItemTags.SWORDS)
                .add(GalacticraftItems.DESH_SWORD.get())
                .add(GalacticraftItems.STEEL_SWORD.get())
                .add(GalacticraftItems.TITANIUM_SWORD.get());

        tag(Tags.Items.BUCKETS)
                .add(GalacticraftItems.OIL_BUCKET.get())
                .add(GalacticraftItems.FUEL_BUCKET.get());
        tag(Tags.Items.INGOTS)
                .addTag(GalacticraftTags.Items.INGOTS_ALUMINUM)
                .addTag(GalacticraftTags.Items.INGOTS_DESH)
                .addTag(GalacticraftTags.Items.INGOTS_LEAD)
                .addTag(GalacticraftTags.Items.INGOTS_STEEL)
                .addTag(GalacticraftTags.Items.INGOTS_TIN)
                .addTag(GalacticraftTags.Items.INGOTS_TITANIUM);
        tag(Tags.Items.NUGGETS)
                .addTag(GalacticraftTags.Items.NUGGETS_DESH)
                .addTag(GalacticraftTags.Items.NUGGETS_LEAD)
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
                .add(GalacticraftItems.ALUMINUM_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_DESH)
                .add(GalacticraftItems.DESH_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_IRIDIUM)
                .add(GalacticraftItems.METEORIC_IRON_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_LEAD)
                .add(GalacticraftItems.LEAD_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_STEEL)
                .add(GalacticraftItems.STEEL_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_TIN)
                .add(GalacticraftItems.TIN_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_TITANIUM)
                .add(GalacticraftItems.TITANIUM_INGOT.get());
        tag(GalacticraftTags.Items.NUGGETS_DESH)
                .add(GalacticraftItems.DESH_NUGGET.get());
        tag(GalacticraftTags.Items.NUGGETS_STEEL)
                .add(GalacticraftItems.STEEL_NUGGET.get());
        tag(GalacticraftTags.Items.NUGGETS_TITANIUM)
                .add(GalacticraftItems.TITANIUM_NUGGET.get());
        tag(GalacticraftTags.Items.PARACHUTE)
                .add(GalacticraftItems.BLACK_PARACHUTE.get())
                .add(GalacticraftItems.BLUE_PARACHUTE.get())
                .add(GalacticraftItems.BROWN_PARACHUTE.get())
                .add(GalacticraftItems.CYAN_PARACHUTE.get())
                .add(GalacticraftItems.GRAY_PARACHUTE.get())
                .add(GalacticraftItems.LIGHT_BLUE_PARACHUTE.get())
                .add(GalacticraftItems.LIGHT_GRAY_PARACHUTE.get())
                .add(GalacticraftItems.LIME_PARACHUTE.get())
                .add(GalacticraftItems.MAGENTA_PARACHUTE.get())
                .add(GalacticraftItems.ORANGE_PARACHUTE.get())
                .add(GalacticraftItems.PINK_PARACHUTE.get())
                .add(GalacticraftItems.PURPLE_PARACHUTE.get())
                .add(GalacticraftItems.RED_PARACHUTE.get())
                .add(GalacticraftItems.WHITE_PARACHUTE.get())
                .add(GalacticraftItems.YELLOW_PARACHUTE.get());
        tag(GalacticraftTags.Items.PLATE_ALUMINUM)
                .add(GalacticraftItems.COMPRESSED_ALUMINUM.get());
        tag(GalacticraftTags.Items.PLATE_BRONZE)
                .add(GalacticraftItems.COMPRESSED_BRONZE.get());
        tag(GalacticraftTags.Items.PLATE_COPPER)
                .add(GalacticraftItems.COMPRESSED_COPPER.get());
        tag(GalacticraftTags.Items.PLATE_DESH)
                .add(GalacticraftItems.COMPRESSED_DESH.get());
        tag(GalacticraftTags.Items.PLATE_IRON)
                .add(GalacticraftItems.COMPRESSED_IRON.get());
        tag(GalacticraftTags.Items.PLATE_METEORIC_IRON)
                .add(GalacticraftItems.COMPRESSED_METEORIC_IRON.get());
        tag(GalacticraftTags.Items.PLATE_TIN)
                .add(GalacticraftItems.COMPRESSED_TIN.get());
        tag(GalacticraftTags.Items.PLATE_TITANIUM)
                .add(GalacticraftItems.COMPRESSED_TITANIUM.get());
        tag(GalacticraftTags.Items.PLATE_STEEL)
                .add(GalacticraftItems.COMPRESSED_STEEL.get());
        tag(GalacticraftTags.Items.PLATE_HEAVY_DUTY)
                .add(GalacticraftItems.HEAVY_DUTY_PLATE.get());
        tag(GalacticraftTags.Items.PLATE_HEAVY_DUTY_2)
                .add(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_2.get());
        tag(GalacticraftTags.Items.PLATE_HEAVY_DUTY_3)
                .add(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_3.get());
        tag(GalacticraftTags.Items.ORES_ALUMINUM)
                .add(GalacticraftItems.ALUMINUM_ORE.get())
                .add(GalacticraftItems.DEEPSLATE_ALUMINUM_ORE.get());
        tag(GalacticraftTags.Items.ORES_CHEESE)
                .add(GalacticraftItems.MOON_CHEESE_ORE.get());
        tag(GalacticraftTags.Items.ORES_SAPPHIRE)
                .add(GalacticraftItems.MOON_SAPPHIRE_ORE.get());
        tag(GalacticraftTags.Items.ORES_SILICON)
                .add(GalacticraftItems.SILICON_ORE.get())
                .add(GalacticraftItems.DEEPSLATE_SILICON_ORE.get());
        tag(GalacticraftTags.Items.ORES_TIN)
                .add(GalacticraftItems.TIN_ORE.get())
                .add(GalacticraftItems.DEEPSLATE_TIN_ORE.get())
                .add(GalacticraftItems.MOON_TIN_ORE.get());
        tag(Tags.Items.ORES)
                .addTag(GalacticraftTags.Items.ORES_ALUMINUM);
        tag(Tags.Items.ORES)
                .addTag(GalacticraftTags.Items.ORES_CHEESE);
        tag(Tags.Items.ORES)
                .addTag(GalacticraftTags.Items.ORES_SAPPHIRE);
        tag(Tags.Items.ORES)
                .addTag(GalacticraftTags.Items.ORES_SILICON);
        tag(Tags.Items.ORES)
                .addTag(GalacticraftTags.Items.ORES_TIN);
        tag(Tags.Items.ORES_COPPER)
                .add(GalacticraftItems.MOON_COPPER_ORE.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_ALUMINUM)
                .add(GalacticraftItems.RAW_ALUMINUM.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_CHEESE)
                .add(GalacticraftItems.CHEESE_CHUNK.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_DESH)
                .add(GalacticraftItems.RAW_DESH.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_LEAD)
                .add(GalacticraftItems.RAW_LEAD.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_IRIDIUM)
                .add(GalacticraftItems.RAW_METEORIC_IRON.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_SILICON)
                .add(GalacticraftItems.RAW_SILICON.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_STEEL)
                .add(GalacticraftItems.RAW_STEEL.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_TIN)
                .add(GalacticraftItems.RAW_TIN.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_TITANIUM)
                .add(GalacticraftItems.RAW_TITANIUM.get());
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

        tag(ItemTags.SLABS)
                .add(GalacticraftItems.MOON_BRICK_SLAB.get())
                .add(GalacticraftItems.ASTEROID_ROCK_SLAB.get())
                .add(GalacticraftItems.TIN_DECORATION_SLAB.get());
        tag(ItemTags.STAIRS)
                .add(GalacticraftItems.MOON_BRICK_STAIRS.get())
                .add(GalacticraftItems.ASTEROID_ROCK_STAIRS.get())
                .add(GalacticraftItems.TIN_DECORATION_STAIRS.get());
        tag(Tags.Items.STORAGE_BLOCKS)
                .addTag(GalacticraftTags.Items.STORAGE_BLOCKS_ALUMINUM)
                .addTag(GalacticraftTags.Items.STORAGE_BLOCKS_RAW_ALUMINUM)
                .addTag(GalacticraftTags.Items.STORAGE_BLOCKS_TIN)
                .addTag(GalacticraftTags.Items.STORAGE_BLOCKS_RAW_TIN)
                .addTag(GalacticraftTags.Items.STORAGE_BLOCKS_RAW_SILICON);
        tag(ItemTags.WALLS)
                .add(GalacticraftItems.MOON_BRICK_WALL.get())
                .add(GalacticraftItems.ASTEROID_ROCK_WALL.get())
                .add(GalacticraftItems.TIN_DECORATION_WALL.get());

        tag(GalacticraftTags.Items.WRENCH)
                .add(GalacticraftItems.WRENCH.get());
    }
}
