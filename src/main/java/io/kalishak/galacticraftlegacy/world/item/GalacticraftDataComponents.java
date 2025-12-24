package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

public final class GalacticraftDataComponents {
    private static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<DataComponentType<?>, @NonNull DataComponentType<GearEquippable>> GEAR_EQUIPPABLE = REGISTRY.registerComponentType(
            "gear_equippable",
            builder -> builder.persistent(GearEquippable.CODEC).networkSynchronized(GearEquippable.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<@NonNull ImmutableItemCapacitor>> ENERGY_CAPACITOR = REGISTRY.registerComponentType(
            "energy_capacitor",
            builder -> builder.persistent(ImmutableItemCapacitor.CODEC).networkSynchronized(ImmutableItemCapacitor.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<@NonNull Integer>> STORED_ENERGY = REGISTRY.registerComponentType(
            "stored_energy",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, @NonNull DataComponentType<SimpleFluidContent>> OXYGEN_TANK = REGISTRY.registerComponentType(
            "oxygen_tank",
            builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC).cacheEncoding()
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
