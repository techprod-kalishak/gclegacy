package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.ResourceHandlerInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

/**
 * Base for machine recipes
 * @param <I> Input backed by {@link net.neoforged.neoforge.transfer.ResourceHandler}
 */
public abstract class MachineRecipe<I extends ResourceHandlerInput> implements Recipe<I> {
    protected final String group;
    protected final ItemStack result;
    protected @Nullable PlacementInfo placementInfo;

    protected MachineRecipe(String group, ItemStack result) {
        this.group = group;
        this.result = result;
    }

    @Override
    public abstract RecipeSerializer<? extends MachineRecipe<I>> getSerializer();

    @Override
    public abstract RecipeType<? extends MachineRecipe<I>> getType();

    @Override
    public String group() {
        return this.group;
    }

    /**
     * Use {@link #disassembleIngredients(I, Transaction, HolderLookup.Provider, boolean)} instead.
     * @param input resource handler with ingredients
     * @param registries registries
     * @return recipe's result
     */
    @Override
    public final ItemStack assemble(I input, HolderLookup.Provider registries) {
        return result().copy();
    }

    /**
     *
     * @param resourceInput resource handler with ingredients
     * @param tx transaction, can be null
     * @param registries registries lol
     * @param simulate mark if all transaction should be committed
     * @return recipe's result if all ingredients could be removed
     */
    public abstract ItemStack disassembleIngredients(I resourceInput, @Nullable Transaction tx, HolderLookup.Provider registries, boolean simulate);

    protected ItemStack result() {
        return this.result;
    }
}
