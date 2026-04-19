/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftLootFunctions {
    private static final DeferredRegister<MapCodec<? extends LootItemFunction>> REGISTRY = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<MapCodec<? extends LootItemFunction>, MapCodec<SetItemFluidTankFunction>> FLUID_TANK = REGISTRY.register(
            "fluid_tank",
            () -> SetItemFluidTankFunction.MAP_CODEC
    );
    public static final DeferredHolder<MapCodec<? extends LootItemFunction>, MapCodec<SetItemCapacitorFunction>> CAPACITOR = REGISTRY.register(
            "capacitor",
            () -> SetItemCapacitorFunction.MAP_CODEC
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
