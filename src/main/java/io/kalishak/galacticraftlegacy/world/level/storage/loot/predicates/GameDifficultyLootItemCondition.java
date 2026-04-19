/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.config.ServerConfig;
import io.kalishak.galacticraftlegacy.config.values.GameDifficulty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record GameDifficultyLootItemCondition(GameDifficulty gameDifficulty) implements LootItemCondition {
    public static final MapCodec<GameDifficultyLootItemCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            GameDifficulty.CODEC.fieldOf("target_difficulty").forGetter(GameDifficultyLootItemCondition::gameDifficulty)
    ).apply(instance, GameDifficultyLootItemCondition::new));
    @Override
    public MapCodec<GameDifficultyLootItemCondition> codec() {
        return MAP_CODEC;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return ServerConfig.GAME_DIFFICULTY.get() == gameDifficulty();
    }
}
