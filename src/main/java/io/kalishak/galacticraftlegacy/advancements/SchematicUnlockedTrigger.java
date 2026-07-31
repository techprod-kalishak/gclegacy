/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.advancements.predicates.SchematicPredicate;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class SchematicUnlockedTrigger extends SimpleCriterionTrigger<SchematicUnlockedTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ResourceKey<SchematicVariant> schematicVariant) {
        trigger(player, instance -> instance.awardPlayer(player, schematicVariant));
    }

    public void trigger(ServerPlayer player) {
        trigger(player, instance -> instance.awardPlayer(player));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, SchematicPredicate schematicPredicate) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("playerName").forGetter(TriggerInstance::player),
                SchematicPredicate.CODEC.fieldOf("schematics").forGetter(TriggerInstance::schematicPredicate)
        ).apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> playerUnlockedSchematic(ResourceKey<SchematicVariant> schematicKey) {
            return GalacticraftCriteriaTriggers.UNLOCKED_SCHEMATIC.get().createCriterion(new TriggerInstance(Optional.empty(), SchematicPredicate.hasSingle(schematicKey)));
        }

        public boolean awardPlayer(ServerPlayer player, ResourceKey<SchematicVariant> schematicVariant) {
            return this.schematicPredicate.matches(player, schematicVariant);
        }

        public boolean awardPlayer(ServerPlayer player) {
            return this.schematicPredicate.matches(player, player.level(), Vec3.ZERO);
        }
    }
}
