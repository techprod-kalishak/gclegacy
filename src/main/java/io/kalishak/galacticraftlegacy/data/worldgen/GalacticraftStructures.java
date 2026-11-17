package io.kalishak.galacticraftlegacy.data.worldgen;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.data.worldgen.village.MoonVillagePools;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

public class GalacticraftStructures {
    public static final ResourceKey<Structure> VILLAGE_MOON = Constants.key(Registries.STRUCTURE, "village_moon");

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);

        context.register(
                VILLAGE_MOON,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(GalacticraftTags.Biomes.HAS_VILLAGE_MOON)).terrainAdapation(TerrainAdjustment.BEARD_THIN).build(),
                        templates.getOrThrow(MoonVillagePools.START),
                        6,
                        ConstantHeight.of(VerticalAnchor.absolute(0)),
                        true,
                        Heightmap.Types.OCEAN_FLOOR_WG
                )
        );
    }
}
