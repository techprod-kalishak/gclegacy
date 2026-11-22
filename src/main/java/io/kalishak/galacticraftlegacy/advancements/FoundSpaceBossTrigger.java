package io.kalishak.galacticraftlegacy.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.entity.monster.boss.AbstractSpaceBoss;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class FoundSpaceBossTrigger extends SimpleCriterionTrigger<FoundSpaceBossTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, AbstractSpaceBoss boss) {
        trigger(player, triggerInstance -> triggerInstance.saw(player, boss));
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Holder<EntityType<?>> bossType) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("boss_type").forGetter(TriggerInstance::bossType)
        ).apply(i, TriggerInstance::new));

        public static Criterion<?> foundBoss(Holder<EntityType<?>> bossType) {
            return GalacticraftCriteriaTriggers.FIND_SPACE_BOSS.get().createCriterion(new TriggerInstance(Optional.empty(), bossType));
        }

        public boolean saw(Player player, AbstractSpaceBoss boss) {
            return this.bossType.is(boss.typeHolder().key()) && player.isWithinEntityInteractionRange(boss, 20.0D);
        }
    }
}
