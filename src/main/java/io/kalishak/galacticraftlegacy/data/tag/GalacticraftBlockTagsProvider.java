package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftBlockTagsProvider extends BlockTagsProvider {
    public GalacticraftBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Blocks.BREATHABLE_AIR)
                .add(Blocks.AIR)
                .add(Blocks.CAVE_AIR)
                .add(GalacticraftBlocks.OXYGEN_AIR.get());
        tag(GalacticraftTags.Blocks.MACHINE)
                .addTag(GalacticraftTags.Blocks.MACHINE_BASIC)
                .addTag(GalacticraftTags.Blocks.MACHINE_ADVANCED);
        tag(GalacticraftTags.Blocks.MACHINE_BASIC)
                .add(GalacticraftBlocks.COAL_GENERATOR.get());
        tag(GalacticraftTags.Blocks.MACHINE_ADVANCED)
                .add(GalacticraftBlocks.ELECTRIC_FURNACE.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(GalacticraftBlocks.COAL_GENERATOR.get())
                .add(GalacticraftBlocks.CIRCUIT_FABRICATOR.get())
                .add(GalacticraftBlocks.ELECTRIC_FURNACE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(GalacticraftBlocks.COAL_GENERATOR.get())
                .add(GalacticraftBlocks.CIRCUIT_FABRICATOR.get())
                .add(GalacticraftBlocks.ELECTRIC_FURNACE.get());

        tag(GalacticraftTags.Blocks.SEALABLE)
                .addTag(Tags.Blocks.GLASS_PANES);
        tag(GalacticraftTags.Blocks.SENSOR_GLASSES_DETECTABLE)
                .addTag(Tags.Blocks.ORES)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_IRON);
    }
}
