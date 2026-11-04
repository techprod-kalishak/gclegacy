/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.galaxies.celestial.Celestial;
import io.kalishak.galacticraftlegacy.registry.BuiltinGalaxies;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class GalacticraftCelestialTagsProvider extends TagsProvider<Celestial> {
    public GalacticraftCelestialTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, GalacticraftRegistries.Keys.CELESTIAL, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(GalacticraftTags.Celestials.MOONS)
                .add(BuiltinGalaxies.MOON);
        tag(GalacticraftTags.Celestials.PLANETS)
                .add(BuiltinGalaxies.MERCURY)
                .add(BuiltinGalaxies.VENUS)
                .add(BuiltinGalaxies.EARTH)
                .add(BuiltinGalaxies.MARS)
                .add(BuiltinGalaxies.ASTEROIDS)
                .add(BuiltinGalaxies.JUPITER)
                .add(BuiltinGalaxies.SATURN)
                .add(BuiltinGalaxies.URANUS)
                .add(BuiltinGalaxies.NEPTUNE);
        tag(GalacticraftTags.Celestials.SOLAR_SYSTEMS)
                .add(BuiltinGalaxies.SOL);
        tag(GalacticraftTags.Celestials.STARS)
                .add(BuiltinGalaxies.SUN);
    }
}
