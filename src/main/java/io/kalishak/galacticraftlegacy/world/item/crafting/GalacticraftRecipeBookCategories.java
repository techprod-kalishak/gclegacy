/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftRecipeBookCategories {
    private static final DeferredRegister<RecipeBookCategory> REGISTRY = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, Galacticraft.MODID);

    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> ARC_HEATING_BLOCKS = REGISTRY.register("arc_heating_blocks", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> ARC_HEATING_FOOD = REGISTRY.register("arc_heating_food", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> ARC_HEATING_MISC = REGISTRY.register("arc_heating_misc", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> COMPRESSING = REGISTRY.register("compressing", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> CRAFTING_ROCKET = REGISTRY.register("crafting_rocket", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> CRAFTING_LAND_VEHICLE = REGISTRY.register("crafting_land_vehicle", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> CRAFTING_FLOATING_VEHICLES = REGISTRY.register("crafting_floating_vehicles", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> CRAFTING_MISC_VEHICLES = REGISTRY.register("crafting_misc_vehicles", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> ELECTRIC_COMPRESSING = REGISTRY.register("electric_compressing", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> FABRICATING = REGISTRY.register("fabricating", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> HEATING_BLOCKS = REGISTRY.register("heating_blocks", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> HEATING_FOOD = REGISTRY.register("heating_food", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> HEATING_MISC = REGISTRY.register("heating_misc", RecipeBookCategory::new);

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
