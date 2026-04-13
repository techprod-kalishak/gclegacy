package io.kalishak.galacticraftlegacy.world.level.levelgen.placement;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public interface GalacticraftPlacements {
    static void bootstrap(BootstrapContext<PlacedFeature> cxt) {
        HolderGetter<ConfiguredFeature<?, ?>> featureGetter = cxt.lookup(Registries.CONFIGURED_FEATURE);

        AsteroidsPlacements.bootstrap(cxt, featureGetter);
        MarsPlacements.bootstrap(cxt, featureGetter);
        MoonPlacements.bootstrap(cxt, featureGetter);
        OverworldPlacements.bootstrap(cxt, featureGetter);
        VenusPlacements.bootstrap(cxt, featureGetter);
    }

    static ResourceKey<PlacedFeature> key(String name) {
        return Constants.key(Registries.PLACED_FEATURE, name);
    }

    static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }
}
