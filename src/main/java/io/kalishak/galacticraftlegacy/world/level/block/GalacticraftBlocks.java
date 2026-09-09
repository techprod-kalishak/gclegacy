/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.GalacticraftBlockIds;
import io.kalishak.galacticraftlegacy.references.GalacticraftBlockItemIds;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredBlockRegister;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.block.cauldron.FlammableCauldronBlock;
import io.kalishak.galacticraftlegacy.world.level.block.cauldron.GalacticraftCauldronInteraction;
import io.kalishak.galacticraftlegacy.world.level.block.machine.*;
import io.kalishak.galacticraftlegacy.world.level.block.machine.oxygen.OxygenCollectorBlock;
import io.kalishak.galacticraftlegacy.world.level.block.state.GalacticraftBlockSetType;
import io.kalishak.galacticraftlegacy.world.level.block.wire.WireBlock;
import io.kalishak.galacticraftlegacy.world.level.block.wire.ColoredPipeBlock;
import io.kalishak.galacticraftlegacy.world.level.block.wire.HeavyWireBlock;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class GalacticraftBlocks {
    private static final DeferredBlockRegister REGISTRY = DeferredBlockRegister.createBlockRegister(Galacticraft.MODID);

    //Overworld ores
    public static final DeferredBlock<DropExperienceBlock> ALUMINUM_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ALUMINUM_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_ALUMINUM_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.DEEPSLATE_ALUMINUM_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> RAW_ALUMINUM_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.RAW_ALUMINUM_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> ALUMINUM_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.ALUMINUM_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> TIN_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.TIN_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_TIN_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.DEEPSLATE_TIN_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> RAW_TIN_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.RAW_TIN_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> TIN_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.TIN_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> SILICON_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.SILICON_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(0, 2), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_SILICON_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.DEEPSLATE_SILICON_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(0, 2), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.0F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> RAW_SILICON_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.RAW_SILICON_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<AirBlock> EMPTY_AIR = REGISTRY.registerBlock(
            GalacticraftBlockIds.EMPTY_AIR,
            AirBlock::new,
            () -> BlockBehaviour.Properties.of().replaceable().noCollision().noLootTable().air()
    );
    public static final DeferredBlock<AirBlock> OXYGEN_AIR = REGISTRY.registerBlock(
            GalacticraftBlockIds.OXYGEN_AIR,
            AirBlock::new,
            () -> BlockBehaviour.Properties.of().replaceable().noCollision().noLootTable().air()
    );

    public static final DeferredBlock<ParachestBlock> PARACHEST = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.PARACHEST,
            properties -> new ParachestBlock(ParachestBlock.Type.MINIMAL, properties),
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );
    public static final DeferredBlock<ParachestBlock> PARACHEST_18 = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.PARACHEST_SINGLE,
            properties -> new ParachestBlock(ParachestBlock.Type.SINGLE, properties),
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );
    public static final DeferredBlock<ParachestBlock> PARACHEST_36 = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.PARACHEST_DOUBLE,
            properties -> new ParachestBlock(ParachestBlock.Type.DOUBLE, properties),
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );
    public static final DeferredBlock<ParachestBlock> PARACHEST_54 = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.PARACHEST_TRIPLE,
            properties -> new ParachestBlock(ParachestBlock.Type.TRIPLE, properties),
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );
    public static final DeferredBlock<DungeonChestBlock> MOON_DUNGEON_CHEST = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_DUNGEON_CHEST,
            properties -> new DungeonChestBlock(FeatureTier.TIER_1, properties),
            () -> BlockBehaviour.Properties.of()
                    .noTerrainParticles()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
                    .isValidSpawn(Blocks::never)
    );
    public static final DeferredBlock<DungeonChestBlock> MARS_DUNGEON_CHEST = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_DUNGEON_CHEST,
            properties -> new DungeonChestBlock(FeatureTier.TIER_2, properties),
            () -> BlockBehaviour.Properties.of()
                    .noTerrainParticles()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
                    .isValidSpawn(Blocks::never)
    );
    public static final DeferredBlock<DungeonChestBlock> VENUS_DUNGEON_CHEST = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_DUNGEON_CHEST,
            properties -> new DungeonChestBlock(FeatureTier.TIER_3, properties),
            () -> BlockBehaviour.Properties.of()
                    .noTerrainParticles()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
                    .isValidSpawn(Blocks::never)
    );

    /** Moon */
    public static final DeferredBlock<TerraformableBlock> MOON_DIRT = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_DIRT,
            TerraformableBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(0.5F, 1.0F)
    );
    public static final DeferredBlock<TerraformableRotatedBlock> MOON_TURF = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_TURF,
            TerraformableRotatedBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(0.5F, 1.0F)
    );

    public static final DeferredBlock<Block> MOON_ROCK = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_ROCK,
            Block::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .strength(1.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> MOON_ROCK_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_ROCK_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .strength(1.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> MOON_ROCK_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_ROCK_STAIRS,
            properties -> new StairBlock(MOON_ROCK.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .strength(1.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<WallBlock> MOON_ROCK_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_ROCK_WALL,
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .strength(1.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> MOON_COPPER_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_COPPER_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> MOON_TIN_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_TIN_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> MOON_SAPPHIRE_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_SAPPHIRE_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> MOON_CHEESE_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_CHEESE_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<Block> MOON_BRICKS = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.MOON_DUNGEON_BRICKS,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(2.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> MOON_BRICK_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_STAIRS,
            properties -> new StairBlock(MOON_BRICKS.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> MOON_BRICK_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> MOON_BRICK_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MOON_DUNGEON_BRICK_WALL,
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<FallenMeteorBlock> FALLEN_METEOR = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.FALLEN_METEOR,
            FallenMeteorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(5.5F, 40.0F)
                    .instrument(NoteBlockInstrument.BASS)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> RAW_METEORIC_IRON_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.RAW_METEORIC_IRON_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(5.5F, 48.0F)
                    .instrument(NoteBlockInstrument.BASS)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> METEORIC_IRON_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.METEORIC_IRON_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(6.5F, 48.0F)
                    .instrument(NoteBlockInstrument.BASS)
                    .requiresCorrectToolForDrops()
    );

    /** Mars */
    public static final DeferredBlock<Block> MARS_STONE = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.MARS_STONE,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.5F, 6.0F)
    );
    public static final DeferredBlock<SlabBlock> MARS_STONE_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_STONE_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.5F, 6.0F)
    );
    public static final DeferredBlock<StairBlock> MARS_STONE_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_STONE_STAIRS,
            properties -> new StairBlock(MARS_STONE.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.5F, 6.0F)
    );
    public static final DeferredBlock<PressurePlateBlock> MARS_STONE_PRESSURE_PLATE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_STONE_PRESSURE_PLATE,
            properties -> new PressurePlateBlock(GalacticraftBlockSetType.MARS_STONE, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .noCollision()
                    .strength(0.5F)
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredBlock<ButtonBlock> MARS_STONE_BUTTON = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_STONE_BUTTON,
            properties -> new ButtonBlock(GalacticraftBlockSetType.MARS_STONE, 30, properties),
            () -> BlockBehaviour.Properties.of()
                    .noCollision()
                    .strength(0.5F)
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredBlock<Block> MARS_COBBLESTONE = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.MARS_COBBLESTONE,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.5F, 6.0F)
    );
    public static final DeferredBlock<SlabBlock> MARS_COBBLESTONE_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_COBBLESTONE_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.5F, 6.0F)
    );
    public static final DeferredBlock<StairBlock> MARS_COBBLESTONE_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_COBBLESTONE_STAIRS,
            properties -> new StairBlock(MARS_COBBLESTONE.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.5F, 6.0F)
    );
    public static final DeferredBlock<WallBlock> MARS_COBBLESTONE_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_COBBLESTONE_WALL,
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .strength(2.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<TerraformableBlock> MARS_REGOLITH = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_REGOLITH,
            TerraformableBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
    );
    public static final DeferredBlock<TerraformableBlock> MARS_FINE_REGOLITH = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_FINE_REGOLITH,
            TerraformableBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
    );

    public static final DeferredBlock<Block> MARS_BRICKS = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.MARS_DUNGEON_BRICKS,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 40.0F)
    );
    public static final DeferredBlock<SlabBlock> MARS_BRICK_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_DUNGEON_BRICK_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 40.0F)
    );
    public static final DeferredBlock<StairBlock> MARS_BRICK_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_DUNGEON_BRICK_STAIRS,
            properties -> new StairBlock(MARS_BRICKS.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 40.0F)
    );
    public static final DeferredBlock<WallBlock> MARS_BRICK_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_DUNGEON_BRICK_WALL,
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> MARS_COPPER_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_COPPER_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 6.0F)
    );
    public static final DeferredBlock<DropExperienceBlock> MARS_TIN_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_TIN_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 6.0F)
    );
    public static final DeferredBlock<DropExperienceBlock> MARS_DESH_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_DESH_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 6.0F)
    );
    public static final DeferredBlock<DropExperienceBlock> MARS_IRON_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MARS_IRON_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 6.0F)
    );
    public static final DeferredBlock<Block> RAW_DESH_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.RAW_DESH_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 60.0F)
    );
    public static final DeferredBlock<Block> DESH_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.DESH_BLOCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 60.0F)
    );

    /** Asteroids */
    public static final DeferredBlock<Block> ASTEROID_ROCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.ASTEROID_ROCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> ASTEROID_ROCK_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ASTEROID_ROCK_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> ASTEROID_ROCK_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ASTEROID_ROCK_STAIRS,
            properties -> new StairBlock(ASTEROID_ROCK.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<WallBlock> ASTEROID_ROCK_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ASTEROID_ROCK_WALL,
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> ASTEROID_ALUMINUM_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ASTEROID_ALUMINUM_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    /** Venus */
    public static final DeferredBlock<TerraformableBlock> VENUS_SOFT_ROCK = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_SOFT_ROCK,
            TerraformableBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .strength(0.9F, 2.5F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> VENUS_SOFT_ROCK_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_SOFT_ROCK_STAIRS,
            properties -> new StairBlock(VENUS_SOFT_ROCK.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .strength(0.9F, 2.5F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> VENUS_SOFT_ROCK_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_SOFT_ROCK_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .strength(0.9F, 2.5F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<WallBlock> VENUS_SOFT_ROCK_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_SOFT_ROCK_WALL,
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .strength(0.9F, 2.5F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<TerraformableBlock> VENUS_HARD_ROCK = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_HARD_ROCK,
            TerraformableBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(1.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<LavaTurnableBlock> VENUS_VOLCANIC_ROCK = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_VOLCANIC_ROCK,
            LavaTurnableBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .strength(2.5F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .lightLevel(_ -> 2)
                    .emissiveRendering(_ -> true)
    );
    public static final DeferredBlock<Block> SCORCHED_VENUS_ROCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.SCORCHED_VENUS_ROCK,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BLACK)
                    .strength(2.5F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> VENUS_DUNGEON_BRICKS = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.VENUS_DUNGEON_BRICKS,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> VENUS_DUNGEON_BRICK_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_DUNGEON_BRICK_STAIRS,
            properties -> new StairBlock(VENUS_DUNGEON_BRICKS.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> VENUS_DUNGEON_BRICK_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_DUNGEON_BRICK_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<WallBlock> VENUS_DUNGEON_BRICK_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_DUNGEON_BRICK_WALL,
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> DEEP_VENUS_DUNGEON_BRICKS = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.DEEP_VENUS_DUNGEON_BRICKS,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<StairBlock> DEEP_VENUS_DUNGEON_BRICK_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.DEEP_VENUS_DUNGEON_BRICK_STAIRS,
            properties -> new StairBlock(DEEP_VENUS_DUNGEON_BRICKS.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> DEEP_VENUS_DUNGEON_BRICK_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.DEEP_VENUS_DUNGEON_BRICK_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<WallBlock> DEEP_VENUS_DUNGEON_BRICK_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.DEEP_VENUS_DUNGEON_BRICK_WALL,
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .strength(4.0F, 40.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> VENUS_ALUMINUM_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_ALUMINUM_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(5.0F, 3.0F)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> VENUS_COPPER_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_COPPER_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(5.0F, 3.0F)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> VENUS_QUARTZ_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_QUARTZ_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(2, 5), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(5.0F, 3.0F)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> VENUS_SILICON_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_SILICON_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(2, 5), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(5.0F, 3.0F)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> VENUS_SOLAR_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_SOLAR_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(5.0F, 3.0F)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> VENUS_TIN_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_TIN_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(5.0F, 3.0F)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> VENUS_LEAD_ORE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.VENUS_LEAD_ORE,
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(5.0F, 3.0F)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
    );

    // Fluids
    public static final DeferredBlock<LiquidBlock> OIL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.OIL,
            properties -> new LiquidBlock(GalacticraftFluids.OIL.get(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .replaceable()
                    .noCollision()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
    );
    public static final DeferredBlock<LiquidBlock> FUEL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.FUEL,
            properties -> new LiquidBlock(GalacticraftFluids.FUEL.get(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .replaceable()
                    .noCollision()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
    );

    //Pipes
    public static final ColorCollection<DeferredBlock<ColoredPipeBlock>> FLUID_PIPE = REGISTRY.registerColoredBlocks(
            GalacticraftBlockItemIds.FLUID_PIPE,
            ColoredPipeBlock::new,
            dyeColor -> BlockBehaviour.Properties.of()
                    .mapColor(dyeColor)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<WireBlock> ALUMINUM_WIRE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ALUMINUM_WIRE,
            properties -> new WireBlock(0.4D, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(0.4F)
                    .sound(SoundType.WOOL)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isValidSpawn(Blocks::never)
    );
    public static final DeferredBlock<HeavyWireBlock> HEAVY_ALUMINUM_WIRE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.HEAVY_ALUMINUM_WIRE,
            properties -> new HeavyWireBlock(0.4D, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(0.4F)
                    .sound(SoundType.WOOL)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isValidSpawn(Blocks::never)
    );

    // Misc
    public static final DeferredBlock<FlammableCauldronBlock> OIL_CAULDRON = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.OIL_CAULDRON,
            properties -> new FlammableCauldronBlock(GalacticraftCauldronInteraction.OIL, GalacticraftFluids.OIL, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F)
                    .noOcclusion()
    );
    public static final DeferredBlock<FlammableCauldronBlock> FUEL_CAULDRON = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.FUEL_CAULDRON,
            properties -> new FlammableCauldronBlock(GalacticraftCauldronInteraction.FUEL, GalacticraftFluids.FUEL, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F)
                    .noOcclusion()
    );
    public static final DeferredBlock<SealedRedstoneWireBlock> SEALED_REDSTONE_WIRE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.SEALED_REDSTONE_WIRE,
            SealedRedstoneWireBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 4.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SealedRepeaterBlock> SEALED_REPEATER = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.SEALED_REPEATER,
            SealedRepeaterBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 4.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SealedComparatorBlock> SEALED_COMPARATOR = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.SEALED_COMPARATOR,
            SealedComparatorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 4.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<FluidTankBlock> FLUID_TANK = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.FLUID_TANK,
            FluidTankBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .sound(SoundType.GLASS)
                    .strength(3.0F, 8.0F)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .pushReaction(PushReaction.IGNORE)
    );

    public static final DeferredBlock<GratingBlock> GRATING = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.GRATING,
            GratingBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .isRedstoneConductor(((_, _, _) -> false))
                    .isValidSpawn(Blocks::never)
                    .strength(3.5F)
    );
    public static final DeferredBlock<CheeseBlock> CHEESE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.CHEESE_BLOCK,
            CheeseBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .forceSolidOn()
                    .strength(0.5F)
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY)
    );

    //Deco
    public static final DeferredBlock<UnlitTorchBlock> UNLIT_TORCH = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.UNLIT_TORCH,
            properties -> new UnlitTorchBlock(Blocks.TORCH.defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredBlock<UnlitTorchBlock> UNLIT_COPPER_TORCH = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.UNLIT_COPPER_TORCH,
            properties -> new UnlitTorchBlock(Blocks.COPPER_TORCH.defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredBlock<WallUnlitTorchBlock> UNLIT_WALL_TORCH = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.UNLIT_WALL_TORCH,
            properties -> new WallUnlitTorchBlock(Blocks.WALL_TORCH.defaultBlockState(), properties),
            () -> wallVariant(UNLIT_TORCH::value, true, properties -> properties
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
            )
    );
    public static final DeferredBlock<WallUnlitTorchBlock> UNLIT_COPPER_WALL_TORCH = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.UNLIT_COPPER_WALL_TORCH,
            properties -> new WallUnlitTorchBlock(Blocks.COPPER_WALL_TORCH.defaultBlockState(), properties),
            () -> wallVariant(UNLIT_COPPER_TORCH::value, true, properties -> properties
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
            )
    );
    public static final DeferredBlock<UnlitLanternBlock> UNLIT_LANTERN = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.UNLIT_LANTERN,
            properties -> new UnlitLanternBlock(Blocks.LANTERN.defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .strength(3.5F)
                    .sound(SoundType.LANTERN)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final WeatheringCopperCollection<DeferredBlock<Block>> UNLIT_COPPER_LANTERN = REGISTRY.registerBlocks(
            GalacticraftBlockItemIds.UNLIT_COPPER_LANTERN,
            (weatherState, properties) -> new UnlitLanternBlock(Blocks.COPPER_LANTERN.waxed().pick(weatherState).defaultBlockState(), properties),
            (weatherState, properties) -> new UnlitWeatheringLanternBlock(weatherState, Blocks.COPPER_LANTERN.weathering().pick(weatherState).defaultBlockState(), properties),
            _ -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .strength(3.5F)
                    .sound(SoundType.LANTERN)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredBlock<Block> TIN_DECORATION_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.TIN_DECORATION_BLOCK,
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> TIN_DECORATION_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.TIN_DECORATION_SLAB,
            SlabBlock::new,
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> TIN_DECORATION_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.TIN_DECORATION_STAIRS,
            properties -> new StairBlock(TIN_DECORATION_BLOCK.get().defaultBlockState(), properties),
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<WallBlock> TIN_DECORATION_WALL = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.TIN_DECORATION_WALL,
            WallBlock::new,
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> TIN_WALL_DECORATION_BLOCK = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.TIN_WALL_DECORATION_BLOCK,
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> TIN_WALL_DECORATION_SLAB = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.TIN_WALL_DECORATION_SLAB,
            SlabBlock::new,
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> TIN_WALL_DECORATION_STAIRS = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.TIN_WALL_DECORATION_STAIRS,
            properties -> new StairBlock(TIN_WALL_DECORATION_BLOCK.get().defaultBlockState(), properties),
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> SPACE_STATION = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.SPACE_STATION,
            builder -> builder
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
    );
    public static final DeferredBlock<NasaWorkbenchBlock> NASA_WORKBENCH = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.NASA_WORKBENCH,
            NasaWorkbenchBlock::new,
            builder -> builder
                    .strength(2.5F)
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
    );
    public static final DeferredBlock<CompactNasaWorkbenchBlock> COMPACT_NASA_WORKBENCH = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.COMPACT_NASA_WORKBENCH,
            CompactNasaWorkbenchBlock::new,
            builder -> builder
                    .strength(2.5F)
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
    );
    public static final ColorCollection<DeferredBlock<TintedGlassPaneBlock>> COLORED_TINTED_GLASS_PANE = REGISTRY.registerColoredBlocks(
            GalacticraftBlockItemIds.COLORED_TINTED_GLASS_PANE,
            (_, properties) -> new TintedGlassPaneBlock(properties),
            dyeColor -> BlockBehaviour.Properties.of()
                    .mapColor(dyeColor)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .isValidSpawn(GalacticraftBlocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
                    .noOcclusion()
    );
    public static final DeferredBlock<TintedGlassPaneBlock> TINTED_GLASS_PANE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.TINTED_GLASS_PANE,
            TintedGlassPaneBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .isValidSpawn(GalacticraftBlocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
                    .noOcclusion()
    );

    /** Machines */
    public static final DeferredBlock<OxygenDetectorBlock> OXYGEN_DETECTOR = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.OXYGEN_DETECTOR,
            OxygenDetectorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(1.0F)
    );

    public static final DeferredBlock<CoalGeneratorBlock> COAL_GENERATOR = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.COAL_GENERATOR,
            CoalGeneratorBlock::new,
            () -> litMachine(13)
    );
    public static final DeferredBlock<CircuitFabricatorBlock> CIRCUIT_FABRICATOR = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.CIRCUIT_FABRICATOR,
            CircuitFabricatorBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<CompressorBlock> COMPRESSOR = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.COMPRESSOR,
            CompressorBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<CompressorBlock> ELECTRIC_COMPRESSOR = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ELECTRIC_COMPRESSOR,
            ElectricCompressorBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<ElectricFurnaceBlock> ELECTRIC_FURNACE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ELECTRIC_FURNACE,
            ElectricFurnaceBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<ArcFurnaceBlock> ELECTRIC_ARC_FURNACE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.ELECTRIC_ARC_FURNACE,
            ArcFurnaceBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<OxygenCollectorBlock> OXYGEN_COLLECTOR = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.OXYGEN_COLLECTOR,
            OxygenCollectorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(1.0F)
    );
    public static final DeferredBlock<MagneticCraftingBlock> MAGNETIC_CRAFTING_TABLE = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.MAGNETIC_CRAFTING_TABLE,
            MagneticCraftingBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .sound(SoundType.METAL)
                    .strength(1.5F)
    );
    public static final DeferredBlock<LandingPadBlock> LANDING_PAD = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.LANDING_PAD,
            LandingPadBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(1.0F, 10.0F)
                    .forceSolidOn()
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isValidSpawn(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<FuelingPadBlock> FUELING_PAD = REGISTRY.registerBlock(
            GalacticraftBlockItemIds.FUELING_PAD,
            FuelingPadBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(1.0F, 10.0F)
                    .forceSolidOn()
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isValidSpawn(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<Block> ASTRO_MINER_BASE = REGISTRY.registerSimpleBlock(
            GalacticraftBlockItemIds.ASTRO_MINER_BASE,
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );

    private static BlockBehaviour.Properties wallVariant(Supplier<Block> baseBlock, boolean overrideDescription, UnaryOperator<BlockBehaviour.Properties> properties) {
        BlockBehaviour.Properties wallProperties = BlockBehaviour.Properties.of().overrideLootTable(baseBlock.get().getLootTable());
        if (overrideDescription) {
            wallProperties = wallProperties.overrideDescription(baseBlock.get().getDescriptionId());
        }

        return properties.apply(wallProperties);
    }

    private static BlockBehaviour.Properties machine() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.DEEPSLATE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .strength(3.0F);
    }
    private static BlockBehaviour.Properties litMachine(int light) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.DEEPSLATE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .strength(3.0F)
                .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? light : 0);
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static Stream<Block> getEntries() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::value);
    }
}
