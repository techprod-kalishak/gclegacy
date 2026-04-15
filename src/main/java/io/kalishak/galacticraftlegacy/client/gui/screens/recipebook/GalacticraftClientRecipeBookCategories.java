/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.recipebook;

import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import net.neoforged.neoforge.client.event.RegisterRecipeBookSearchCategoriesEvent;

public class GalacticraftClientRecipeBookCategories {
    public static void registerBookCategories(RegisterRecipeBookSearchCategoriesEvent event) {
        event.register(SearchRecipeBookCategory.FABRICATING,
                GalacticraftRecipeBookCategories.FABRICATING.get()
        );
        event.register(SearchRecipeBookCategory.COMPRESSING,
                GalacticraftRecipeBookCategories.COMPRESSING.get()
        );
        event.register(SearchRecipeBookCategory.ELECTRIC_COMPRESSING,
                GalacticraftRecipeBookCategories.ELECTRIC_COMPRESSING.get()
        );
        event.register(SearchRecipeBookCategory.HEATING,
                GalacticraftRecipeBookCategories.HEATING_BLOCKS.get(),
                GalacticraftRecipeBookCategories.HEATING_FOOD.get(),
                GalacticraftRecipeBookCategories.HEATING_MISC.get()
        );
    }
}
