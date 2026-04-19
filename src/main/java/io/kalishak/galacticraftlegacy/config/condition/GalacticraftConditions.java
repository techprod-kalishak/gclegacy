/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config.condition;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.config.values.GameDifficulty;
import net.minecraft.world.level.chunk.Configuration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GalacticraftConditions {
    private static final DeferredRegister<MapCodec<? extends ICondition>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, Galacticraft.MODID);

    public static ICondition withQuickMode() {
        return new GameDifficultyConfigCondition(GameDifficulty.QUICK);
    }

    public static ICondition withHardMode() {
        return new GameDifficultyConfigCondition(GameDifficulty.HARD);
    }

    public static ICondition withAdventureMode() {
        return new GameDifficultyConfigCondition(GameDifficulty.ADVENTURE);
    }

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<GameDifficultyConfigCondition>> GAME_DIFFICULTY = REGISTRY.register(
            "game_difficulty",
            () -> GameDifficultyConfigCondition.MAP_CODEC
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
