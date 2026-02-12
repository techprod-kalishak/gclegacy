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
            CircuitRecipe.Serializer::new
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AnvilCompressingRecipe>> COMPRESSING = REGISTRY.register(
            "compressing",
            () -> new CompressingRecipe.Serializer<>(AnvilCompressingRecipe::new, 200)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ElectricCompressingRecipe>> ELECTRIC_COMPRESSING = REGISTRY.register(
            "electric_compressing",
            () -> new CompressingRecipe.Serializer<>(ElectricCompressingRecipe::new, 100)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HeatingRecipe>> HEATING = REGISTRY.register(
            "heating",
            () -> new AbstractSmeltingRecipe.Serializer<>(HeatingRecipe::new, 100)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArcHeatingRecipe>> ARC_HEATING = REGISTRY.register(
            "arc_heating",
            () -> new AbstractSmeltingRecipe.Serializer<>(ArcHeatingRecipe::new, 50)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
