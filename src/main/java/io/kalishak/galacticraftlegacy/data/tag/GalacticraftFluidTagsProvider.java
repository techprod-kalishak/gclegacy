/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.GalacticraftFluidIds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;

import java.util.concurrent.CompletableFuture;

public class GalacticraftFluidTagsProvider extends FluidTagsProvider {
    public GalacticraftFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Fluids.FLOWS_GRATING)
                .addTag(FluidTags.WATER)
                .addTag(FluidTags.LAVA);
        tag(GalacticraftTags.Fluids.IS_OXYGEN)
                .add(GalacticraftFluidIds.OXYGEN)
                .add(GalacticraftFluidIds.FLOWING_OXYGEN);
        tag(GalacticraftTags.Fluids.IS_OIL)
                .add(GalacticraftFluidIds.OIL)
                .add(GalacticraftFluidIds.FLOWING_OIL);
        tag(GalacticraftTags.Fluids.IS_FUEL)
                .add(GalacticraftFluidIds.FUEL)
                .add(GalacticraftFluidIds.FLOWING_FUEL);
        tag(GalacticraftTags.Fluids.FLAMMABLE_LIQUID)
                .addTag(GalacticraftTags.Fluids.IS_OIL)
                .addTag(GalacticraftTags.Fluids.IS_FUEL);
    }
}
