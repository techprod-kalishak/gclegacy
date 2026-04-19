/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.config.values.GameDifficulty;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftLootItemConditions {
    private static final DeferredRegister<MapCodec<? extends LootItemCondition>> REGISTRY = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Galacticraft.MODID);

    public static LootItemCondition withQuickMode() {
        return new GameDifficultyLootItemCondition(GameDifficulty.QUICK);
    }

    public static LootItemCondition withHardMode() {
        return new GameDifficultyLootItemCondition(GameDifficulty.HARD);
    }

    public static LootItemCondition withAdventureMode() {
        return new GameDifficultyLootItemCondition(GameDifficulty.ADVENTURE);
    }

    public static final DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<GameDifficultyLootItemCondition>> GAME_DIFFICULTY = REGISTRY.register(
            "game_difficulty",
            () -> GameDifficultyLootItemCondition.MAP_CODEC
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
