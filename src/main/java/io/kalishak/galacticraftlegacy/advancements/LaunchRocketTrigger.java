/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class LaunchRocketTrigger extends SimpleCriterionTrigger<LaunchRocketTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        trigger(player, triggerInstance -> triggerInstance.rocket.matches(player, player.getVehicle()));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, EntityPredicate rocket) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                EntityPredicate.CODEC.fieldOf("rocket").forGetter(TriggerInstance::rocket)
        ).apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> sitsIn(EntityPredicate.Builder rocket) {
            return GalacticraftCriteriaTriggers.LAUNCH_ROCKET.get().createCriterion(new TriggerInstance(Optional.empty(), rocket.build()));
        }
    }
}
