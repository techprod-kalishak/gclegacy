package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class GalacticraftBlockLootSubProvider extends BlockLootSubProvider {
    private static final Set<Item> EXPLOSION_RESISTANT = Set.of();

    protected GalacticraftBlockLootSubProvider(HolderLookup.Provider registries) {
        super(EXPLOSION_RESISTANT, FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected void generate() {
        add(GalacticraftBlocks.COAL_GENERATOR.get(), this::createNameableBlockEntityTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GalacticraftBlocks.getEntries().toList();
    }
}
