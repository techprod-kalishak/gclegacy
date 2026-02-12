package io.kalishak.galacticraftlegacy.world.level.material.fluid;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GalacticraftFluidType {
    private static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Galacticraft.MODID);

    public static final DeferredHolder<FluidType, FluidType> OXYGEN = REGISTRY.register(
            "oxygen",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.galacticraftlegacy.oxygen")
            )
    );
    public static final DeferredHolder<FluidType, FluidType> OIL = REGISTRY.register(
            "oil",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.galacticraftlegacy.oil")
                    .viscosity(2230)
                    .density(700)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY))
    );
    public static final DeferredHolder<FluidType, FluidType> FUEL = REGISTRY.register(
            "fuel",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.galacticraftlegacy.fuel")
                    .viscosity(850)
                    .density(770)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY))
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
