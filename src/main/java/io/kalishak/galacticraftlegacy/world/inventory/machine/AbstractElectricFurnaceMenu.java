/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ElectricCookingRecipe;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractElectricFurnaceBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractElectricFurnaceMenu<R extends ElectricCookingRecipe, M extends AbstractElectricFurnaceBlockEntity<R>> extends AbstractMachineRecipeBookMenu<M> {
    protected final Supplier<RecipeType<R>> recipeType;
    private @Nullable RecipeHolder<R> lastUsedRecipe;

    protected AbstractElectricFurnaceMenu(MenuType<? extends AbstractMachineRecipeBookMenu<M>> menuType, int containerId, Inventory playerInventory, M machine, ContainerData containerData, Supplier<RecipeType<R>> recipeType) {
        super(menuType, containerId, playerInventory, machine, containerData);
        this.recipeType = recipeType;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        this.machine.fillStackedContents(stackedItemContents);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.FURNACE;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();

            if (index == 2) {
                if (!moveItemStackTo(stack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, clicked);
            } else if ((this instanceof ElectricArcFurnaceMenu) && index == 3) {
                if (!moveItemStackTo(stack, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, clicked);
            } else if (index != 1 && index != 0) {
                if (canSmelt(stack)) {
                    if (!moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isFuel(stack)) {
                    if (!moveItemStackTo(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!moveItemStackTo(stack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !moveItemStackTo(stack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return clicked;
    }

    public float getBurnProgress() {
        int i = this.containerData.get(0);
        int j = this.containerData.get(1);
        return j != 0 && i != 0 ? Mth.clamp((float)i / j, 0.0F, 1.0F) : 0.0F;
    }

    protected boolean canSmelt(ItemStack stackInSlot) {
        if (stackInSlot.isEmpty() || !this.machine.hasLevel()) {
            return false;
        }

        if (this.level instanceof ServerLevel serverLevel) {
            Optional<RecipeHolder<R>> recipe = serverLevel.recipeAccess().getRecipeFor(this.recipeType.get(), new SingleRecipeInput(stackInSlot), serverLevel, this.lastUsedRecipe);
            recipe.ifPresent(this::setLastUsedRecipe);

            return recipe.isPresent();
        }

        return false;
    }

    private void setLastUsedRecipe(@Nullable RecipeHolder<R> lastUsedRecipe) {
        this.lastUsedRecipe = lastUsedRecipe;
    }

    protected boolean isFuel(ItemStack stack) {
        FurnaceFuel furnaceFuel = stack.typeHolder().getData(NeoForgeDataMaps.FURNACE_FUELS);

        return furnaceFuel != null && furnaceFuel.burnTime() > 0;
    }
}
