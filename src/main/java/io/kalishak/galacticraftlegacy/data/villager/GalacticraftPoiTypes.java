package io.kalishak.galacticraftlegacy.data.villager;

import com.google.common.collect.ImmutableSet;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftPoiTypes {
    private static final DeferredRegister<PoiType> REGISTRY = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<PoiType, PoiType> MOONLLAGER = REGISTRY.register(
            "moon",
            () -> new PoiType(
                    ImmutableSet.copyOf(GalacticraftBlocks.TIN_DECORATION_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1,
                    1
            )
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
