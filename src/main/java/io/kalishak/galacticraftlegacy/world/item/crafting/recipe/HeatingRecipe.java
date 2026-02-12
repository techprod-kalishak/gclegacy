package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.ElectricFurnaceRecipeDisplay;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public class HeatingRecipe extends AbstractSmeltingRecipe {
    public HeatingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, int cookingTime) {
        super(group, category, ingredient, result, cookingTime);
    }

    @Override
    protected Holder<Item> icon() {
        return GalacticraftItems.ELECTRIC_FURNACE;
    }

    @Override
    public RecipeSerializer<? extends HeatingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.HEATING.get();
    }

    @Override
    public RecipeType<? extends HeatingRecipe> getType() {
        return GalacticraftRecipeType.HEATING.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ElectricFurnaceRecipeDisplay(
                        ingredient().display(),
                        new SlotDisplay.ItemSlotDisplay(GalacticraftItems.BATTERY),
                        new SlotDisplay.ItemStackSlotDisplay(result()),
                        new SlotDisplay.ItemSlotDisplay(icon()),
                        this.cookingTime
                )
        );
    }
}
