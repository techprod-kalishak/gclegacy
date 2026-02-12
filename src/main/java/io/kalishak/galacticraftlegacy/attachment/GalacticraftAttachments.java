package io.kalishak.galacticraftlegacy.attachment;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.entity.EntityGearInventory;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.attachment.level.race.FlagData;
import io.kalishak.galacticraftlegacy.attachment.level.race.SpaceRaceManager;
import io.kalishak.galacticraftlegacy.attachment.level.race.SpaceRaceTeam;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.item.component.SchematicContent;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EitherHolder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class GalacticraftAttachments {
    private static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Galacticraft.MODID);

    //Entity
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FlagData>> DATA_FLAG = REGISTRY.register(
            "data_flag",
            () -> AttachmentType.builder(() -> FlagData.DEFAULT)
                    .serialize(FlagData.MAP_CODEC)
                    .sync(FlagData.STREAM_CODEC)
                    .build()
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SchematicContent>> DATA_SCHEMATIC = REGISTRY.register(
            "data_schematic",
            () -> AttachmentType.builder(() -> SchematicContent.DEFAULT)
                    .serialize(SchematicContent.MAP_CODEC)
                    .sync(SchematicContent.STREAM_CODEC)
                    .build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerSpaceData>> PLAYER_SPACE_DATA = REGISTRY.register(
            "player_space_data",
            () -> AttachmentType.builder(PlayerSpaceData::new)
                    .serialize(PlayerSpaceData.CODEC)
                    .sync(PlayerSpaceData.STREAM_CODEC)
                    .copyOnDeath()
                    .copyHandler(PlayerSpaceData::copyOnDeath).build()
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<EntityGearInventory>> ENTITY_GEAR_INVENTORY = REGISTRY.register(
            "entity_gear_inventory",
            () -> AttachmentType.builder(EntityGearInventory::new)
                    .serialize(EntityGearInventory.CODEC, EntityGearInventory::shouldSave)
                    .sync(EntityGearInventory.STREAM_CODEC)
                    .build()
    );

    //Level
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpaceRaceManager>> SPACE_RACE_MANAGER = REGISTRY.register(
            "space_race_manager",
            () -> AttachmentType.builder(SpaceRaceManager::new)
                    .serialize(SpaceRaceManager.MAP_CODEC)
                    .sync(SpaceRaceManager.STREAM_CODEC)
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
