package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftBiomeTagsProvider extends BiomeTagsProvider {
    public GalacticraftBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Biomes.HAS_BASIC_FEATURES)
                .addOptionalTag(GalacticraftTags.Biomes.IS_MOON);
        tag(GalacticraftTags.Biomes.HAS_ADVANCED_FEATURES)
                .addOptionalTag(GalacticraftTags.Biomes.IS_MARS);
        tag(GalacticraftTags.Biomes.HAS_ULTIMATE_FEATURES)
                .addOptionalTag(GalacticraftTags.Biomes.IS_ASTEROIDS)
                .addOptionalTag(GalacticraftTags.Biomes.IS_VENUS);
    }
}
