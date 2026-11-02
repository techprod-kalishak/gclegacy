/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.ai.attributes;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftAttributes {
    private static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, Galacticraft.MODID);

    public static final DeferredHolder<Attribute, PercentageAttribute> THERMAL_PROTECTION = REGISTRY.register(
            "thermal_protection",
            () -> new PercentageAttribute("attribute.name.thermal_protection", 0.0D, -0.25D, 0.25D)
    );
    public static final DeferredHolder<Attribute, BooleanAttribute> CORROSION_PROTECTION = REGISTRY.register(
            "corrosion_protection",
            () -> new BooleanAttribute("attribute.name.corrosion_protection", false)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
