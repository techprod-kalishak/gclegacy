/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.transfer.node.EnergyNodeNetwork;
import io.kalishak.galacticraftlegacy.transfer.node.FluidNodeNetwork;
import io.kalishak.galacticraftlegacy.transfer.node.NodeNetwork;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PackedNodeNetworkTypes {
    private static final DeferredRegister<MapCodec<? extends NodeNetwork.PackedNode>> REGISTRY = DeferredRegister.create(GalacticraftRegistries.Keys.PACKED_NODE_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<MapCodec<? extends NodeNetwork.PackedNode>, MapCodec<FluidNodeNetwork.Packed>> FLUID = REGISTRY.register(
            "fluid",
            () -> FluidNodeNetwork.Packed.MAP_CODEC
    );
    public static final DeferredHolder<MapCodec<? extends NodeNetwork.PackedNode>, MapCodec<EnergyNodeNetwork.Packed>> ENERGY = REGISTRY.register(
            "energy",
            () -> EnergyNodeNetwork.Packed.MAP_CODEC
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
