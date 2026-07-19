package io.kalishak.galacticraftlegacy.world.inventory.container;

import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ResourceHandlerCraftingResultInventory implements ResourceHandler<ItemResource>, RecipeCraftingHolder {
    private @NonNull ItemStack stack = ItemStack.EMPTY;
    private @Nullable RecipeHolder<?> recipeUsed;
    public final ItemStackResourceHandler resourceHandler = new ItemStackResourceHandler() {
        @Override
        protected ItemStack getStack() {
            return ResourceHandlerCraftingResultInventory.this.stack;
        }

        @Override
        protected void setStack(ItemStack stack) {
            ResourceHandlerCraftingResultInventory.this.stack = stack;
        }
    };

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeUsed) {
        this.recipeUsed = recipeUsed;
    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return this.recipeUsed;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ItemResource getResource(int index) {
        return ItemResource.of(this.stack);
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.stack.count();
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return 1;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return this.resourceHandler.isValid(index, resource);
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return this.resourceHandler.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return this.resourceHandler.extract(index, resource, amount, transaction);
    }
}
