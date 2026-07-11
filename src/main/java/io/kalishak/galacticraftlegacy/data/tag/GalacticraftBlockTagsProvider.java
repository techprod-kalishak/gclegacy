/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.GalacticraftBlockIds;
import io.kalishak.galacticraftlegacy.references.GalacticraftBlockItemIds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BlockItemTagAppender;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftBlockTagsProvider extends BlockTagsProvider {
    public GalacticraftBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    protected BlockItemTagAppender<Block> tag(BlockItemTagId tagId) {
        return new BlockItemTagAppender<>(super.tag(tagId.block())) {
            @Override
            protected ResourceKey<Block> convertElement(BlockItemId element) {
                return element.block();
            }
        };
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Blocks.INFINIBURN_OPEN_SPACE)
                .add(GalacticraftBlockIds.EMPTY_AIR);
        tag(GalacticraftTags.Blocks.METEOR_BLOCK_REPLACEABLE)
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK.block())
                .add(GalacticraftBlockItemIds.ASTEROID_ALUMINUM_ORE.block());
        tag(GalacticraftTags.Blocks.BASE_STONE_ASTEROID)
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK.block());
        tag(GalacticraftTags.BlockItems.BASE_STONE_MOON)
                .add(GalacticraftBlockItemIds.MOON_TURF)
                .add(GalacticraftBlockItemIds.MOON_ROCK);
        tag(GalacticraftTags.Blocks.MOON_CARVER_REPLACEABLES)
                .addTag(GalacticraftTags.Blocks.BASE_STONE_MOON)
                .add(GalacticraftBlockItemIds.MOON_DIRT.block());
        tag(GalacticraftTags.Blocks.BREATHABLE_AIR)
                .add(BlockIds.VOID_AIR)
                .add(BlockIds.CAVE_AIR)
                .add(GalacticraftBlockIds.OXYGEN_AIR);
        tag(GalacticraftTags.Blocks.CRUDE_OIL_POOL_REPLACEABLE)
                .addTag(BlockTags.BASE_STONE_OVERWORLD)
                .addTag(BlockTags.SAND)
                .add(BlockItemIds.SANDSTONE.block());
        BlockItemTagAppender<Block> lanternTag = tag(BlockItemTags.LANTERNS)
                .add(GalacticraftBlockItemIds.UNLIT_LANTERN);
        GalacticraftBlockItemIds.UNLIT_COPPER_LANTERN.forEach(lanternTag::add);
        tag(GalacticraftTags.Blocks.MACHINE)
                .addTag(GalacticraftTags.Blocks.MACHINE_BASIC)
                .addTag(GalacticraftTags.Blocks.MACHINE_ADVANCED);
        tag(GalacticraftTags.BlockItems.MACHINE_BASIC)
                .add(GalacticraftBlockItemIds.COAL_GENERATOR)
                .add(GalacticraftBlockItemIds.OXYGEN_DETECTOR)
                .add(GalacticraftBlockItemIds.CIRCUIT_FABRICATOR)
                .add(GalacticraftBlockItemIds.COMPRESSOR)
                .add(GalacticraftBlockItemIds.OXYGEN_COLLECTOR);
        tag(GalacticraftTags.BlockItems.MACHINE_ADVANCED)
                .add(GalacticraftBlockItemIds.ELECTRIC_FURNACE)
                .add(GalacticraftBlockItemIds.ELECTRIC_ARC_FURNACE);
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(GalacticraftBlockItemIds.COAL_GENERATOR.block())
                .add(GalacticraftBlockItemIds.CIRCUIT_FABRICATOR.block())
                .add(GalacticraftBlockItemIds.COMPRESSOR.block())
                .add(GalacticraftBlockItemIds.ELECTRIC_COMPRESSOR.block())
                .add(GalacticraftBlockItemIds.OXYGEN_DETECTOR.block())
                .add(GalacticraftBlockItemIds.ELECTRIC_FURNACE.block())
                .add(GalacticraftBlockItemIds.ELECTRIC_ARC_FURNACE.block())
                .add(GalacticraftBlockItemIds.MOON_COPPER_ORE.block())
                .add(GalacticraftBlockItemIds.MOON_CHEESE_ORE.block())
                .add(GalacticraftBlockItemIds.TIN_ORE.block())
                .add(GalacticraftBlockItemIds.DEEPSLATE_TIN_ORE.block())
                .add(GalacticraftBlockItemIds.MOON_TIN_ORE.block())
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK.block())
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK_SLAB.block())
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK_STAIRS.block())
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK_WALL.block());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(GalacticraftBlockItemIds.ASTEROID_ALUMINUM_ORE.block())
                .add(GalacticraftBlockItemIds.ALUMINUM_ORE.block())
                .add(GalacticraftBlockItemIds.DEEPSLATE_ALUMINUM_ORE.block())
                .add(GalacticraftBlockItemIds.SILICON_ORE.block())
                .add(GalacticraftBlockItemIds.DEEPSLATE_SILICON_ORE.block())
                .add(GalacticraftBlockItemIds.MOON_SAPPHIRE_ORE.block())
                .add(GalacticraftBlockItemIds.FALLEN_METEOR.block());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(GalacticraftTags.Blocks.MACHINE)
                .add(GalacticraftBlockItemIds.MOON_ROCK.block())
                .add(GalacticraftBlockItemIds.MOON_COPPER_ORE.block())
                .add(GalacticraftBlockItemIds.MOON_CHEESE_ORE.block())
                .add(GalacticraftBlockItemIds.MOON_TIN_ORE.block())
                .add(GalacticraftBlockItemIds.MOON_SAPPHIRE_ORE.block())
                .add(GalacticraftBlockItemIds.MOON_DUNGEON_BRICKS.block())
                .add(GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_SLAB.block())
                .add(GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_STAIRS.block())
                .add(GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_WALL.block())
                .add(GalacticraftBlockItemIds.ALUMINUM_ORE.block())
                .add(GalacticraftBlockItemIds.DEEPSLATE_ALUMINUM_ORE.block())
                .add(GalacticraftBlockItemIds.ASTEROID_ALUMINUM_ORE.block())
                .add(GalacticraftBlockItemIds.TIN_ORE.block())
                .add(GalacticraftBlockItemIds.DEEPSLATE_TIN_ORE.block())
                .add(GalacticraftBlockItemIds.SILICON_ORE.block())
                .add(GalacticraftBlockItemIds.DEEPSLATE_SILICON_ORE.block())
                .add(GalacticraftBlockItemIds.OIL_CAULDRON.block())
                .add(GalacticraftBlockItemIds.FUEL_CAULDRON.block())
                .add(GalacticraftBlockItemIds.FALLEN_METEOR.block())
                .add(GalacticraftBlockItemIds.TIN_DECORATION_BLOCK.block())
                .add(GalacticraftBlockItemIds.TIN_DECORATION_CUT_BLOCK.block())
                .add(GalacticraftBlockItemIds.TIN_DECORATION_SLAB.block())
                .add(GalacticraftBlockItemIds.TIN_DECORATION_STAIRS.block())
                .add(GalacticraftBlockItemIds.TIN_DECORATION_WALL.block());
        tag(GalacticraftTags.BlockItems.ORES_ALUMINUM)
                .add(GalacticraftBlockItemIds.ALUMINUM_ORE)
                .add(GalacticraftBlockItemIds.DEEPSLATE_ALUMINUM_ORE)
                .add(GalacticraftBlockItemIds.ASTEROID_ALUMINUM_ORE);
        tag(GalacticraftTags.BlockItems.ORES_CHEESE)
                .add(GalacticraftBlockItemIds.MOON_CHEESE_ORE);
        tag(GalacticraftTags.BlockItems.ORES_SAPPHIRE)
                .add(GalacticraftBlockItemIds.MOON_SAPPHIRE_ORE);
        tag(GalacticraftTags.BlockItems.ORES_SILICON)
                .add(GalacticraftBlockItemIds.SILICON_ORE)
                .add(GalacticraftBlockItemIds.DEEPSLATE_SILICON_ORE);
        tag(GalacticraftTags.BlockItems.ORES_TIN)
                .add(GalacticraftBlockItemIds.TIN_ORE)
                .add(GalacticraftBlockItemIds.DEEPSLATE_TIN_ORE)
                .add(GalacticraftBlockItemIds.MOON_TIN_ORE);
        tag(GalacticraftTags.BlockItems.STORAGE_BLOCKS_ALUMINUM)
                .add(GalacticraftBlockItemIds.ALUMINUM_BLOCK);
        tag(GalacticraftTags.BlockItems.STORAGE_BLOCKS_RAW_ALUMINUM)
                .add(GalacticraftBlockItemIds.RAW_ALUMINUM_BLOCK);
        tag(GalacticraftTags.BlockItems.STORAGE_BLOCKS_TIN)
                .add(GalacticraftBlockItemIds.TIN_BLOCK);
        tag(GalacticraftTags.BlockItems.STORAGE_BLOCKS_RAW_TIN)
                .add(GalacticraftBlockItemIds.RAW_TIN_BLOCK);
        tag(GalacticraftTags.BlockItems.STORAGE_BLOCKS_RAW_SILICON)
                .add(GalacticraftBlockItemIds.RAW_SILICON_BLOCK);
        tag(Tags.Blocks.ORES)
                .addTag(GalacticraftTags.Blocks.ORES_ALUMINUM);
        tag(Tags.Blocks.ORES)
                .addTag(GalacticraftTags.Blocks.ORES_CHEESE);
        tag(Tags.Blocks.ORES)
                .addTag(GalacticraftTags.Blocks.ORES_SAPPHIRE);
        tag(Tags.Blocks.ORES)
                .addTag(GalacticraftTags.Blocks.ORES_SILICON);
        tag(Tags.Blocks.ORES)
                .addTag(GalacticraftTags.Blocks.ORES_TIN);
        tag(Tags.Blocks.ORES_COPPER)
                .add(GalacticraftBlockItemIds.MOON_COPPER_ORE.block());

//        tag(GalacticraftTags.Blocks.SEALABLE)
//                .addTag(Tags.Blocks.GLASS_PANES);
        tag(GalacticraftTags.Blocks.SENSOR_GLASSES_DETECTABLE)
                .addTag(Tags.Blocks.ORES)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_IRON);
        tag(GalacticraftTags.Blocks.SEALABLE_FROM_BOTTOM)
                .add(BlockItemIds.DIRT_PATH.block())
                .add(BlockItemIds.FARMLAND.block())
                .add(BlockItemIds.ENCHANTING_TABLE.block())
                .add(BlockItemIds.STONECUTTER.block());
        tag(BlockItemTags.SLABS)
                .add(GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_SLAB)
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK_SLAB)
                .add(GalacticraftBlockItemIds.TIN_DECORATION_SLAB);
        tag(BlockItemTags.STAIRS)
                .add(GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_STAIRS)
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK_STAIRS)
                .add(GalacticraftBlockItemIds.TIN_DECORATION_STAIRS);
        tag(Tags.Blocks.STORAGE_BLOCKS)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_ALUMINUM)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_ALUMINUM)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_TIN)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_TIN)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_SILICON);
        tag(BlockItemTags.WALLS)
                .add(GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_WALL)
                .add(GalacticraftBlockItemIds.ASTEROID_ROCK_WALL)
                .add(GalacticraftBlockItemIds.TIN_DECORATION_WALL);
        tag(GalacticraftTags.BlockItems.LIT_TORCHES_STANDING)
                .add(BlockItemIds.TORCH)
                .add(BlockItemIds.SOUL_TORCH)
                .add(BlockItemIds.COPPER_TORCH);
        tag(GalacticraftTags.Blocks.LIT_TORCHES_WALL)
                .add(BlockIds.WALL_TORCH)
                .add(BlockIds.SOUL_WALL_TORCH)
                .add(BlockIds.COPPER_WALL_TORCH);
        tag(GalacticraftTags.BlockItems.LIT_TORCHES)
                .addTag(GalacticraftTags.Blocks.LIT_TORCHES_STANDING)
                .addTag(GalacticraftTags.Blocks.LIT_TORCHES_WALL);
    }
}
