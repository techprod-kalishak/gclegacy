/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedFluidResource;
import io.kalishak.galacticraftlegacy.attachment.entity.AdvancedMovement;
import io.kalishak.galacticraftlegacy.attachment.entity.EntityGearInventory;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.entity.FlagData;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.MachineStatus;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

public final class GalacticraftAttachments {
    private static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Galacticraft.MODID);

    //Block
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<MachineStatus>> MACHINE_STATUS = REGISTRY.register(
            "machine_status",
            () -> AttachmentType.builder(() -> new MachineStatus(MachineStatus.Type.IDLE))
                    .serialize(MachineStatus.MAP_CODEC)
                    .sync(MachineStatus.STREAM_CODEC)
                    .build()
    );
    @ApiStatus.Internal
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SyncedEnergyHandler>> SYNC_ENERGY_STORAGE = REGISTRY.register(
            "energy_storage",
            () -> AttachmentType.builder(SyncedEnergyHandler::fromBlockEntity)
                    .sync(SyncedEnergyHandler.STREAM_CODEC)
                    .build()
    );
    @ApiStatus.Internal
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SyncedFluidResource>> SYNC_FLUID_STORAGE = REGISTRY.register(
            "fluid_storage",
            () -> AttachmentType.builder(SyncedFluidResource::fromBlockEntity)
                    .sync(SyncedFluidResource.STREAM_CODEC)
                    .build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FluidStack>> SYNC_FLUID_STACK = REGISTRY.register(
            "fluidstack",
            () -> AttachmentType.builder(() -> FluidStack.EMPTY)
                    .sync(FluidStack.OPTIONAL_STREAM_CODEC)
                    .build()
    );

    //Entity
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AdvancedMovement>> ADVANCED_MOVEMENT = REGISTRY.register(
            "advanced_movement",
            () -> AttachmentType.builder(AdvancedMovement::new)
                    .serialize(AdvancedMovement.MAP_CODEC)
                    .sync(AdvancedMovement.STREAM_CODEC)
                    .build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FlagData>> DATA_FLAG = REGISTRY.register(
            "data_flag",
            () -> AttachmentType.builder(() -> FlagData.DEFAULT)
                    .serialize(FlagData.MAP_CODEC)
                    .sync(FlagData.STREAM_CODEC)
                    .build()
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Holder<SchematicVariant>>> DATA_SCHEMATIC = REGISTRY.register(
            "data_schematic",
            () -> AttachmentType.builder(attachmentHolder -> {
                if (attachmentHolder instanceof Entity entity) {
                    return entity.registryAccess().getOrThrow(SchematicVariants.TIER_2_ROCKET).getDelegate();
                }

                throw new IllegalArgumentException(attachmentHolder.getClass() + " should not store the schematic data!");
            })
                    .serialize(SchematicVariant.MAP_CODEC)
                    .sync(SchematicVariant.STREAM_CODEC)
                    .build()
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerSpaceData>> PLAYER_SPACE_DATA = REGISTRY.register(
            "player_space_data",
            () -> AttachmentType.builder(PlayerSpaceData::new)
                    .serialize(PlayerSpaceData.MAP_CODEC)
                    .sync(PlayerSpaceData.STREAM_CODEC)
                    .copyOnDeath()
                    .copyHandler(PlayerSpaceData::copyOnDeath).build()
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<EntityGearInventory>> ENTITY_GEAR_INVENTORY = REGISTRY.register(
            "entity_gear_inventory",
            () -> AttachmentType.builder(EntityGearInventory::new)
                    .serialize(EntityGearInventory.MAP_CODEC, EntityGearInventory::shouldSave)
                    .sync(EntityGearInventory.STREAM_CODEC)
                    .build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Holder<CelestialBodyLevelData>>> CELESTIAL_BODY = REGISTRY.register(
            "celestial_body_data",
            () -> AttachmentType.builder(CelestialBodyLevelData::fromLevel).build()
    );

    public static void init(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
