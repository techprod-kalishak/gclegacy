package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.data.asset.GalacticraftLanguageProvider;
import io.kalishak.galacticraftlegacy.data.asset.GalacticraftModelProvider;
import io.kalishak.galacticraftlegacy.data.asset.GalacticraftSpritesProvider;
import io.kalishak.galacticraftlegacy.data.tag.GalacticraftBlockTagsProvider;
import io.kalishak.galacticraftlegacy.data.tag.GalacticraftEntityTypeTagsProvider;
import io.kalishak.galacticraftlegacy.data.tag.GalacticraftItemTagsProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

public class GalacticraftData {
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(GalacticraftSpritesProvider::new);
        event.createProvider(GalacticraftLanguageProvider::new);
        event.createProvider(GalacticraftModelProvider::new);

        event.createProvider(GalacticraftItemTagsProvider::new);
        event.createProvider(GalacticraftBlockTagsProvider::new);
        event.createProvider(GalacticraftEntityTypeTagsProvider::new);
        event.createProvider(GalacticraftRecipeProvider.Runner::new);
        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(GalacticraftBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider)
        );
    }
}
