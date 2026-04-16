/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;

public class Schematics {
    public static final Codec<Schematics> CODEC = ResourceKey.codec(GalacticraftRegistries.Keys.SCHEMATIC).listOf().xmap(Schematics::new, schematics -> schematics.unlockedSchematics);
    public static final StreamCodec<ByteBuf, Schematics> STREAM_CODEC = ResourceKey.streamCodec(GalacticraftRegistries.Keys.SCHEMATIC).apply(ByteBufCodecs.list())
            .map(Schematics::new, schematics -> schematics.unlockedSchematics);
    private final List<ResourceKey<SchematicVariant>> unlockedSchematics;

    public static Schematics empty() {
        return new Schematics(new ArrayList<>());
    }

    public static Schematics copyOf(Schematics schematics) {
        List<ResourceKey<SchematicVariant>> copied = new ArrayList<>(schematics.unlockedCount());
        copied.addAll(schematics.unlockedSchematics);

        return new Schematics(copied);
    }

    public Schematics(List<ResourceKey<SchematicVariant>> unlockedSchematics) {
        this.unlockedSchematics = unlockedSchematics;
    }

    public boolean unlock(ResourceKey<SchematicVariant> key) {
        if (isUnlocked(key)) {
            return false;
        }

        return this.unlockedSchematics.add(key);
    }

    public void schematicUnlockedByTeam(ResourceKey<SchematicVariant> schematicId) {
        unlock(schematicId);
    }

    public void updateSpaceRaceTeam(SpaceRaceTeam spaceRaceTeam) {
        for (ResourceKey<SchematicVariant> schematicKey : this.unlockedSchematics) {
            spaceRaceTeam.getUnlockedSchematics().unlock(schematicKey);
        }
    }

    public boolean isUnlocked(ResourceKey<SchematicVariant> schematicId) {
        return this.unlockedSchematics.stream().anyMatch(id -> id.identifier().equals(schematicId.identifier()));
    }

    public int unlockedCount() {
        return this.unlockedSchematics.size();
    }

    public boolean isEmpty() {
        return this.unlockedSchematics.isEmpty();
    }
}
