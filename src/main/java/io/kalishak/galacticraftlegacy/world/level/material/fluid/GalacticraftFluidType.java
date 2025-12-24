package io.kalishak.galacticraftlegacy.world.level.material.fluid;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GalacticraftFluidType {
    private static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Galacticraft.MODID);

    public static final DeferredHolder<FluidType, FluidType> OXYGEN = REGISTRY.register(
            "oxygen",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.galacticraftlegacy.oxygen"))
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
