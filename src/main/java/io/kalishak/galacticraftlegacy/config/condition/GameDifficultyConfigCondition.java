/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.config.ServerConfig;
import io.kalishak.galacticraftlegacy.config.values.GameDifficulty;

public class GameDifficultyConfigCondition extends ConfigCondition<GameDifficulty> {
    public static final MapCodec<GameDifficultyConfigCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            GameDifficulty.CODEC.fieldOf("target_difficulty").forGetter(condition -> condition.targetValue)
    ).apply(instance, GameDifficultyConfigCondition::new));

    public GameDifficultyConfigCondition(GameDifficulty targetDifficulty) {
        super(ServerConfig.GAME_DIFFICULTY, targetDifficulty);
    }

    @Override
    public MapCodec<GameDifficultyConfigCondition> codec() {
        return MAP_CODEC;
    }
}
