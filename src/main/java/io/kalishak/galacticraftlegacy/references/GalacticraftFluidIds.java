/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public class GalacticraftFluidIds {
    public static final ResourceKey<Fluid> NITROGEN = ResourceKey.create(Registries.FLUID, Constants.id("nitrogen"));
    public static final ResourceKey<Fluid> OXYGEN = ResourceKey.create(Registries.FLUID, Constants.id("oxygen"));
    public static final ResourceKey<Fluid> FLOWING_OXYGEN = ResourceKey.create(Registries.FLUID, Constants.id("flowing_oxygen"));
    public static final ResourceKey<Fluid> ARGON = ResourceKey.create(Registries.FLUID, Constants.id("argon"));
    public static final ResourceKey<Fluid> CO2 = ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide"));

    public static final ResourceKey<Fluid> FUEL = ResourceKey.create(Registries.FLUID, Constants.id("fuel"));
    public static final ResourceKey<Fluid> FLOWING_FUEL = ResourceKey.create(Registries.FLUID, Constants.id("flowing_fuel"));
    public static final ResourceKey<Fluid> OIL = ResourceKey.create(Registries.FLUID, Constants.id("oil"));
    public static final ResourceKey<Fluid> FLOWING_OIL = ResourceKey.create(Registries.FLUID, Constants.id("flowing_oil"));
}
