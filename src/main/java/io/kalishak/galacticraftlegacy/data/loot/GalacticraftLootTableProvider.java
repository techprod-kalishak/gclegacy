/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GalacticraftLootTableProvider extends LootTableProvider {
    private static final List<SubProviderEntry> ENTRIES = List.of(
            new SubProviderEntry(GalacticraftBlockLootSubProvider::new, LootContextParamSets.BLOCK),
            new SubProviderEntry(GalacticraftEntityLootSubProvider::new, LootContextParamSets.ENTITY)
    );

    GalacticraftLootTableProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, requiredTables, subProviders, registries);
    }

    public static GalacticraftLootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new GalacticraftLootTableProvider(
                output,
                Set.of(),
                ENTRIES,
                registries
        );
    }
}
