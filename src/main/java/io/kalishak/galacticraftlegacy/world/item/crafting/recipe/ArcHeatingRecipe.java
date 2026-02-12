package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class ArcHeatingRecipe extends AbstractSmeltingRecipe {
    public ArcHeatingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, int cookingTime) {
        super(group, category, ingredient, result, cookingTime);
    }

    @Override
    protected Holder<Item> icon() {
        return GalacticraftItems.ELECTRIC_FURNACE;
    }

    @Override
    public RecipeSerializer<ArcHeatingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.ARC_HEATING.get();
    }

    @Override
    public RecipeType<ArcHeatingRecipe> getType() {
        return GalacticraftRecipeType.ARC_HEATING.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of();
    }
}
