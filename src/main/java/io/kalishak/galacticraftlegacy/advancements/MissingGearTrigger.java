/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Optional;

public class MissingGearTrigger extends SimpleCriterionTrigger<MissingGearTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void award(ServerPlayer player) {
        trigger(player, i -> i.matches(player));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, List<GearEquipmentSlot> slots, LocationPredicate location) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                GearEquipmentSlot.CODEC.listOf().optionalFieldOf("slots", List.of()).forGetter(TriggerInstance::slots),
                LocationPredicate.CODEC.fieldOf("location").forGetter(TriggerInstance::location)
        ).apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> cannotHear(HolderLookup<Biome> biomes) {
            return GalacticraftCriteriaTriggers.MISSING_GEAR.get().createCriterion(new TriggerInstance(
                    Optional.empty(),
                    List.of(GearEquipmentSlot.FREQUENCY_MODULE),
                    LocationPredicate.Builder.location()
                            .setBiomes(biomes.getOrThrow(GalacticraftTags.Biomes.IS_ORBIT))
                            .build()
                    )
            );
        }

        public static Criterion<TriggerInstance> noParachute() {
            return GalacticraftCriteriaTriggers.MISSING_GEAR.get().createCriterion(new TriggerInstance(
                            Optional.empty(),
                            List.of(GearEquipmentSlot.PARACHUTE),
                            LocationPredicate.Builder.location()
                                    .setY(MinMaxBounds.Doubles.atLeast(250.0D))
                                    .build()
                    )
            );
        }

        public boolean matches(ServerPlayer player) {
            if (this.location.matches(player.level(), player.getX(), player.getY(), player.getZ())) {
                SpaceGearEquipment equipment = AttachmentHelper.getGearInventory(player).getGearEquipment();

                return this.slots.stream().allMatch(slot -> equipment.get(slot).isEmpty());
            }

            return false;
        }
    }
}
