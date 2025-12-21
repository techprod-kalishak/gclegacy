package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.GalacticraftLegacy;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

public final class GalacticraftDataComponents {
    private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GalacticraftLegacy.MODID);

    public static final DeferredHolder<DataComponentType<?>, @NonNull DataComponentType<GearEquippable>> GEAR_EQUIPPABLE = COMPONENTS.registerComponentType(
            "gear_equippable",
            builder -> builder.persistent(GearEquippable.CODEC).networkSynchronized(GearEquippable.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, @NonNull DataComponentType<OxygenTank>> OXYGEN_TANK = COMPONENTS.registerComponentType(
            "oxygen_tank",
            builder -> builder.persistent(OxygenTank.CODEC).networkSynchronized(OxygenTank.STREAM_CODEC).cacheEncoding()
    );

    public static void init(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}
