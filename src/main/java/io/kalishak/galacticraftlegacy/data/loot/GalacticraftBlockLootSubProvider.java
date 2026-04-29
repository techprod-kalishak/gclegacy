/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.loot;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class GalacticraftBlockLootSubProvider extends BlockLootSubProvider {
    private static final Set<Item> EXPLOSION_RESISTANT = Set.of();

    protected GalacticraftBlockLootSubProvider(HolderLookup.Provider registries) {
        super(EXPLOSION_RESISTANT, FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected void generate() {
        dropSelf(GalacticraftBlocks.GRATING.get());
        add(GalacticraftBlocks.CHEESE.get(), noDrop());
        add(GalacticraftBlocks.COAL_GENERATOR.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.CIRCUIT_FABRICATOR.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.COMPRESSOR.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.ELECTRIC_COMPRESSOR.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.ELECTRIC_FURNACE.get(), this::createNameableBlockEntityTable);
        dropSelf(GalacticraftBlocks.OXYGEN_DETECTOR.get());
        dropSelf(GalacticraftBlocks.ALUMINUM_WIRE.get());
        dropSelf(GalacticraftBlocks.HEAVY_ALUMINUM_WIRE.get());
        dropSelf(GalacticraftBlocks.WHITE_PIPE.get());
        dropSelf(GalacticraftBlocks.ORANGE_PIPE.get());
        dropSelf(GalacticraftBlocks.MAGENTA_PIPE.get());
        dropSelf(GalacticraftBlocks.LIGHT_BLUE_PIPE.get());
        dropSelf(GalacticraftBlocks.YELLOW_PIPE.get());
        dropSelf(GalacticraftBlocks.LIME_PIPE.get());
        dropSelf(GalacticraftBlocks.PINK_PIPE.get());
        dropSelf(GalacticraftBlocks.GRAY_PIPE.get());
        dropSelf(GalacticraftBlocks.LIGHT_GRAY_PIPE.get());
        dropSelf(GalacticraftBlocks.CYAN_PIPE.get());
        dropSelf(GalacticraftBlocks.PURPLE_PIPE.get());
        dropSelf(GalacticraftBlocks.BLUE_PIPE.get());
        dropSelf(GalacticraftBlocks.BROWN_PIPE.get());
        dropSelf(GalacticraftBlocks.GREEN_PIPE.get());
        dropSelf(GalacticraftBlocks.RED_PIPE.get());
        dropSelf(GalacticraftBlocks.BLACK_PIPE.get());
        add(GalacticraftBlocks.ALUMINUM_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_ALUMINUM.get()));
        add(GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_ALUMINUM.get()));
        add(GalacticraftBlocks.TIN_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_TIN.get()));
        add(GalacticraftBlocks.DEEPSLATE_TIN_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_TIN.get()));
        add(GalacticraftBlocks.SILICON_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_SILICON.get()));
        add(GalacticraftBlocks.DEEPSLATE_SILICON_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_SILICON.get()));
        dropSelf(GalacticraftBlocks.RAW_ALUMINUM_BLOCK.get());
        dropSelf(GalacticraftBlocks.ALUMINUM_BLOCK.get());
        dropSelf(GalacticraftBlocks.RAW_TIN_BLOCK.get());
        dropSelf(GalacticraftBlocks.TIN_BLOCK.get());
        dropSelf(GalacticraftBlocks.RAW_SILICON_BLOCK.get());
        dropSelf(GalacticraftBlocks.MOON_DIRT.get());
        dropSelf(GalacticraftBlocks.MOON_TURF.get());
        dropSelf(GalacticraftBlocks.MOON_ROCK.get());
        add(GalacticraftBlocks.MOON_COPPER_ORE.get(), this::createCopperOreDrops);
        add(GalacticraftBlocks.MOON_TIN_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_TIN.get()));
        add(GalacticraftBlocks.MOON_SAPPHIRE_ORE.get(), block -> createOreDrop(block, GalacticraftItems.SAPPHIRE.get()));
        add(GalacticraftBlocks.MOON_CHEESE_ORE.get(), block -> createOreDrop(block, GalacticraftItems.CHEESE_CHUNK.get()));
        dropSelf(GalacticraftBlocks.MOON_BRICKS.get());
        dropSelf(GalacticraftBlocks.MOON_BRICK_STAIRS.get());
        dropSelf(GalacticraftBlocks.MOON_BRICK_SLAB.get());
        dropSelf(GalacticraftBlocks.MOON_BRICK_WALL.get());
        dropOther(GalacticraftBlocks.OIL_CAULDRON.get(), Items.CAULDRON);
        dropOther(GalacticraftBlocks.FUEL_CAULDRON.get(), Items.CAULDRON);
        dropSelf(GalacticraftBlocks.UNLIT_TORCH.get());
        dropSelf(GalacticraftBlocks.UNLIT_COPPER_TORCH.get());
        dropSelf(GalacticraftBlocks.UNLIT_LANTERN.get());
        GalacticraftBlocks.UNLIT_COPPER_LANTERN.forEach(this::dropSelf);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GalacticraftBlocks.getEntries().toList();
    }
}
