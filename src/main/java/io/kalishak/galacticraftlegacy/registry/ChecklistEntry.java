/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.advancements.criterion.PredicateByteBufs;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.codec.HolderSetCodec;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public record ChecklistEntry(String id, Map<GearEquipmentSlot, ItemPredicate> requiredItems) {
    public static final Codec<ChecklistEntry> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(ChecklistEntry::id),
            Codec.unboundedMap(GearEquipmentSlot.CODEC, ItemPredicate.CODEC).fieldOf("required_items").forGetter(ChecklistEntry::requiredItems)
    ).apply(instance, ChecklistEntry::new));
    public static final Codec<Holder<ChecklistEntry>> CODEC = RegistryCodecs.holder(GalacticraftRegistries.Keys.CHECKLIST);
    public static final Codec<HolderSet<ChecklistEntry>> HOLDER_SET_CODEC = HolderSetCodec.create(GalacticraftRegistries.Keys.CHECKLIST, CODEC, true);
    public static final StreamCodec<RegistryFriendlyByteBuf, ChecklistEntry> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ChecklistEntry::id,
            ByteBufCodecs.map(HashMap::new, GearEquipmentSlot.STREAM_CODEC, PredicateByteBufs.ITEM_PREDICATE), ChecklistEntry::requiredItems,
            ChecklistEntry::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ChecklistEntry>> STREAM_CODEC = ByteBufCodecs.holder(GalacticraftRegistries.Keys.CHECKLIST, DIRECT_STREAM_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, HolderSet<ChecklistEntry>> HOLDER_SET_STREAM_CODEC = ByteBufCodecs.holderSet(GalacticraftRegistries.Keys.CHECKLIST);

    public String getDescriptionId(String modId) {
        return Identifier.fromNamespaceAndPath(modId, this.id).toLanguageKey("checklist_entry");
    }
}
