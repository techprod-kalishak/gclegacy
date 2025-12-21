package io.kalishak.galacticraftlegacy.attachment;

import io.kalishak.galacticraftlegacy.GalacticraftLegacy;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jspecify.annotations.NonNull;

public final class GalacticraftAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, GalacticraftLegacy.MODID);

    public static final DeferredHolder<AttachmentType<?>, @NonNull AttachmentType<SpacePlayerData>> SPACE_DATA = ATTACHMENTS.register(
            "space_data",
            () -> AttachmentType.builder(() -> new SpacePlayerData()).serialize(SpacePlayerData.CODEC).sync(SpacePlayerData.STREAM_CODEC).copyOnDeath().copyHandler(SpacePlayerData::copyOnDeath).build()
    );

    public static void init(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}
