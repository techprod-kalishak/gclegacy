package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Stream;

public final class GalacticraftBlocks {
    private static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Galacticraft.MODID);

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
            "parachest",
            ParachestBlock::new,
            BlockBehaviour.Properties::of
    );

    //MOON BLOCKS
    public static final DeferredBlock<Block> MOON_ROCK = REGISTRY.registerBlock(
            "moon_rock",
            Block::new,
            BlockBehaviour.Properties::of
    );

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

    public static final DeferredBlock<CoalGeneratorBlock> COAL_GENERATOR = REGISTRY.registerBlock(
            "coal_generator",
            CoalGeneratorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F)
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)
    );
    public static final DeferredBlock<CircuitFabricatorBlock> CIRCUIT_FABRICATOR = REGISTRY.registerBlock(
            "circuit_fabricator",
            CircuitFabricatorBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F)
    );
    public static final DeferredBlock<ElectricFurnaceBlock> ELECTRIC_FURNACE = REGISTRY.registerBlock(
            "electric_furnace",
            ElectricFurnaceBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static Stream<Block> getEntries() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::value);
    }
}
