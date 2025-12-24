package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.stream.Stream;

public final class GalacticraftBlocks {
    private static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Galacticraft.MODID);

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

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static Stream<Block> getEntries() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::value);
    }
}
