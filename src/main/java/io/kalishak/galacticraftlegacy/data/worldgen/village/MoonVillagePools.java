package io.kalishak.galacticraftlegacy.data.worldgen.village;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.kalishak.galacticraftlegacy.data.worldgen.GalacticraftProcessorLists;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class MoonVillagePools {
    public static final ResourceKey<StructureTemplatePool> START = Constants.key(Registries.TEMPLATE_POOL, "village/moon/town_centers");
    public static final ResourceKey<StructureTemplatePool> STREET = Constants.key(Registries.TEMPLATE_POOL, "village/moon/streets");
    public static final ResourceKey<StructureTemplatePool> HOUSE = Constants.key(Registries.TEMPLATE_POOL, "village/moon/houses");
    public static final ResourceKey<StructureTemplatePool> MOON_VILLAGER = Constants.key(Registries.TEMPLATE_POOL, "village/moon/villagers");
    private static final ResourceKey<StructureTemplatePool> TERMINATORS_KEY = Constants.key(Registries.TEMPLATE_POOL, "village/moon/terminators");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        Holder<StructureTemplatePool> terminators = pools.getOrThrow(TERMINATORS_KEY);
        Holder<StructureProcessorList> streetMoon = processorLists.getOrThrow(GalacticraftProcessorLists.STREET_MOON);

        context.register(
                START,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/town_centers/moon_fountain_01"), 50)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                STREET,
                new StructureTemplatePool(
                        terminators,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/corner_01", streetMoon), 2),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/corner_02", streetMoon), 2),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/corner_03", streetMoon), 2),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/straight_01", streetMoon), 4),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/straight_02", streetMoon), 4),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/straight_03", streetMoon), 7),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/straight_04", streetMoon), 7),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/straight_05", streetMoon), 3),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/straight_06", streetMoon), 4),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/crossroad_01", streetMoon), 2),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/crossroad_02", streetMoon), 1),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/crossroad_03", streetMoon), 2),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/crossroad_04", streetMoon), 2),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/crossroad_05", streetMoon), 2),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/crossroad_06", streetMoon), 2),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/streets/turn_01", streetMoon), 3)
                        ),
                        StructureTemplatePool.Projection.TERRAIN_MATCHING
                )
        );
        context.register(
                HOUSE,
                new StructureTemplatePool(
                        terminators,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/houses/moon_dome"), 50),
                                Pair.of(StructurePoolElement.empty(), 10)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                TERMINATORS_KEY,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/terminators/terminator_01", streetMoon), 1)
                        ),
                        StructureTemplatePool.Projection.TERRAIN_MATCHING
                )
        );
        context.register(
                MOON_VILLAGER,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/villagers/nitwit"), 1),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/villagers/baby"), 1),
                                Pair.of(StructurePoolElement.legacy("galacticraftlegacy:village/moon/villagers/unemployed"), 10)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }
}
