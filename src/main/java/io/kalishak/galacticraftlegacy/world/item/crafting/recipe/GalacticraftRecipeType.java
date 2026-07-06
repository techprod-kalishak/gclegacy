/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftRecipeType {
    private static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CircuitRecipe>> CIRCUIT = REGISTRY.register(
            "circuit",
            () -> RecipeType.simple(Constants.id("circuit"))
    );
    public static final DeferredHolder<RecipeType<?>, RecipeType<AnvilCompressingRecipe>> COMPRESSING = REGISTRY.register(
            "compressing",
            () -> RecipeType.simple(Constants.id("compressing"))
    );
    public static final DeferredHolder<RecipeType<?>, RecipeType<ElectricCompressingRecipe>> ELECTRIC_COMPRESSING = REGISTRY.register(
            "electric_compressing",
            () -> RecipeType.simple(Constants.id("electric_compressing"))
    );
    public static final DeferredHolder<RecipeType<?>, RecipeType<HeatingRecipe>> HEATING = REGISTRY.register(
            "heating",
            () -> RecipeType.simple(Constants.id("heating"))
    );
    public static final DeferredHolder<RecipeType<?>, RecipeType<ArcHeatingRecipe>> ARC_HEATING = REGISTRY.register(
            "arc_heating",
            () -> RecipeType.simple(Constants.id("arc_heating"))
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
