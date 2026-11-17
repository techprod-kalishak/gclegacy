package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.data.villager.GalacticraftTrades;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VillagerTradesTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftTradeTagProvider extends VillagerTradesTagsProvider {
    public GalacticraftTradeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(GalacticraftTags.VillagerTrades.MOON_LEVEL_1)
                .add(GalacticraftTrades.MOONLLAGER_1_OXYGEN_MASK_LUNAR)
                .add(GalacticraftTrades.MOONLLAGER_1_LIGHT_TANK_LUNAR)
                .add(GalacticraftTrades.MOONLLAGER_1_OXYGEN_GEAR_LUNAR);
        tag(GalacticraftTags.VillagerTrades.MOON_LEVEL_2)
                .add(GalacticraftTrades.MOONLLAGER_2_FUEL_CANISTER_LUNAR)
                .add(GalacticraftTrades.MOONLLAGER_2_PARACHUTE_LUNAR)
                .add(GalacticraftTrades.MOONLLAGER_2_BATTERY_LUNAR);
        tag(GalacticraftTags.VillagerTrades.MOON_LEVEL_3)
                .add(GalacticraftTrades.MOONLLAGER_3_CANISTER_CARROT)
                .add(GalacticraftTrades.MOONLLAGER_3_BASIC_WAFER_LUNAR)
                .add(GalacticraftTrades.MOONLLAGER_3_EXCHANGE_BUGGY_SCHEMATIC)
                .add(GalacticraftTrades.MOONLLAGER_3_EXCHANGE_T2_SCHEMATIC);
        tag(GalacticraftTags.VillagerTrades.MOON_LEVEL_4)
                .add(GalacticraftTrades.MOONLLAGER_4_COMPRESSED_TIN_LUNAR)
                .add(GalacticraftTrades.MOONLLAGER_4_COMPRESSED_COPPER_LUNAR);
        tag(GalacticraftTags.VillagerTrades.MOON_LEVEL_5)
                .add(GalacticraftTrades.MOONLLAGER_5_JUNGLE_TREE_LUNAR);
    }
}
