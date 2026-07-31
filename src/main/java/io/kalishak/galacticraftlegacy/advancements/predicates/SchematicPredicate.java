/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.advancements.predicates;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record SchematicPredicate(List<ResourceKey<SchematicVariant>> unlockedSchematics) implements EntitySubPredicate {
    public static final Codec<SchematicPredicate> CODEC = ResourceKey.codec(GalacticraftRegistries.Keys.SCHEMATIC)
            .listOf()
            .xmap(SchematicPredicate::new, SchematicPredicate::unlockedSchematics);

    @Override
    public boolean matches(Entity entity, ServerLevel serverLevel, @Nullable Vec3 vec3) {
        if (!(entity instanceof Player)) {
            return false;
        }

        Schematics schematics = entity.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

        return this.unlockedSchematics.stream().allMatch(schematics::isUnlocked);
    }

    public boolean matches(Player player, ResourceKey<SchematicVariant> schematicVariant) {
        Schematics schematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

        return schematics.isUnlocked(schematicVariant);
    }

    public static SchematicPredicate hasSingle(ResourceKey<SchematicVariant> schematic) {
        return Builder.builder().haveSchematic(schematic).build();
    }

    public static class Builder {
        private final List<ResourceKey<SchematicVariant>> unlockedSchematics = new ArrayList<>();

        public static Builder builder() {
            return new Builder();
        }

        public Builder haveSchematic(ResourceKey<SchematicVariant> schematic) {
            this.unlockedSchematics.add(schematic);

            return this;
        }

        public Builder haveSchematic(Holder<SchematicVariant> schematic) {
            return haveSchematic(schematic.getKey());
        }

        public SchematicPredicate build() {
            return new SchematicPredicate(this.unlockedSchematics);
        }
    }
}
