/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.material.fluid;

import com.google.common.base.Suppliers;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GalacticraftFluids {
    private static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(Registries.FLUID, Galacticraft.MODID);
    private static final Supplier<BaseFlowingFluid.Properties> OXYGEN_PROPERTIES = Suppliers.memoize(() -> new BaseFlowingFluid.Properties(
            GalacticraftFluidType.OXYGEN::value,
            GalacticraftFluids.OXYGEN::value,
            GalacticraftFluids.OXYGEN_FLOWING::value
    ));
    private static final Supplier<BaseFlowingFluid.Properties> OIL_PROPERTIES = Suppliers.memoize(() -> new BaseFlowingFluid.Properties(
            GalacticraftFluidType.OIL::value,
            GalacticraftFluids.OIL::value,
            GalacticraftFluids.OIL_FLOWING::value
    ).block(GalacticraftBlocks.OIL::value).bucket(GalacticraftItems.OIL_BUCKET::value));
    private static final Supplier<BaseFlowingFluid.Properties> FUEL_PROPERTIES = Suppliers.memoize(() -> new BaseFlowingFluid.Properties(
            GalacticraftFluidType.FUEL::value,
            GalacticraftFluids.FUEL::value,
            GalacticraftFluids.FUEL_FLOWING::value
    ).block(GalacticraftBlocks.FUEL::value).bucket(GalacticraftItems.FUEL_BUCKET::value));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> OXYGEN = REGISTRY.register("oxygen", () -> new BaseFlowingFluid.Source(OXYGEN_PROPERTIES.get()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> OXYGEN_FLOWING = REGISTRY.register("oxygen_flowing", () -> new BaseFlowingFluid.Flowing(OXYGEN_PROPERTIES.get()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> OIL = REGISTRY.register("oil", () -> new BaseFlowingFluid.Source(OIL_PROPERTIES.get()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> OIL_FLOWING = REGISTRY.register("oil_flowing", () -> new BaseFlowingFluid.Flowing(OIL_PROPERTIES.get()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> FUEL = REGISTRY.register("fuel", () -> new BaseFlowingFluid.Source(FUEL_PROPERTIES.get()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FUEL_FLOWING = REGISTRY.register("fuel_flowing", () -> new BaseFlowingFluid.Flowing(FUEL_PROPERTIES.get()));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
