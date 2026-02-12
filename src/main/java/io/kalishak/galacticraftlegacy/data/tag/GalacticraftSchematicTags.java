package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftSchematicTags extends KeyTagProvider<SchematicVariant> {

    public GalacticraftSchematicTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, GalacticraftRegistries.Keys.SCHEMATIC, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.PLACEABLE_SCHEMATICS)
                .add(SchematicVariants.ASTRO_MINER)
                .add(SchematicVariants.CARGO_ROCKET)
                .add(SchematicVariants.MOON_BUGGY)
                .add(SchematicVariants.TIER_2_ROCKET)
                .add(SchematicVariants.TIER_3_ROCKET);
    }
}
