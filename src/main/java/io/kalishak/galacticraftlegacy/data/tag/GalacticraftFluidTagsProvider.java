package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftFluidTagsProvider extends FluidTagsProvider {
    public GalacticraftFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Fluids.IS_OXYGEN)
                .add(GalacticraftFluids.OXYGEN.get())
                .add(GalacticraftFluids.OXYGEN_FLOWING.get());
        tag(GalacticraftTags.Fluids.IS_OIL)
                .add(GalacticraftFluids.OIL.get())
                .add(GalacticraftFluids.OIL_FLOWING.get());
        tag(GalacticraftTags.Fluids.IS_FUEL)
                .add(GalacticraftFluids.FUEL.get())
                .add(GalacticraftFluids.FUEL_FLOWING.get());
    }
}
