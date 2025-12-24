package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftBlockTagsProvider extends BlockTagsProvider {
    public GalacticraftBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Blocks.MACHINE)
                .addTag(GalacticraftTags.Blocks.MACHINE_BASIC)
                .addOptionalTag(GalacticraftTags.Blocks.MACHINE_ADVANCED);
        tag(GalacticraftTags.Blocks.MACHINE_BASIC)
                .add(GalacticraftBlocks.COAL_GENERATOR.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(GalacticraftBlocks.COAL_GENERATOR.get())
                .add(GalacticraftBlocks.CIRCUIT_FABRICATOR.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(GalacticraftBlocks.COAL_GENERATOR.get())
                .add(GalacticraftBlocks.CIRCUIT_FABRICATOR.get());
    }
}
