/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.telemetry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.UniquelyIdentifyable;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public class GloballyReferencedEntity<StoredEntityType extends UniquelyIdentifyable> {
    public static Codec<GloballyReferencedEntity<UniquelyIdentifyable>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityReference.codec().fieldOf("reference").forGetter(GloballyReferencedEntity::getReference),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(GloballyReferencedEntity::getKnownDimension),
            BlockPos.CODEC.fieldOf("position").forGetter(GloballyReferencedEntity::getLastKnownPosition)
    ).apply(instance, GloballyReferencedEntity::new));
    public static final Codec<Set<GloballyReferencedEntity<UniquelyIdentifyable>>> CODEC_SET = Codec.list(CODEC).xmap(Sets::newHashSet, Lists::newArrayList);
    protected final EntityReference<StoredEntityType> entityReference;
    protected ResourceKey<Level> knownDimension;
    protected BlockPos lastKnownPosition;

    public GloballyReferencedEntity(EntityReference<StoredEntityType> entityReference, ResourceKey<Level> knownDimension, BlockPos lastKnownPosition) {
        this.entityReference = entityReference;
        this.knownDimension = knownDimension;
        this.lastKnownPosition = lastKnownPosition;
    }

    public GlobalPos getGlobalPosition() {
        return GlobalPos.of(this.knownDimension, this.lastKnownPosition);
    }

    public EntityReference<StoredEntityType> getReference() {
        return this.entityReference;
    }

    public ResourceKey<Level> getKnownDimension() {
        return this.knownDimension;
    }

    public BlockPos getLastKnownPosition() {
        return this.lastKnownPosition;
    }

    public @Nullable StoredEntityType getEntity(MinecraftServer server, Class<StoredEntityType> clazz) {
        ServerLevel level = server.getLevel(this.knownDimension);

        return level == null ? null : this.entityReference.getEntity(level::getEntity, clazz);
    }
}
