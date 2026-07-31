/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredBlockEntityTypeRegister;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.*;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.WireBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ColoredPipeBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.DenseWireBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;

public final class GalacticraftBlockEntityType {
    private static final DeferredBlockEntityTypeRegister REGISTRY = DeferredBlockEntityTypeRegister.createBlockEntities(Galacticraft.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricArcFurnaceBlockEntity>> ELECTRIC_ARC_FURNACE = REGISTRY.registerBlockEntity(
            "electric_arc_furnace",
            ElectricArcFurnaceBlockEntity::new,
            GalacticraftBlocks.ELECTRIC_ARC_FURNACE
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ColoredPipeBlockEntity>> COLORED_PIPE = REGISTRY.registerBlockEntity(
            "colored_pipe",
            ColoredPipeBlockEntity::new,
            Set.copyOf(GalacticraftBlocks.FLUID_PIPE.asList())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CompressorBlockEntity>> COMPRESSOR = REGISTRY.registerBlockEntity(
            "compressor",
            CompressorBlockEntity::new,
            GalacticraftBlocks.COMPRESSOR
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DungeonChestBlockEntity>> DUNGEON_CHEST = REGISTRY.registerBlockEntity(
            "dungeon_chest",
            DungeonChestBlockEntity::new,
            Set.of(
                    GalacticraftBlocks.MOON_DUNGEON_CHEST,
                    GalacticraftBlocks.MARS_DUNGEON_CHEST,
                    GalacticraftBlocks.VENUS_DUNGEON_CHEST
            )
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricCompressorBlockEntity>> ELECTRIC_COMPRESSOR = REGISTRY.registerBlockEntity(
            "electric_compressor",
            ElectricCompressorBlockEntity::new,
            GalacticraftBlocks.ELECTRIC_COMPRESSOR
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricFurnaceBlockEntity>> ELECTRIC_FURNACE = REGISTRY.registerBlockEntity(
            "electric_furnace",
            ElectricFurnaceBlockEntity::new,
            GalacticraftBlocks.ELECTRIC_FURNACE
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FallenMeteorBlockEntity>> FALLEN_METEOR = REGISTRY.registerBlockEntity(
            "fallen_meteor",
            FallenMeteorBlockEntity::new,
            GalacticraftBlocks.FALLEN_METEOR
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FlammableCauldronBlockEntity>> FLAMMABLE_CAULDRON = REGISTRY.registerBlockEntity(
            "flammable_cauldron",
            FlammableCauldronBlockEntity::new,
            Set.of(
                    GalacticraftBlocks.OIL_CAULDRON,
                    GalacticraftBlocks.FUEL_CAULDRON
            )
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR = REGISTRY.registerBlockEntity(
            "coal_generator",
            CoalGeneratorBlockEntity::new,
            GalacticraftBlocks.COAL_GENERATOR
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CircuitFabricatorBlockEntity>> CIRCUIT_FABRICATOR = REGISTRY.registerBlockEntity(
            "circuit_fabricator",
            CircuitFabricatorBlockEntity::new,
            GalacticraftBlocks.CIRCUIT_FABRICATOR
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LaunchControllerBlockEntity>> LAUNCH_CONTROLLER = REGISTRY.registerBlockEntity(
            "launch_controller",
            LaunchControllerBlockEntity::new,
            Set.of()
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MagneticCraftingBlockEntity>> MAGNETIC_CRAFTING = REGISTRY.registerBlockEntity(
            "magnetic_crafting_table",
            MagneticCraftingBlockEntity::new,
            GalacticraftBlocks.MAGNETIC_CRAFTING_TABLE
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ParachestBlockEntity>> PARACHEST = REGISTRY.registerBlockEntity(
            "parachest",
            ParachestBlockEntity::new,
            Set.of(
                    GalacticraftBlocks.PARACHEST,
                    GalacticraftBlocks.PARACHEST_18,
                    GalacticraftBlocks.PARACHEST_36,
                    GalacticraftBlocks.PARACHEST_54
            )
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OxygenCollectorBlockEntity>> OXYGEN_COLLECTOR = REGISTRY.registerBlockEntity(
            "oxygen_collector",
            OxygenCollectorBlockEntity::new,
            GalacticraftBlocks.OXYGEN_COLLECTOR
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OxygenDetectorBlockEntity>> OXYGEN_DETECTOR = REGISTRY.registerBlockEntity(
            "oxygen_detector",
            OxygenDetectorBlockEntity::new,
            GalacticraftBlocks.OXYGEN_DETECTOR
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WireBlockEntity>> WIRE = REGISTRY.registerBlockEntity(
            "wire",
            WireBlockEntity::new,
            GalacticraftBlocks.ALUMINUM_WIRE
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DenseWireBlockEntity>> DENSE_WIRE = REGISTRY.registerBlockEntity(
            "dense_wire",
            DenseWireBlockEntity::new,
            GalacticraftBlocks.HEAVY_ALUMINUM_WIRE
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NasaWorkbenchBlockEntity>> NASA_WORKBENCH = REGISTRY.registerBlockEntity(
            "nasa_workbench",
            NasaWorkbenchBlockEntity::new,
            GalacticraftBlocks.NASA_WORKBENCH
    );

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        CircuitFabricatorBlockEntity.registerCapabilities(event);
        CoalGeneratorBlockEntity.registerCapabilities(event);
        ColoredPipeBlockEntity.registerCapabilities(event);
        CompressorBlockEntity.registerItemCapabilities(event);
        DungeonChestBlockEntity.registerCapabilities(event);
        ElectricCompressorBlockEntity.registerCapabilities(event);
        AbstractElectricFurnaceBlockEntity.registerCapabilities(event, ELECTRIC_FURNACE.get());
        AbstractElectricFurnaceBlockEntity.registerCapabilities(event, ELECTRIC_ARC_FURNACE.get());
        FlammableCauldronBlockEntity.registerCapability(event);
        MagneticCraftingBlockEntity.registerCapabilities(event);
        ParachestBlockEntity.registerCapabilities(event);
        OxygenCollectorBlockEntity.registerCapabilities(event);
        WireBlockEntity.registerCapabilities(event, WIRE.get());
        WireBlockEntity.registerCapabilities(event, DENSE_WIRE.get());
    }

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftBlockEntityType::registerCapabilities);
    }
}
