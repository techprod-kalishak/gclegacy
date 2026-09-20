/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.loot;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class GalacticraftLootTableProvider extends LootTableProvider {
    GalacticraftLootTableProvider(Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders) {
        super(requiredTables, subProviders);
    }

    public static GalacticraftLootTableProvider create() {
        return new GalacticraftLootTableProvider(
                Set.of(),
                List.of(
                        new SubProviderEntry(GalacticraftBlockLootSubProvider::new, LootContextParamSets.BLOCK),
                        new SubProviderEntry(GalacticraftChestLootSubProvider::new, LootContextParamSets.CHEST),
                        new SubProviderEntry(GalacticraftEntityLootSubProvider::new, LootContextParamSets.ENTITY)
                )
        );
    }
}
