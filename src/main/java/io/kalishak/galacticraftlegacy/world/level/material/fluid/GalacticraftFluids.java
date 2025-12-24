package io.kalishak.galacticraftlegacy.world.level.material.fluid;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftFluids {
    private static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(Registries.FLUID, Galacticraft.MODID);

    public static final DeferredHolder<Fluid, GaseousFluid.Still> OXYGEN = DeferredHolder.create(Galacticraft.key(Registries.FLUID, "oxygen"));
    public static final DeferredHolder<Fluid, GaseousFluid.Flowing> OXYGEN_FLOWING = DeferredHolder.create(Galacticraft.key(Registries.FLUID, "oxygen_flowing"));

    public static void init(IEventBus bus) {
        BaseFlowingFluid.Properties OXYGEN_PROPERTIES = new BaseFlowingFluid.Properties(
                GalacticraftFluidType.OXYGEN,
                GalacticraftFluids.OXYGEN,
                GalacticraftFluids.OXYGEN_FLOWING
        );
        REGISTRY.register("oxygen", () -> new GaseousFluid.Still(OXYGEN_PROPERTIES));
        REGISTRY.register("oxygen_flowing", () -> new GaseousFluid.Still(OXYGEN_PROPERTIES));

        REGISTRY.register(bus);
    }
}
