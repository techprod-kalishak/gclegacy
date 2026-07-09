package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.ElectricFurnaceRecipeBookComponent;
import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.SearchRecipeBookCategory;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricArcFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ArcHeatingRecipe;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ElectricArcFurnaceBlockEntity;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

import java.util.List;

public class ElectricArcFurnaceScreen extends AbstractElectricFurnaceScreen<ArcHeatingRecipe, ElectricArcFurnaceBlockEntity, ElectricArcFurnaceMenu> {
    private static final Identifier TEXTURES = Constants.id("textures/gui/container/electric_arc_furnace.png");
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            Constants.id("recipe_book/electric_arc_furnace_filter_enabled"),
            Constants.id("recipe_book/electric_arc_furnace_filter_disabled"),
            Constants.id("recipe_book/electric_arc_furnace_filter_enabled_highlighted"),
            Constants.id("recipe_book/electric_arc_furnace_filter_disabled_highlighted")
    );
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            asTab(SearchRecipeBookCategory.ARC_HEATING),
            new RecipeBookComponent.TabInfo(GalacticraftItems.CHEESE_CHUNK.get(), GalacticraftRecipeBookCategories.ARC_HEATING_FOOD.get()),
            new RecipeBookComponent.TabInfo(Items.IRON_BLOCK, GalacticraftRecipeBookCategories.ARC_HEATING_BLOCKS.get()),
            new RecipeBookComponent.TabInfo(Items.MAGMA_BLOCK, Items.NETHERITE_INGOT, GalacticraftRecipeBookCategories.ARC_HEATING_MISC.get())
    );

    public ElectricArcFurnaceScreen(ElectricArcFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, new ElectricFurnaceRecipeBookComponent(menu, FILTER_SPRITES, GalacticraftComponents.FILTER_NAME_HEATABLE, TABS), playerInventory, title, TEXTURES);
    }
}
