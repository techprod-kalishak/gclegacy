package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class GalacticraftBlockEntityType {
    private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<BlockEntityType<?>, @NonNull BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR = REGISTRY.register(
            "coal_generator",
            () -> new BlockEntityType<>(CoalGeneratorBlockEntity::new, Set.of(GalacticraftBlocks.COAL_GENERATOR.get()))
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<@NonNull CircuitFabricatorBlockEntity>> CIRCUIT_FABRICATOR = REGISTRY.register(
            "circuit_fabricator",
            () -> new BlockEntityType<>(CircuitFabricatorBlockEntity::new, Set.of(GalacticraftBlocks.CIRCUIT_FABRICATOR.get()))
    );

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        CoalGeneratorBlockEntity.registerCapabilities(event);
    }

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftBlockEntityType::registerCapabilities);
    }
}
