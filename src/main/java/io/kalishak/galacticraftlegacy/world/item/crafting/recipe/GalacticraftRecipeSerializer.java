/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftRecipeSerializer {
    private static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Galacticraft.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CircuitRecipe>> CIRCUIT = REGISTRY.register(
            "circuit",
            () -> new RecipeSerializer<>(CircuitRecipe.CODEC, CircuitRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AnvilCompressingRecipe>> COMPRESSING = REGISTRY.register(
            "compressing",
            () -> new RecipeSerializer<>(AnvilCompressingRecipe.CODEC, AnvilCompressingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ElectricCompressingRecipe>> ELECTRIC_COMPRESSING = REGISTRY.register(
            "electric_compressing",
            () -> new RecipeSerializer<>(ElectricCompressingRecipe.CODEC, ElectricCompressingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HeatingRecipe>> HEATING = REGISTRY.register(
            "heating",
            () -> AbstractSmeltingRecipe.recipeSerializer(HeatingRecipe::new, 100)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArcHeatingRecipe>> ARC_HEATING = REGISTRY.register(
            "arc_heating",
            () -> AbstractSmeltingRecipe.recipeSerializer(ArcHeatingRecipe::new, 50)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
