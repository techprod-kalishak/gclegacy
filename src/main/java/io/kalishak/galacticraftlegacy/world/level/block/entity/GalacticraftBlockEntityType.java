package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.*;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.WireBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ColoredPipeBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.DenseWireBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class GalacticraftBlockEntityType {
    private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ColoredPipeBlockEntity>> COLORED_PIPE = REGISTRY.register(
            "colored_pipe",
            () -> new BlockEntityType<>(ColoredPipeBlockEntity::new, Set.of(
                    GalacticraftBlocks.WHITE_PIPE.get(),
                    GalacticraftBlocks.ORANGE_PIPE.get(),
                    GalacticraftBlocks.MAGENTA_PIPE.get(),
                    GalacticraftBlocks.LIGHT_BLUE_PIPE.get(),
                    GalacticraftBlocks.YELLOW_PIPE.get(),
                    GalacticraftBlocks.LIME_PIPE.get(),
                    GalacticraftBlocks.PINK_PIPE.get(),
                    GalacticraftBlocks.GRAY_PIPE.get(),
                    GalacticraftBlocks.LIGHT_GRAY_PIPE.get(),
                    GalacticraftBlocks.CYAN_PIPE.get(),
                    GalacticraftBlocks.PURPLE_PIPE.get(),
                    GalacticraftBlocks.BLUE_PIPE.get(),
                    GalacticraftBlocks.BROWN_PIPE.get(),
                    GalacticraftBlocks.GREEN_PIPE.get(),
                    GalacticraftBlocks.RED_PIPE.get(),
                    GalacticraftBlocks.BLACK_PIPE.get()
            ))
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DungeonChestBlockEntity>> DUNGEON_CHEST = REGISTRY.register(
            "dungeon_chest",
            () -> new BlockEntityType<>(DungeonChestBlockEntity::new, Set.of(
                    GalacticraftBlocks.MOON_DUNGEON_CHEST.get(),
                    GalacticraftBlocks.MARS_DUNGEON_CHEST.get(),
                    GalacticraftBlocks.VENUS_DUNGEON_CHEST.get()
            ))
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FlammableCauldronBlockEntity>> FLAMMABLE_CAULDRON = REGISTRY.register(
            "flammable_cauldron",
            () -> new BlockEntityType<>(FlammableCauldronBlockEntity::new, Set.of(
                    GalacticraftBlocks.OIL_CAULDRON.get(),
                    GalacticraftBlocks.FUEL_CAULDRON.get()
            ))
    );
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
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LaunchControllerBlockEntity>> LAUNCH_CONTROLLER = REGISTRY.register(
            "launch_controller",
            () -> new BlockEntityType<>(LaunchControllerBlockEntity::new, Set.of())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ParachestBlockEntity>> PARACHEST = REGISTRY.register(
            "parachest",
            () -> new BlockEntityType<>(ParachestBlockEntity::new, Set.of(
                    GalacticraftBlocks.PARACHEST.get(),
                    GalacticraftBlocks.PARACHEST_18.get(),
                    GalacticraftBlocks.PARACHEST_36.get(),
                    GalacticraftBlocks.PARACHEST_54.get()
            ))
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OxygenDetectorBlockEntity>> OXYGEN_DETECTOR = REGISTRY.register(
            "oxygen_detector",
            () -> new BlockEntityType<>(OxygenDetectorBlockEntity::new, Set.of(
                    GalacticraftBlocks.OXYGEN_DETECTOR.get())
            )
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WireBlockEntity>> WIRE = REGISTRY.register(
            "wire",
            () -> new BlockEntityType<>(WireBlockEntity::new, Set.of(
                    GalacticraftBlocks.ALUMINUM_WIRE.get()
            ))
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DenseWireBlockEntity>> DENSE_WIRE = REGISTRY.register(
            "dense_wire",
            () -> new BlockEntityType<>(DenseWireBlockEntity::new, Set.of(
                    GalacticraftBlocks.HEAVY_ALUMINUM_WIRE.get()
            ))
    );

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        CoalGeneratorBlockEntity.registerCapabilities(event);
        CircuitFabricatorBlockEntity.registerCapabilities(event);
        ElectricFurnaceBlockEntity.registerCapabilities(event);
        ParachestBlockEntity.registerCapabilities(event);
        ColoredPipeBlockEntity.registerCapabilities(event);
        WireBlockEntity.registerCapabilities(event, WIRE.get());
        WireBlockEntity.registerCapabilities(event, DENSE_WIRE.get());
        FlammableCauldronBlockEntity.registerCapability(event);
    }

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftBlockEntityType::registerCapabilities);
    }
}
