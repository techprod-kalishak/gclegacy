package io.kalishak.galacticraftlegacy.data.worldgen;

import com.google.common.collect.ImmutableList;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

public class GalacticraftProcessorLists {
    public static final ResourceKey<StructureProcessorList> STREET_MOON = Constants.key(Registries.PROCESSOR_LIST, "street_moon");

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        context.register(STREET_MOON, new StructureProcessorList(
                ImmutableList.of(new RuleProcessor(
                        ImmutableList.of(
                                new ProcessorRule(new BlockMatchTest(GalacticraftBlocks.MOON_DIRT.get()), AlwaysTrueTest.INSTANCE, Blocks.SPRUCE_PLANKS.defaultBlockState())
                        )
                ))
        ));
    }
}
