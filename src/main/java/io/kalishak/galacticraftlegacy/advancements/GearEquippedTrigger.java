/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GearEquippedTrigger extends SimpleCriterionTrigger<GearEquippedTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ResourceHandler<ItemResource> gear, ItemStack stack) {
        int full = 0, empty = 0, occupied = 0;

        for (int i = 0; i < gear.size(); i++) {
            ItemResource inGear = gear.getResource(i);

            if (inGear.isEmpty()) {
                empty++;
            } else {
                occupied++;

                if (gear.getAmountAsInt(i) >= gear.getCapacityAsInt(i, inGear)) {
                    full++;
                }
            }
        }

        trigger(player, gear, stack, full, empty, occupied);
    }

    private void trigger(ServerPlayer serverPlayer, ResourceHandler<ItemResource> gear, ItemStack stack, int full, int empty, int occupied) {
        this.trigger(serverPlayer, instance -> instance.matches(gear, stack, full, empty, occupied));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, GearSlots slots, List<ItemPredicate> items) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                GearSlots.CODEC.optionalFieldOf("slots", GearSlots.ANY).forGetter(TriggerInstance::slots),
                ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(TriggerInstance::items)
        ).apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> hasItems(ItemPredicate.Builder... items) {
            return hasItems(Stream.of(items).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
        }

        public static Criterion<TriggerInstance> hasItems(ItemLike... items) {
            ItemPredicate[] predicates = new ItemPredicate[items.length];

            for (int i = 0; i < items.length; i++) {
                predicates[i] = new ItemPredicate(Optional.of(HolderSet.direct(new ItemStack(items[i]).typeHolder())), MinMaxBounds.Ints.ANY, DataComponentMatchers.ANY);
            }

            return hasItems(predicates);
        }

        public static Criterion<TriggerInstance> hasItems(ItemPredicate... items) {
            return GalacticraftCriteriaTriggers.GEAR_EQUIPPED.get().createCriterion(new TriggerInstance(Optional.empty(), GearSlots.ANY, Arrays.asList(items)));
        }

        public boolean matches(ResourceHandler<ItemResource> gear, ItemStack stack, int full, int empty, int occupied) {
            if (!this.slots.matches(full, empty, occupied)) {
                return false;
            } else if (this.items.isEmpty()) {
                return true;
            } else if (this.items.size() != 1) {
                List<ItemPredicate> predicates = new ObjectArrayList<>(this.items);
                int size = gear.size();

                for (int i = 0; i < size; i++) {
                    if (predicates.isEmpty()) {
                        return true;
                    }

                    ItemStack inGear = ItemUtil.getStack(gear, i);

                    if (!inGear.isEmpty()) {
                        predicates.removeIf(tester -> tester.test(inGear));
                    }
                }

                return predicates.isEmpty();
            }

            return !stack.isEmpty() && this.items.getFirst().test(stack);
        }
    }

    public record GearSlots(MinMaxBounds.Ints occupied, MinMaxBounds.Ints full, MinMaxBounds.Ints empty) {
        public static final Codec<GearSlots> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                MinMaxBounds.Ints.CODEC.optionalFieldOf("occupied", MinMaxBounds.Ints.ANY).forGetter(GearSlots::occupied),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("full", MinMaxBounds.Ints.ANY).forGetter(GearSlots::full),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("empty", MinMaxBounds.Ints.ANY).forGetter(GearSlots::empty)
        ).apply(instance, GearSlots::new));

        public static final GearSlots ANY = new GearSlots(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY);

        public boolean matches(int full, int empty, int occupied) {
            return this.full.matches(full) && this.empty.matches(empty) && this.occupied.matches(occupied);
        }
    }
}
