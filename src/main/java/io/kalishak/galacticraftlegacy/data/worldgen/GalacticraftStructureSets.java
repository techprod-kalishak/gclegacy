package io.kalishak.galacticraftlegacy.data.worldgen;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

public class GalacticraftStructureSets {
    public static final ResourceKey<StructureSet> MOON_VILLAGES = Constants.key(Registries.STRUCTURE_SET, "moon_villages");

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(
                MOON_VILLAGES,
                new StructureSet(
                        List.of(
                                StructureSet.entry(structures.getOrThrow(GalacticraftStructures.VILLAGE_MOON))
                        ),
                        new RandomSpreadStructurePlacement(54, 12, RandomSpreadType.LINEAR, 10387312)
                )
        );
    }
}
