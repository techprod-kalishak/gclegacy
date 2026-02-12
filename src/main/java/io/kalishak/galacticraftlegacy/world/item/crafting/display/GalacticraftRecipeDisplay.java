package io.kalishak.galacticraftlegacy.world.item.crafting.display;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftRecipeDisplay {
    private static final DeferredRegister<RecipeDisplay.Type<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_DISPLAY, Galacticraft.MODID);

    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<CircutFabricatorRecipeDisplay>> CIRCUIT_FABRICATOR = REGISTRY.register(
            "circuit_fabricator",
            () -> new RecipeDisplay.Type<>(CircutFabricatorRecipeDisplay.MAP_CODEC, CircutFabricatorRecipeDisplay.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<CompressorRecipeDisplay>> COMPRESSOR = REGISTRY.register(
            "compressor",
            () -> new RecipeDisplay.Type<>(CompressorRecipeDisplay.MAP_CODEC, CompressorRecipeDisplay.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<ElectricFurnaceRecipeDisplay>> ELECTRIC_FURNACE = REGISTRY.register(
            "electric_furnace",
            () -> new RecipeDisplay.Type<>(ElectricFurnaceRecipeDisplay.MAP_CODEC, ElectricFurnaceRecipeDisplay.STREAM_CODEC)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
