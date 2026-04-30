/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.display;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftSlotDisplays {
    private static final DeferredRegister<SlotDisplay.Type<?>> REGISTRY = DeferredRegister.create(Registries.SLOT_DISPLAY, Galacticraft.MODID);

    public static final DeferredHolder<SlotDisplay.Type<?>, SlotDisplay.Type<BatterySlotDisplay>> BATTERY = REGISTRY.register(
            "battery",
            () -> new SlotDisplay.Type<>(BatterySlotDisplay.MAP_CODEC, BatterySlotDisplay.STREAM_CODEC)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
