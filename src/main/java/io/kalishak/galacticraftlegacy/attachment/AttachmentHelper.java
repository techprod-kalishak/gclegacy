package io.kalishak.galacticraftlegacy.attachment;

import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface AttachmentHelper {
    static GearInventoryProvider getGearInventory(Entity entity) {
        if (entity.is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)) {
            if (entity instanceof Player) {
                return entity.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
            }

            return entity.getData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY);
        }

        throw new IllegalArgumentException("Attempted to extract GearInventoryProvider from disallowed entity type: " + entity.getType() + ", is it in tag: " + GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR + "?");
    }

    static boolean hasGearInventory(Entity entity) {
        return entity.hasData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY) || entity.hasData(GalacticraftAttachments.PLAYER_SPACE_DATA);
    }

    static <T> Optional<T> get(AttachmentHolder attachmentHolder, Supplier<AttachmentType<T>> attachmentType) {
        if (attachmentHolder.hasData(attachmentType)) {
            return Optional.of(attachmentHolder.getData(attachmentType));
        }

        return Optional.empty();
    }

    static <T, S> Optional<S> getMap(AttachmentHolder attachmentHolder, Supplier<AttachmentType<T>> attachmentType, Function<T, S> getter) {
        return get(attachmentHolder, attachmentType).map(getter);
    }

    static <T> T getOrDefault(AttachmentHolder attachmentHolder, Supplier<AttachmentType<T>> attachmentType, T defaultValue) {
        return get(attachmentHolder, attachmentType).orElse(defaultValue);
    }

    static <T, S> S getMapOrDefault(AttachmentHolder attachmentHolder, Supplier<AttachmentType<T>> attachmentType, Function<T, S> getter, S defaultValue) {
        return getMap(attachmentHolder, attachmentType, getter).orElse(defaultValue);
    }
}
