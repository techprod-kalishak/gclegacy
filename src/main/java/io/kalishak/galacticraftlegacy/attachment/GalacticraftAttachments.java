package io.kalishak.galacticraftlegacy.attachment;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jspecify.annotations.NonNull;

public final class GalacticraftAttachments {
    private static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Galacticraft.MODID);

    public static final DeferredHolder<AttachmentType<?>, @NonNull AttachmentType<GearInventory>> GEAR_INVENTORY = REGISTRY.register(
            "space_data",
            () -> AttachmentType.builder(GearInventory::new)
                    .serialize(GearInventory.CODEC)
                    .sync(GearInventory.STREAM_CODEC)
                    .copyOnDeath()
                    .copyHandler(GearInventory::copyOnDeath).build()
    );

    public static void init(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
