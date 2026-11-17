package io.kalishak.galacticraftlegacy.world.entity.npc.villager;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftVillagerTypes {
    private static final DeferredRegister<VillagerType> REGISTRY = DeferredRegister.create(Registries.VILLAGER_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<VillagerType, VillagerType> MOON = REGISTRY.register(
            "moon",
            VillagerType::new
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
