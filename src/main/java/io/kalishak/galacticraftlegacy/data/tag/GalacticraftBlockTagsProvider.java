/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftBlockTagsProvider extends BlockTagsProvider {
    public GalacticraftBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Blocks.BASE_STONE_MOON)
                .add(GalacticraftBlocks.MOON_DIRT.get())
                .add(GalacticraftBlocks.MOON_ROCK.get());
        tag(GalacticraftTags.Blocks.MOON_CARVER_REPLACEABLES)
                .addTag(GalacticraftTags.Blocks.BASE_STONE_MOON)
                .add(GalacticraftBlocks.MOON_TURF.get());
        tag(GalacticraftTags.Blocks.BREATHABLE_AIR)
                .add(Blocks.AIR)
                .add(Blocks.CAVE_AIR)
                .add(GalacticraftBlocks.OXYGEN_AIR.get());
        tag(GalacticraftTags.Blocks.CRUDE_OIL_POOL_REPLACEABLE)
                .addTag(BlockTags.BASE_STONE_OVERWORLD)
                .addTag(BlockTags.SAND)
                .add(Blocks.SANDSTONE);
        TagAppender<Block, Block> lanternTag = tag(BlockTags.LANTERNS)
                .add(GalacticraftBlocks.UNLIT_LANTERN.get());
        GalacticraftBlocks.UNLIT_COPPER_LANTERN.forEach(lanternTag::add);
        tag(GalacticraftTags.Blocks.MACHINE)
                .addTag(GalacticraftTags.Blocks.MACHINE_BASIC)
                .addTag(GalacticraftTags.Blocks.MACHINE_ADVANCED);
        tag(GalacticraftTags.Blocks.MACHINE_BASIC)
                .add(GalacticraftBlocks.COAL_GENERATOR.get())
                .add(GalacticraftBlocks.OXYGEN_DETECTOR.get())
                .add(GalacticraftBlocks.CIRCUIT_FABRICATOR.get())
                .add(GalacticraftBlocks.COMPRESSOR.get())
                .add(GalacticraftBlocks.OXYGEN_COLLECTOR.get());
        tag(GalacticraftTags.Blocks.MACHINE_ADVANCED)
                .add(GalacticraftBlocks.ELECTRIC_FURNACE.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(GalacticraftBlocks.COAL_GENERATOR.get())
                .add(GalacticraftBlocks.CIRCUIT_FABRICATOR.get())
                .add(GalacticraftBlocks.COMPRESSOR.get())
                .add(GalacticraftBlocks.ELECTRIC_COMPRESSOR.get())
                .add(GalacticraftBlocks.OXYGEN_DETECTOR.get())
                .add(GalacticraftBlocks.ELECTRIC_FURNACE.get())
                .add(GalacticraftBlocks.MOON_COPPER_ORE.get())
                .add(GalacticraftBlocks.MOON_CHEESE_ORE.get())
                .add(GalacticraftBlocks.TIN_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_TIN_ORE.get())
                .add(GalacticraftBlocks.MOON_TIN_ORE.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(GalacticraftBlocks.ALUMINUM_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE.get())
                .add(GalacticraftBlocks.SILICON_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_SILICON_ORE.get())
                .add(GalacticraftBlocks.MOON_SAPPHIRE_ORE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(GalacticraftTags.Blocks.MACHINE)
                .add(GalacticraftBlocks.MOON_ROCK.get())
                .add(GalacticraftBlocks.MOON_COPPER_ORE.get())
                .add(GalacticraftBlocks.MOON_CHEESE_ORE.get())
                .add(GalacticraftBlocks.MOON_TIN_ORE.get())
                .add(GalacticraftBlocks.MOON_SAPPHIRE_ORE.get())
                .add(GalacticraftBlocks.MOON_BRICKS.get())
                .add(GalacticraftBlocks.MOON_BRICK_SLAB.get())
                .add(GalacticraftBlocks.MOON_BRICK_STAIRS.get())
                .add(GalacticraftBlocks.MOON_BRICK_WALL.get())
                .add(GalacticraftBlocks.ALUMINUM_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE.get())
                .add(GalacticraftBlocks.TIN_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_TIN_ORE.get())
                .add(GalacticraftBlocks.SILICON_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_SILICON_ORE.get())
                .add(GalacticraftBlocks.OIL_CAULDRON.get())
                .add(GalacticraftBlocks.FUEL_CAULDRON.get());
        tag(GalacticraftTags.Blocks.ORES_ALUMINUM)
                .add(GalacticraftBlocks.ALUMINUM_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE.get());
        tag(GalacticraftTags.Blocks.ORES_CHEESE)
                .add(GalacticraftBlocks.MOON_CHEESE_ORE.get());
        tag(GalacticraftTags.Blocks.ORES_SAPPHIRE)
                .add(GalacticraftBlocks.MOON_SAPPHIRE_ORE.get());
        tag(GalacticraftTags.Blocks.ORES_SILICON)
                .add(GalacticraftBlocks.SILICON_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_SILICON_ORE.get());
        tag(GalacticraftTags.Blocks.ORES_TIN)
                .add(GalacticraftBlocks.TIN_ORE.get())
                .add(GalacticraftBlocks.DEEPSLATE_TIN_ORE.get())
                .add(GalacticraftBlocks.MOON_TIN_ORE.get());
        tag(GalacticraftTags.Blocks.STORAGE_BLOCKS_ALUMINUM)
                .add(GalacticraftBlocks.ALUMINUM_BLOCK.get());
        tag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_ALUMINUM)
                .add(GalacticraftBlocks.RAW_ALUMINUM_BLOCK.get());
        tag(GalacticraftTags.Blocks.STORAGE_BLOCKS_TIN)
                .add(GalacticraftBlocks.TIN_BLOCK.get());
        tag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_TIN)
                .add(GalacticraftBlocks.RAW_TIN_BLOCK.get());
        tag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_SILICON)
                .add(GalacticraftBlocks.RAW_SILICON_BLOCK.get());
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
                .add(GalacticraftBlocks.MOON_COPPER_ORE.get());

        tag(GalacticraftTags.Blocks.SEALABLE)
                .addTag(Tags.Blocks.GLASS_PANES);
        tag(GalacticraftTags.Blocks.SENSOR_GLASSES_DETECTABLE)
                .addTag(Tags.Blocks.ORES)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_IRON);
        tag(GalacticraftTags.Blocks.SEALABLE_FROM_BOTTOM)
                .add(Blocks.DIRT_PATH)
                .add(Blocks.FARMLAND)
                .add(Blocks.ENCHANTING_TABLE)
                .add(Blocks.STONECUTTER);
        tag(BlockTags.SLABS)
                .add(GalacticraftBlocks.MOON_BRICK_SLAB.get());
        tag(BlockTags.STAIRS)
                .add(GalacticraftBlocks.MOON_BRICK_STAIRS.get());
        tag(Tags.Blocks.STORAGE_BLOCKS)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_ALUMINUM)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_ALUMINUM)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_TIN)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_TIN)
                .addTag(GalacticraftTags.Blocks.STORAGE_BLOCKS_RAW_SILICON);
        tag(BlockTags.WALLS)
                .add(GalacticraftBlocks.MOON_BRICK_WALL.get());

        tag(GalacticraftTags.Blocks.LIT_TORCHES_STANDING)
                .add(Blocks.TORCH)
                .add(Blocks.SOUL_TORCH)
                .add(Blocks.COPPER_TORCH);
        tag(GalacticraftTags.Blocks.LIT_TORCHES_WALL)
                .add(Blocks.WALL_TORCH)
                .add(Blocks.SOUL_WALL_TORCH)
                .add(Blocks.COPPER_WALL_TORCH);
        tag(GalacticraftTags.Blocks.LIT_TORCHES)
                .addTag(GalacticraftTags.Blocks.LIT_TORCHES_STANDING)
                .addTag(GalacticraftTags.Blocks.LIT_TORCHES_WALL);
    }
}
