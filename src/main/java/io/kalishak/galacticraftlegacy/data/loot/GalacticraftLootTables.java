package io.kalishak.galacticraftlegacy.data.loot;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class GalacticraftLootTables {
    // Chests
    public static final ResourceKey<LootTable> CRASHED_PROBE = key("chests/crashed_probe");
    public static final ResourceKey<LootTable> MOON_DUNGEON = key("chests/moon_dungeon");
    public static final ResourceKey<LootTable> MARS_DUNGEON = key("chests/mars_dungeon");
    public static final ResourceKey<LootTable> VENUS_DUNGEON = key("chests/venus_dungeon");

    private static ResourceKey<LootTable> key(String name) {
        return Constants.key(Registries.LOOT_TABLE, name);
    }
}
