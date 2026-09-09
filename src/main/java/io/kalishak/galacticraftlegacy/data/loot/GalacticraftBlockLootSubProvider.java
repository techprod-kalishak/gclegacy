/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.loot;

import io.kalishak.galacticraftlegacy.client.data.GalacticraftBlockFamilies;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class GalacticraftBlockLootSubProvider extends BlockLootSubProvider {
    protected GalacticraftBlockLootSubProvider(HolderLookup.Provider registries) {
        super(getExplosionResistant(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    private static Set<Item> getExplosionResistant() {
        return Set.of(
                GalacticraftItems.RAW_METEORIC_IRON,
                GalacticraftItems.RAW_METEORIC_IRON_BLOCK
        ).stream().map(DeferredItem::asItem).collect(Collectors.toSet());
    }

    @Override
    protected void generate() {
        List<BlockFamily> families = List.of(
                GalacticraftBlockFamilies.MOON_ROCK,
                GalacticraftBlockFamilies.ASTEROID_ROCKS,
                GalacticraftBlockFamilies.MOON_BRICKS,
                GalacticraftBlockFamilies.MARS_COBBLESTONE,
                GalacticraftBlockFamilies.TIN_DECORATION,
                GalacticraftBlockFamilies.TIN_WALL_DECORATION,
                GalacticraftBlockFamilies.MARS_BRICKS,
                GalacticraftBlockFamilies.VENUS_SOFT_ROCK,
                GalacticraftBlockFamilies.VENUS_BRICKS,
                GalacticraftBlockFamilies.DEEP_VENUS_BRICKS
        );
        families.forEach(this::generateForBlockFamily);
        generateForBlockFamily(GalacticraftBlockFamilies.MARS_STONE, _ -> createSilkTouchOnlyTable(GalacticraftBlocks.MARS_COBBLESTONE));

        dropSelf(GalacticraftBlocks.GRATING.get());
        add(GalacticraftBlocks.CHEESE.get(), noDrop());
        add(GalacticraftBlocks.COAL_GENERATOR.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.CIRCUIT_FABRICATOR.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.COMPRESSOR.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.ELECTRIC_COMPRESSOR.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.ELECTRIC_FURNACE.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.ELECTRIC_ARC_FURNACE.get(), this::createNameableBlockEntityTable);
        add(GalacticraftBlocks.OXYGEN_COLLECTOR.get(), this::createNameableBlockEntityTable);
        dropSelf(GalacticraftBlocks.OXYGEN_DETECTOR.get());
        dropSelf(GalacticraftBlocks.ALUMINUM_WIRE.get());
        dropSelf(GalacticraftBlocks.HEAVY_ALUMINUM_WIRE.get());
        GalacticraftBlocks.FLUID_PIPE.forEach(fluidPipe -> dropSelf(fluidPipe.get()));
        add(GalacticraftBlocks.ALUMINUM_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_ALUMINUM.get()));
        add(GalacticraftBlocks.DEEPSLATE_ALUMINUM_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_ALUMINUM.get()));
        add(GalacticraftBlocks.TIN_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_TIN.get()));
        add(GalacticraftBlocks.DEEPSLATE_TIN_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_TIN.get()));
        add(GalacticraftBlocks.SILICON_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_SILICON.get()));
        add(GalacticraftBlocks.DEEPSLATE_SILICON_ORE.get(), block -> createLargeOreDrop(block, GalacticraftItems.RAW_SILICON, 2, 4));
        dropSelf(GalacticraftBlocks.RAW_ALUMINUM_BLOCK.get());
        dropSelf(GalacticraftBlocks.ALUMINUM_BLOCK.get());
        dropSelf(GalacticraftBlocks.RAW_TIN_BLOCK.get());
        dropSelf(GalacticraftBlocks.TIN_BLOCK.get());
        dropSelf(GalacticraftBlocks.RAW_SILICON_BLOCK.get());
        dropSelf(GalacticraftBlocks.MARS_FINE_REGOLITH.get());
        dropSelf(GalacticraftBlocks.MARS_REGOLITH.get());
        dropSelf(GalacticraftBlocks.MOON_DIRT.get());
        dropSelf(GalacticraftBlocks.MOON_TURF.get());
        dropSelf(GalacticraftBlocks.MOON_ROCK.get());
        add(GalacticraftBlocks.MOON_COPPER_ORE.get(), this::createCopperOreDrops);
        add(GalacticraftBlocks.MOON_TIN_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_TIN.get()));
        add(GalacticraftBlocks.MOON_SAPPHIRE_ORE.get(), block -> createOreDrop(block, GalacticraftItems.SAPPHIRE.get()));
        add(GalacticraftBlocks.MOON_CHEESE_ORE.get(), block -> createOreDrop(block, GalacticraftItems.CHEESE_CHUNK.get()));
        dropSelf(GalacticraftBlocks.RAW_METEORIC_IRON_BLOCK.get());
        dropSelf(GalacticraftBlocks.METEORIC_IRON_BLOCK.get());
        dropOther(GalacticraftBlocks.OIL_CAULDRON.get(), Items.CAULDRON);
        dropOther(GalacticraftBlocks.FUEL_CAULDRON.get(), Items.CAULDRON);
        dropSelf(GalacticraftBlocks.SEALED_REDSTONE_WIRE.get());
        dropSelf(GalacticraftBlocks.SEALED_REPEATER.get());
        dropSelf(GalacticraftBlocks.FLUID_TANK.get());
        dropSelf(GalacticraftBlocks.SEALED_COMPARATOR.get());
        dropSelf(GalacticraftBlocks.UNLIT_TORCH.get());
        dropSelf(GalacticraftBlocks.UNLIT_COPPER_TORCH.get());
        dropSelf(GalacticraftBlocks.UNLIT_LANTERN.get());
        GalacticraftBlocks.UNLIT_COPPER_LANTERN.map(DeferredHolder::get).forEach(this::dropSelf);
        add(GalacticraftBlocks.MAGNETIC_CRAFTING_TABLE.get(), block -> createComponentsBlockEntityTable(block, builder -> builder.include(GalacticraftDataComponents.CRAFTING_MEMORY.get())));
        add(GalacticraftBlocks.ASTEROID_ALUMINUM_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_ALUMINUM.get()));
        add(GalacticraftBlocks.FALLEN_METEOR.get(), block -> createOreDrop(block, GalacticraftItems.RAW_METEORIC_IRON.get()));
        dropSelf(GalacticraftBlocks.COMPACT_NASA_WORKBENCH.get());
        dropSelf(GalacticraftBlocks.NASA_WORKBENCH.get());
        dropSelf(GalacticraftBlocks.LANDING_PAD.get());
        dropSelf(GalacticraftBlocks.FUELING_PAD.get());
        GalacticraftBlocks.COLORED_TINTED_GLASS_PANE.map(DeferredHolder::get).forEach(this::dropWhenSilkTouch);
        dropWhenSilkTouch(GalacticraftBlocks.TINTED_GLASS_PANE.get());
        add(GalacticraftBlocks.MARS_COPPER_ORE.get(), this::createCopperOreDrops);
        add(GalacticraftBlocks.MARS_TIN_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_TIN.get()));
        add(GalacticraftBlocks.MARS_DESH_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_DESH.get()));
        add(GalacticraftBlocks.MARS_IRON_ORE.get(), block -> createOreDrop(block, Items.RAW_IRON));
        dropSelf(GalacticraftBlocks.RAW_DESH_BLOCK.get());
        dropSelf(GalacticraftBlocks.DESH_BLOCK.get());
        dropSelf(GalacticraftBlocks.VENUS_HARD_ROCK.get());
        dropWhenSilkTouch(GalacticraftBlocks.VENUS_VOLCANIC_ROCK.get());
        dropSelf(GalacticraftBlocks.SCORCHED_VENUS_ROCK.get());
        add(GalacticraftBlocks.VENUS_ALUMINUM_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_ALUMINUM.get()));
        add(GalacticraftBlocks.VENUS_COPPER_ORE.get(), this::createCopperOreDrops);
        add(GalacticraftBlocks.VENUS_QUARTZ_ORE.get(), block -> createOreDrop(block, Items.QUARTZ));
        add(GalacticraftBlocks.VENUS_SILICON_ORE.get(), block -> createLargeOreDrop(block, GalacticraftItems.RAW_SILICON, 2, 4));
        add(GalacticraftBlocks.VENUS_SOLAR_ORE.get(), block -> createLargeOreDrop(block, GalacticraftItems.SOLAR_DUST.get(), 4, 7));
        add(GalacticraftBlocks.VENUS_TIN_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_TIN.get()));
        add(GalacticraftBlocks.VENUS_LEAD_ORE.get(), block -> createOreDrop(block, GalacticraftItems.RAW_LEAD.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GalacticraftBlocks.getEntries().toList();
    }

    protected LootTable.Builder createComponentsBlockEntityTable(Block drop, UnaryOperator<CopyComponentsFunction.Builder> builder) {
        return LootTable.lootTable()
                .withPool(
                        applyExplosionCondition(
                                drop,
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(drop)
                                                        .apply(builder.apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)))
                                        )
                        )
                );
    }

    protected LootTable.Builder createLargeOreDrop(Block block, ItemLike drop, int min, int max) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(drop)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between((float) min, (float) max)))
                .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    protected void generateForBlockFamily(BlockFamily family, Function<Block, LootTable.Builder> baseBlockBuilder) {
        add(family.getBaseBlock(), baseBlockBuilder);
        family.getVariants().forEach((variant, block) -> {
            if (variant != BlockFamily.Variant.SLAB) {
                dropSelf(block);
            } else {
                add(block, this::createSlabItemTable);
            }
        });
    }

    protected void generateForBlockFamily(BlockFamily family) {
        generateForBlockFamily(family, this::createSingleItemTable);
    }
}
