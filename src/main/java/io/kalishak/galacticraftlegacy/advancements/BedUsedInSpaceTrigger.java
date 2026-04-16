/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.Optional;

public class BedUsedInSpaceTrigger extends SimpleCriterionTrigger<BedUsedInSpaceTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer) {
        trigger(serverPlayer, triggerInstance -> triggerInstance.awardPlayer(serverPlayer));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Holder<DimensionType> level, BlockPos bedPosition) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("playerName").forGetter(TriggerInstance::player),
                DimensionType.CODEC.fieldOf("level").forGetter(TriggerInstance::level),
                BlockPos.CODEC.fieldOf("bed_position").forGetter(TriggerInstance::bedPosition)
        ).apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> triedToLayInBed(Level level, BlockPos bedPosition) {
            return GalacticraftCriteriaTriggers.BED_USED_IN_SPACE.get().createCriterion(new TriggerInstance(Optional.empty(), level.dimensionTypeRegistration(), bedPosition));
        }

        public static Criterion<TriggerInstance> triedToLayInBed(Player player, BlockPos bedPosition) {
            return triedToLayInBed(player.level(), bedPosition);
        }

        public boolean awardPlayer(Player player) {
            if (this.level.is(GalacticraftTags.DimensionTypes.REQUIRES_CRYOCHAMBER)) {
                BlockState state = player.level().getBlockState(this.bedPosition);

                return state.is(BlockTags.BEDS);
            }

            return false;
        }
    }
}
