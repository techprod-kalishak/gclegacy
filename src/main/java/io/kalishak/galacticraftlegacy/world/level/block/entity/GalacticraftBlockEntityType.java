package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class GalacticraftBlockEntityType {
    private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricFurnaceBlockEntity>> ELECTRIC_FURNACE = REGISTRY.register(
            "electric_furnace",
            () -> new BlockEntityType<>(ElectricFurnaceBlockEntity::new, Set.of(GalacticraftBlocks.ELECTRIC_FURNACE.get()))
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR = REGISTRY.register(
            "coal_generator",
            () -> new BlockEntityType<>(CoalGeneratorBlockEntity::new, Set.of(GalacticraftBlocks.COAL_GENERATOR.get()))
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CircuitFabricatorBlockEntity>> CIRCUIT_FABRICATOR = REGISTRY.register(
            "circuit_fabricator",
            () -> new BlockEntityType<>(CircuitFabricatorBlockEntity::new, Set.of(GalacticraftBlocks.CIRCUIT_FABRICATOR.get()))
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ParachestBlockEntity>> PARACHEST = REGISTRY.register(
            "parachest",
            () -> new BlockEntityType<>(ParachestBlockEntity::new, Set.of(GalacticraftBlocks.PARACHEST.get()))
    );

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        CoalGeneratorBlockEntity.registerCapabilities(event);
        CircuitFabricatorBlockEntity.registerCapabilities(event);
        ElectricFurnaceBlockEntity.registerCapabilities(event);
        ParachestBlockEntity.registerCapabilities(event);
    }

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftBlockEntityType::registerCapabilities);
    }
}
