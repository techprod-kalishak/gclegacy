/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.SearchRecipeBookCategory;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.ElectricFurnaceRecipeBookComponent;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.HeatingRecipe;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ElectricFurnaceBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;

import java.util.List;

public class ElectricFurnaceScreen extends AbstractElectricFurnaceScreen<HeatingRecipe, ElectricFurnaceBlockEntity, ElectricFurnaceMenu> {
    private static final Identifier TEXTURES = Constants.id("textures/gui/container/electric_furnace.png");
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            Constants.id("recipe_book/electric_furnace_filter_enabled"),
            Constants.id("recipe_book/electric_furnace_filter_disabled"),
            Constants.id("recipe_book/electric_furnace_filter_enabled_highlighted"),
            Constants.id("recipe_book/electric_furnace_filter_disabled_highlighted")
    );
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            asTab(SearchRecipeBookCategory.HEATING),
            new RecipeBookComponent.TabInfo(Items.BEEF, GalacticraftRecipeBookCategories.HEATING_FOOD.get()),
            new RecipeBookComponent.TabInfo(Items.SMOOTH_STONE, GalacticraftRecipeBookCategories.HEATING_BLOCKS.get()),
            new RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.DIAMOND, GalacticraftRecipeBookCategories.HEATING_MISC.get())
    );

    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, new ElectricFurnaceRecipeBookComponent(menu, FILTER_SPRITES, GalacticraftComponents.FILTER_NAME_HEATABLE.asComponent(), TABS), playerInventory, title, TEXTURES);
    }
}
