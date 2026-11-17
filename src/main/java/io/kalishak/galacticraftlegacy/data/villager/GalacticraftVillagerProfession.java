package io.kalishak.galacticraftlegacy.data.villager;

import com.google.common.collect.ImmutableSet;
import io.kalishak.galacticraftlegacy.Galacticraft;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftVillagerProfession {
    private static final DeferredRegister<VillagerProfession> REGISTRY = DeferredRegister.create(Registries.VILLAGER_PROFESSION, Galacticraft.MODID);

    public static final DeferredHolder<VillagerProfession, VillagerProfession> MOON = REGISTRY.register(
            "moon",
            () -> new VillagerProfession(
                    Component.translatable("entity.galacticraftlegacy.villager.moon"),
                    poiType -> poiType.is(GalacticraftPoiTypes.MOONLLAGER.key()),
                    poiType -> poiType.is(GalacticraftPoiTypes.MOONLLAGER.key()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    null,
                    Int2ObjectMap.ofEntries(
                            Int2ObjectMap.entry(1, GalacticraftTradeSets.MOON_LEVEL_1),
                            Int2ObjectMap.entry(2, GalacticraftTradeSets.MOON_LEVEL_2),
                            Int2ObjectMap.entry(3, GalacticraftTradeSets.MOON_LEVEL_3),
                            Int2ObjectMap.entry(4, GalacticraftTradeSets.MOON_LEVEL_4),
                            Int2ObjectMap.entry(5, GalacticraftTradeSets.MOON_LEVEL_5)
                    )
            )
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
