package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.StaticRecipePattern;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public class ElectricCompressingRecipe extends CompressingRecipe {
    public ElectricCompressingRecipe(String group, StaticRecipePattern pattern, ItemStack result, float experience, int compressingTime) {
        super(group, pattern, result, experience, compressingTime);
    }

    @Override
    public RecipeSerializer<ElectricCompressingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.ELECTRIC_COMPRESSING.get();
    }

    @Override
    public RecipeType<ElectricCompressingRecipe> getType() {
        return GalacticraftRecipeType.ELECTRIC_COMPRESSING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return GalacticraftRecipeBookCategories.ELECTRIC_COMPRESSING.get();
    }

    @Override
    protected Holder<Item> icon() {
        return null;
    }

    @Override
    protected SlotDisplay energySource() {
        return null;
    }
}
