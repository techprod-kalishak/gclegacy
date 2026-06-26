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
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.concurrent.CompletableFuture;

public class GalacticraftDimensionTypeTags extends KeyTagProvider<DimensionType> {
    public GalacticraftDimensionTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.DIMENSION_TYPE, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(GalacticraftTags.DimensionTypes.HAS_DISABLED_ROCKETS)
                .add(BuiltinDimensionTypes.NETHER)
                .add(BuiltinDimensionTypes.END);
        tag(GalacticraftTags.DimensionTypes.OPEN_SPACE)
                .add(GalacticraftDimensionTypes.OVERWORLD_ORBIT)
                .add(GalacticraftDimensionTypes.ASTEROIDS);
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
