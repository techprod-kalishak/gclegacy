package io.kalishak.galacticraftlegacy.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public class GalacricraftFluidIds {
    public static final ResourceKey<Fluid> NITROGEN = ResourceKey.create(Registries.FLUID, Constants.id("nitrogen"));
    public static final ResourceKey<Fluid> OXYGEN = ResourceKey.create(Registries.FLUID, Constants.id("oxygen"));
    public static final ResourceKey<Fluid> ARGON = ResourceKey.create(Registries.FLUID, Constants.id("argon"));
    public static final ResourceKey<Fluid> CO2 = ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide"));
}
