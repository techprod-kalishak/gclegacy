/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftRecipeSerializer {
    private static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Galacticraft.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArcHeatingRecipe>> ARC_HEATING = REGISTRY.register(
            "arc_heating",
            () -> new RecipeSerializer<>(ArcHeatingRecipe.MAP_CODEC, ArcHeatingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AnvilCompressingRecipe>> COMPRESSING = REGISTRY.register(
            "compressing",
            () -> new RecipeSerializer<>(AnvilCompressingRecipe.CODEC, AnvilCompressingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ElectricCompressingRecipe>> ELECTRIC_COMPRESSING = REGISTRY.register(
            "electric_compressing",
            () -> new RecipeSerializer<>(ElectricCompressingRecipe.CODEC, ElectricCompressingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CircuitRecipe>> CIRCUIT = REGISTRY.register(
            "fabricating",
            () -> new RecipeSerializer<>(CircuitRecipe.CODEC, CircuitRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HeatingRecipe>> HEATING = REGISTRY.register(
            "heating",
            () -> new RecipeSerializer<>(HeatingRecipe.MAP_CODEC, HeatingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<VehicleCraftingRecipe>> VEHICLE_CRAFTING = REGISTRY.register(
            "vehicle_crafting",
            () -> new RecipeSerializer<>(VehicleCraftingRecipe.MAP_CODEC, VehicleCraftingRecipe.STREAM_CODEC)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
