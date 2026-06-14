/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredWeatheringCopperBlocks;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.block.cauldron.FlammableCauldronBlock;
import io.kalishak.galacticraftlegacy.world.level.block.cauldron.GalacticraftCauldronInteraction;
import io.kalishak.galacticraftlegacy.world.level.block.machine.*;
import io.kalishak.galacticraftlegacy.world.level.block.machine.oxygen.OxygenCollectorBlock;
import io.kalishak.galacticraftlegacy.world.level.block.wire.WireBlock;
import io.kalishak.galacticraftlegacy.world.level.block.wire.ColoredPipeBlock;
import io.kalishak.galacticraftlegacy.world.level.block.wire.HeavyWireBlock;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
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
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class GalacticraftBlocks {
    private static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Galacticraft.MODID);

    //Overworld ores
    public static final DeferredBlock<DropExperienceBlock> ALUMINUM_ORE = REGISTRY.registerBlock(
            "aluminum_ore",
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_ALUMINUM_ORE = REGISTRY.registerBlock(
            "deepslate_aluminum_ore",
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> RAW_ALUMINUM_BLOCK = REGISTRY.registerSimpleBlock(
            "raw_aluminum_block",
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> ALUMINUM_BLOCK = REGISTRY.registerSimpleBlock(
            "aluminum_block",
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> TIN_ORE = REGISTRY.registerBlock(
            "tin_ore",
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_TIN_ORE = REGISTRY.registerBlock(
            "deepslate_tin_ore",
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> RAW_TIN_BLOCK = REGISTRY.registerSimpleBlock(
            "raw_tin_block",
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> TIN_BLOCK = REGISTRY.registerSimpleBlock(
            "tin_block",
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> SILICON_ORE = REGISTRY.registerBlock(
            "silicon_ore",
            properties -> new DropExperienceBlock(UniformInt.of(0, 2), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_SILICON_ORE = REGISTRY.registerBlock(
            "deepslate_silicon_ore",
            properties -> new DropExperienceBlock(UniformInt.of(0, 2), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.0F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> RAW_SILICON_BLOCK = REGISTRY.registerSimpleBlock(
            "raw_silicon_block",
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<AirBlock> EMPTY_AIR = REGISTRY.registerBlock(
            "empty_air",
            AirBlock::new,
            () -> BlockBehaviour.Properties.of().replaceable().noCollision().noLootTable().air()
    );
    public static final DeferredBlock<AirBlock> OXYGEN_AIR = REGISTRY.registerBlock(
            "oxygen_air",
            AirBlock::new,
            () -> BlockBehaviour.Properties.of().replaceable().noCollision().noLootTable().air()
    );

    public static final DeferredBlock<ParachestBlock> PARACHEST = REGISTRY.registerBlock(
            "parachest_minimal",
            properties -> new ParachestBlock(ParachestBlock.Type.MINIMAL, properties),
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );
    public static final DeferredBlock<ParachestBlock> PARACHEST_18 = REGISTRY.registerBlock(
            "parachest_single",
            properties -> new ParachestBlock(ParachestBlock.Type.MINIMAL, properties),
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );
    public static final DeferredBlock<ParachestBlock> PARACHEST_36 = REGISTRY.registerBlock(
            "parachest_double",
            properties -> new ParachestBlock(ParachestBlock.Type.MINIMAL, properties),
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );
    public static final DeferredBlock<ParachestBlock> PARACHEST_54 = REGISTRY.registerBlock(
            "parachest_triple",
            properties -> new ParachestBlock(ParachestBlock.Type.MINIMAL, properties),
            () -> BlockBehaviour.Properties.of()
                    .noLootTable()
    );
    public static final DeferredBlock<DungeonChestBlock> MOON_DUNGEON_CHEST = REGISTRY.registerBlock(
            "moon_dungeon_chest",
            properties -> new DungeonChestBlock(FeatureTier.TIER_1, properties),
            () -> BlockBehaviour.Properties.of()
                    .noTerrainParticles()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
                    .isValidSpawn(Blocks::never)
    );
    public static final DeferredBlock<DungeonChestBlock> MARS_DUNGEON_CHEST = REGISTRY.registerBlock(
            "mars_dungeon_chest",
            properties -> new DungeonChestBlock(FeatureTier.TIER_2, properties),
            () -> BlockBehaviour.Properties.of()
                    .noTerrainParticles()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
                    .isValidSpawn(Blocks::never)
    );
    public static final DeferredBlock<DungeonChestBlock> VENUS_DUNGEON_CHEST = REGISTRY.registerBlock(
            "venus_dungeon_chest",
            properties -> new DungeonChestBlock(FeatureTier.TIER_3, properties),
            () -> BlockBehaviour.Properties.of()
                    .noTerrainParticles()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
                    .isValidSpawn(Blocks::never)
    );

    //MOON BLOCKS
    public static final DeferredBlock<TerraformableBlock> MOON_DIRT = REGISTRY.registerBlock(
            "moon_dirt",
            TerraformableBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(0.5F, 1.0F)
    );
    public static final DeferredBlock<TerraformableRotatedBlock> MOON_TURF = REGISTRY.registerBlock(
            "moon_turf",
            TerraformableRotatedBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(0.5F, 1.0F)
    );

    public static final DeferredBlock<Block> MOON_ROCK = REGISTRY.registerBlock(
            "moon_rock",
            Block::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .strength(1.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> MOON_COPPER_ORE = REGISTRY.registerBlock(
            "moon_copper_ore",
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> MOON_TIN_ORE = REGISTRY.registerBlock(
            "moon_tin_ore",
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> MOON_SAPPHIRE_ORE = REGISTRY.registerBlock(
            "moon_sapphire_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<DropExperienceBlock> MOON_CHEESE_ORE = REGISTRY.registerBlock(
            "moon_cheese_ore",
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.5F, 3.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<Block> MOON_BRICKS = REGISTRY.registerSimpleBlock(
            "moon_bricks",
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(2.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> MOON_BRICK_STAIRS = REGISTRY.registerBlock(
            "moon_brick_stairs",
            properties -> new StairBlock(MOON_BRICKS.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> MOON_BRICK_SLAB = REGISTRY.registerBlock(
            "moon_brick_slab",
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> MOON_BRICK_WALL = REGISTRY.registerBlock(
            "moon_brick_wall",
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<FallenMeteorBlock> FALLEN_METEOR = REGISTRY.registerBlock(
            "fallen_meteor",
            FallenMeteorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(5.5F, 40.0F)
                    .instrument(NoteBlockInstrument.BASS)
                    .requiresCorrectToolForDrops()
    );

    /** Asteroids */
    public static final DeferredBlock<Block> ASTEROID_ROCK = REGISTRY.registerSimpleBlock(
            "asteroid_rock",
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> ASTEROID_ROCK_SLAB = REGISTRY.registerBlock(
            "asteroid_rock_slab",
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> ASTEROID_ROCK_STAIRS = REGISTRY.registerBlock(
            "asteroid_rock_stairs",
            properties -> new StairBlock(ASTEROID_ROCK.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<WallBlock> ASTEROID_ROCK_WALL = REGISTRY.registerBlock(
            "asteroid_rock_wall",
            WallBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DropExperienceBlock> ASTEROID_ALUMINUM_ORE = REGISTRY.registerBlock(
            "asteroid_aluminum_ore",
            properties -> new DropExperienceBlock(ConstantInt.ZERO, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
    );

    // Fluids
    public static final DeferredBlock<LiquidBlock> OIL = REGISTRY.registerBlock(
            "oil",
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
            "fuel",
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
    public static final DeferredBlock<ColoredPipeBlock> WHITE_PIPE = REGISTRY.registerBlock(
            "white_pipe",
            properties -> new ColoredPipeBlock(DyeColor.WHITE, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.WHITE)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> ORANGE_PIPE = REGISTRY.registerBlock(
            "orange_pipe",
            properties -> new ColoredPipeBlock(DyeColor.ORANGE, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.ORANGE)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> MAGENTA_PIPE = REGISTRY.registerBlock(
            "magenta_pipe",
            properties -> new ColoredPipeBlock(DyeColor.MAGENTA, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.MAGENTA)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );

    public static final DeferredBlock<ColoredPipeBlock> LIGHT_BLUE_PIPE = REGISTRY.registerBlock(
            "light_blue_pipe",
            properties -> new ColoredPipeBlock(DyeColor.LIGHT_BLUE, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.LIGHT_BLUE)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> YELLOW_PIPE = REGISTRY.registerBlock(
            "yellow_pipe",
            properties -> new ColoredPipeBlock(DyeColor.YELLOW, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.YELLOW)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> LIME_PIPE = REGISTRY.registerBlock(
            "lime_pipe",
            properties -> new ColoredPipeBlock(DyeColor.LIME, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.LIME)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> PINK_PIPE = REGISTRY.registerBlock(
            "pink_pipe",
            properties -> new ColoredPipeBlock(DyeColor.PINK, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.PINK)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> GRAY_PIPE = REGISTRY.registerBlock(
            "gray_pipe",
            properties -> new ColoredPipeBlock(DyeColor.GRAY, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.GRAY)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> LIGHT_GRAY_PIPE = REGISTRY.registerBlock(
            "light_gray_pipe",
            properties -> new ColoredPipeBlock(DyeColor.LIGHT_GRAY, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> CYAN_PIPE = REGISTRY.registerBlock(
            "cyan_pipe",
            properties -> new ColoredPipeBlock(DyeColor.CYAN, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.CYAN)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> PURPLE_PIPE = REGISTRY.registerBlock(
            "purple_pipe",
            properties -> new ColoredPipeBlock(DyeColor.PURPLE, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.PURPLE)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> BLUE_PIPE = REGISTRY.registerBlock(
            "blue_pipe",
            properties -> new ColoredPipeBlock(DyeColor.BLUE, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.BLUE)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> BROWN_PIPE = REGISTRY.registerBlock(
            "brown_pipe",
            properties -> new ColoredPipeBlock(DyeColor.BROWN, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.BROWN)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> GREEN_PIPE = REGISTRY.registerBlock(
            "green_pipe",
            properties -> new ColoredPipeBlock(DyeColor.GREEN, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.GREEN)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> RED_PIPE = REGISTRY.registerBlock(
            "red_pipe",
            properties -> new ColoredPipeBlock(DyeColor.RED, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.RED)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(GalacticraftBlocks::never)
                    .isSuffocating(GalacticraftBlocks::never)
                    .isViewBlocking(GalacticraftBlocks::never)
    );
    public static final DeferredBlock<ColoredPipeBlock> BLACK_PIPE = REGISTRY.registerBlock(
            "black_pipe",
            properties -> new ColoredPipeBlock(DyeColor.BLACK, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.BLACK)
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
            "aluminum_wire",
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
            "heavy_aluminum_wire",
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
            "oil_cauldron",
            properties -> new FlammableCauldronBlock(GalacticraftCauldronInteraction.OIL, GalacticraftFluids.OIL, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F)
                    .noOcclusion()
    );
    public static final DeferredBlock<FlammableCauldronBlock> FUEL_CAULDRON = REGISTRY.registerBlock(
            "fuel_cauldron",
            properties -> new FlammableCauldronBlock(GalacticraftCauldronInteraction.FUEL, GalacticraftFluids.FUEL, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F)
                    .noOcclusion()
    );

    public static final DeferredBlock<GratingBlock> GRATING = REGISTRY.registerBlock(
            "grating",
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
            "cheese",
            CheeseBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .forceSolidOn()
                    .strength(0.5F)
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY)
    );

    //Deco
    public static final DeferredBlock<UnlitTorchBlock> UNLIT_TORCH = REGISTRY.registerBlock(
            "unlit_torch",
            properties -> new UnlitTorchBlock(Blocks.TORCH.defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredBlock<UnlitTorchBlock> UNLIT_COPPER_TORCH = REGISTRY.registerBlock(
            "unlit_copper_torch",
            properties -> new UnlitTorchBlock(Blocks.COPPER_TORCH.defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredBlock<WallUnlitTorchBlock> UNLIT_WALL_TORCH = REGISTRY.registerBlock(
            "unlit_wall_torch",
            properties -> new WallUnlitTorchBlock(Blocks.WALL_TORCH.defaultBlockState(), properties),
            () -> wallVariant(UNLIT_TORCH::value, true, properties -> properties
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
            )
    );
    public static final DeferredBlock<WallUnlitTorchBlock> UNLIT_COPPER_WALL_TORCH = REGISTRY.registerBlock(
            "unlit_wall_copper_torch",
            properties -> new WallUnlitTorchBlock(Blocks.COPPER_WALL_TORCH.defaultBlockState(), properties),
            () -> wallVariant(UNLIT_COPPER_TORCH::value, true, properties -> properties
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
            )
    );
    public static final DeferredBlock<UnlitLanternBlock> UNLIT_LANTERN = REGISTRY.registerBlock(
            "unlit_lantern",
            properties -> new UnlitLanternBlock(Blocks.LANTERN.defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .strength(3.5F)
                    .sound(SoundType.LANTERN)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredWeatheringCopperBlocks UNLIT_COPPER_LANTERN = DeferredWeatheringCopperBlocks.create(
            "unlit_copper_lantern",
            REGISTRY::registerBlock,
            properties -> new UnlitLanternBlock(Blocks.COPPER_LANTERN.unaffected().defaultBlockState(), properties),
            (weatherState, properties) -> new UnlitWeatheringLanternBlock(weatherState, Blocks.COPPER_LANTERN.unaffected().defaultBlockState(), properties),
            _ -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .strength(3.5F)
                    .sound(SoundType.LANTERN)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final DeferredBlock<Block> TIN_DECORATION_BLOCK = REGISTRY.registerSimpleBlock(
            "tin_decoration_block",
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> TIN_DECORATION_CUT_BLOCK = REGISTRY.registerSimpleBlock(
            "tin_decoration_cut_block",
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<SlabBlock> TIN_DECORATION_SLAB = REGISTRY.registerBlock(
            "tin_decoration_slab",
            SlabBlock::new,
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<StairBlock> TIN_DECORATION_STAIRS = REGISTRY.registerBlock(
            "tin_decoration_stairs",
            properties -> new StairBlock(TIN_DECORATION_BLOCK.get().defaultBlockState(), properties),
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<WallBlock> TIN_DECORATION_WALL = REGISTRY.registerBlock(
            "tin_decoration_wall",
            WallBlock::new,
            properties -> properties
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .strength(1.0F, 15.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<Block> SPACE_STATION = REGISTRY.registerSimpleBlock(
            "space_station",
            builder -> builder
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
    );

    /** Machines */
    public static final DeferredBlock<OxygenDetectorBlock> OXYGEN_DETECTOR = REGISTRY.registerBlock(
            "oxygen_detector",
            OxygenDetectorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(1.0F)
    );

    public static final DeferredBlock<CoalGeneratorBlock> COAL_GENERATOR = REGISTRY.registerBlock(
            "coal_generator",
            CoalGeneratorBlock::new,
            () -> litMachine(13)
    );
    public static final DeferredBlock<CircuitFabricatorBlock> CIRCUIT_FABRICATOR = REGISTRY.registerBlock(
            "circuit_fabricator",
            CircuitFabricatorBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<CompressorBlock> COMPRESSOR = REGISTRY.registerBlock(
            "compressor",
            CompressorBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<CompressorBlock> ELECTRIC_COMPRESSOR = REGISTRY.registerBlock(
            "electric_compressor",
            ElectricCompressorBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<ElectricFurnaceBlock> ELECTRIC_FURNACE = REGISTRY.registerBlock(
            "electric_furnace",
            ElectricFurnaceBlock::new,
            GalacticraftBlocks::machine
    );
    public static final DeferredBlock<OxygenCollectorBlock> OXYGEN_COLLECTOR = REGISTRY.registerBlock(
            "oxygen_collector",
            OxygenCollectorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(1.0F)
    );
    public static final DeferredBlock<MagneticCraftingBlock> MAGNETIC_CRAFTING_TABLE = REGISTRY.registerBlock(
            "magnetic_crafting_table",
            MagneticCraftingBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .sound(SoundType.METAL)
                    .strength(1.5F)
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

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static Stream<Block> getEntries() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::value);
    }
}
