/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.slot;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class CraftingResultHandlerSlot extends ResourceHandlerSlot {
    private final CraftingContainer craftSlots;
    private final Player player;
    private int removeCount;

    public CraftingResultHandlerSlot(Player player, CraftingContainer craftSlots, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int handlerSlot, int xPosition, int yPosition) {
        super(handler, slotModifier, handlerSlot, xPosition, yPosition);
        this.player = player;
        this.craftSlots = craftSlots;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (hasItem()) {
            this.removeCount += Math.min(amount, getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(ItemStack picked, int count) {
        this.removeCount += count;
        checkTakeAchievements(picked);
    }

    @Override
    protected void onSwapCraft(int count) {
        this.removeCount += count;
    }

    @Override
    protected void checkTakeAchievements(ItemStack carried) {
        if (this.removeCount > 0) {
            carried.onCraftedBy(this.player, this.removeCount);
        }

        if (getResourceHandler() instanceof RecipeCraftingHolder recipeCraftingHolder) {
            recipeCraftingHolder.awardUsedRecipes(this.player, this.craftSlots.getItems());
        }

        this.removeCount = 0;
    }

    private static NonNullList<ItemStack> copyAllInputItems(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int slot = 0; slot < result.size(); ++slot) {
            result.set(slot, input.getItem(slot));
        }

        return result;
    }

    private NonNullList<ItemStack> getRemainingItems(CraftingInput input, Level level) {
        NonNullList<ItemStack> remaining;
        if (level instanceof ServerLevel serverLevel) {
            remaining = serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, serverLevel).map((recipe) -> recipe.value().getRemainingItems(input)).orElseGet(() -> copyAllInputItems(input));
        } else {
            remaining = CraftingRecipe.defaultCraftingReminder(input);
        }

        return remaining;
    }

    @Override
    public void onTake(Player player, ItemStack carried) {
        checkTakeAchievements(carried);
        CraftingInput.Positioned positionedRecipe = this.craftSlots.asPositionedCraftInput();
        CraftingInput input = positionedRecipe.input();
        int recipeLeft = positionedRecipe.left();
        int recipeTop = positionedRecipe.top();

        NonNullList<ItemStack> remaining = getRemainingItems(input, player.level());

        for (int y = 0; y < input.height(); ++y) {
            for (int x = 0; x < input.width(); ++x) {
                int slot = x + recipeLeft + (y + recipeTop) * this.craftSlots.getWidth();
                ItemStack itemStack = this.craftSlots.getItem(slot);
                ItemStack replacement = remaining.get(x + y * input.width());

                if (!itemStack.isEmpty()) {
                    this.craftSlots.removeItem(slot, 1);
                    itemStack = this.craftSlots.getItem(slot);
                }

                if (!replacement.isEmpty()) {
                    if (itemStack.isEmpty()) {
                        this.craftSlots.setItem(slot, replacement);
                    } else if (ItemStack.isSameItemSameComponents(itemStack, replacement)) {
                        replacement.grow(itemStack.getCount());
                        this.craftSlots.setItem(slot, replacement);
                    } else if (!this.player.getInventory().add(replacement)) {
                        this.player.drop(replacement, false);
                    }
                }
            }
        }
    }

    @Override
    public boolean isFake() {
        return true;
    }
}
