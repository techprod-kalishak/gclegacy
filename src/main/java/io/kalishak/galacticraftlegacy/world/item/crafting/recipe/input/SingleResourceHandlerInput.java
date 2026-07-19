package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;

public record SingleResourceHandlerInput(ResourceHandler<ItemResource> delegate, int index) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return ItemUtil.getStack(this.delegate, this.index);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getItem(this.index).isEmpty();
    }
}
