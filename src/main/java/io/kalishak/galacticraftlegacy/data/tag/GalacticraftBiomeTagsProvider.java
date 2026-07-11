/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.biome.MoonBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class GalacticraftBiomeTagsProvider extends TagsProvider<Biome> {
    public GalacticraftBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Registries.BIOME, provider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Biomes.HAS_BASIC_FEATURES)
                .addTag(GalacticraftTags.Biomes.IS_MOON);
        tag(GalacticraftTags.Biomes.HAS_ADVANCED_FEATURES)
                .addOptionalTag(GalacticraftTags.Biomes.IS_MARS);
        tag(GalacticraftTags.Biomes.HAS_ULTIMATE_FEATURES)
                .addOptionalTag(GalacticraftTags.Biomes.IS_ASTEROIDS)
                .addOptionalTag(GalacticraftTags.Biomes.IS_VENUS);
        tag(GalacticraftTags.Biomes.IS_MOON)
                .add(MoonBiomes.MOON_PLAINS);
    }
}
