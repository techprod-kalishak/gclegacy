/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public record SchematicContent(Holder<SchematicVariant> schematic) {
    public static final MapCodec<SchematicContent> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SchematicVariant.CODEC.fieldOf("schematic_variant").forGetter(SchematicContent::schematic)
    ).apply(instance, SchematicContent::new));
    public static final Codec<SchematicContent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, SchematicContent> STREAM_CODEC = StreamCodec.composite(
            SchematicVariant.STREAM_CODEC, SchematicContent::schematic, SchematicContent::new
    );
    public static SchematicContent getDefault(IAttachmentHolder attachmentHolder) {
        if (attachmentHolder instanceof Entity entity) {
            return new SchematicContent(entity.registryAccess().getOrThrow(SchematicVariants.TIER_2_ROCKET));
        }

        throw new IllegalArgumentException(attachmentHolder.getClass() + " should not have schematic data!");
    }
}
