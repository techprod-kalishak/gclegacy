package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class GalacticraftTags {
    private static <R> TagKey<R> tagKey(ResourceKey<? extends Registry<R>> registryKey, String tagKey) {
        return TagKey.create(registryKey, Galacticraft.id(tagKey));
    }

    public static class Blocks {
        public static final TagKey<Block> MACHINE = tagKey(Registries.BLOCK, "machine");
        public static final TagKey<Block> MACHINE_BASIC = tagKey(Registries.BLOCK, "machine/basic");
        public static final TagKey<Block> MACHINE_ADVANCED = tagKey(Registries.BLOCK, "machine/advanced");
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> CAN_EQUIP_PARACHUTE = tagKey(Registries.ENTITY_TYPE, "can_equip_parachute");
    }

    public static class Items {
        public static final TagKey<Item> PARACHUTE = tagKey(Registries.ITEM, "parachute");
        public static final TagKey<Item> WRENCH = tagKey(Registries.ITEM, "tools/wrench");
    }
}
