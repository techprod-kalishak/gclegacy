/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensionTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.concurrent.CompletableFuture;

public class GalacticraftDimensionTypeTags extends TagsProvider<DimensionType> {
    public GalacticraftDimensionTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.DIMENSION_TYPE, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(GalacticraftTags.DimensionTypes.GRAVITY_OVERRIDDEN)
                .add(GalacticraftDimensionTypes.MOON)
                .add(GalacticraftDimensionTypes.OVERWORLD_ORBIT)
                .add(GalacticraftDimensionTypes.MARS)
                .add(GalacticraftDimensionTypes.VENUS)
                .add(GalacticraftDimensionTypes.ASTEROIDS);
        tag(GalacticraftTags.DimensionTypes.HAS_DISABLED_ROCKETS)
                .add(BuiltinDimensionTypes.NETHER)
                .add(BuiltinDimensionTypes.END);
        tag(GalacticraftTags.DimensionTypes.HAS_METEORS)
                .add(GalacticraftDimensionTypes.MOON)
                .add(GalacticraftDimensionTypes.MARS);
        tag(GalacticraftTags.DimensionTypes.NEEDS_FREQUENCY_MODULE)
                .add(GalacticraftDimensionTypes.OVERWORLD_ORBIT)
                .add(GalacticraftDimensionTypes.MOON)
                .add(GalacticraftDimensionTypes.MARS)
                .add(GalacticraftDimensionTypes.ASTEROIDS)
                .add(GalacticraftDimensionTypes.VENUS);
        tag(GalacticraftTags.DimensionTypes.REQUIRES_CRYOCHAMBER)
                .add(GalacticraftDimensionTypes.OVERWORLD_ORBIT)
                .add(GalacticraftDimensionTypes.MOON)
                .add(GalacticraftDimensionTypes.MARS)
                .add(GalacticraftDimensionTypes.VENUS)
                .add(GalacticraftDimensionTypes.ASTEROIDS);
        tag(GalacticraftTags.DimensionTypes.SPACE_MOB_HABITABLE)
                .add(GalacticraftDimensionTypes.MOON)
                .add(GalacticraftDimensionTypes.MARS);
    }
}
