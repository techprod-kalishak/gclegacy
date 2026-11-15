package io.kalishak.galacticraftlegacy.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Set;

public record BurningLootCondition() implements LootItemCondition {
    public static final MapCodec<BurningLootCondition> MAP_CODEC = MapCodec.unit(BurningLootCondition::new);

    @Override
    public MapCodec<BurningLootCondition> codec() {
        return MAP_CODEC;
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity self = lootContext.getOptional(LootContextParams.THIS_ENTITY);

        return self != null && self.isOnFire();
    }

    public static LootItemCondition.Builder isBurning() {
        return BurningLootCondition::new;
    }
}
