/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.level.block.machine.AbstractMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public abstract class AbstractElectricFurnaceBlockEntity<R extends AbstractCookingRecipe> extends RecipeMachineBlockEntity<SingleRecipeInput, R> implements WorldlyContainer {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_BATTERY = 1;
    public static final int SLOT_RESULT = 2;
    protected static final int[] SLOTS_FOR_UP = { SLOT_INPUT };
    protected static final int[] SLOTS_FOR_DOWN = { SLOT_RESULT };
    protected static final int[] SLOTS_FOR_SIDES = { SLOT_BATTERY };

    int cookingTimer;
    int cookingTotalTime;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> AbstractElectricFurnaceBlockEntity.this.cookingTimer;
                case 1 -> AbstractElectricFurnaceBlockEntity.this.cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> AbstractElectricFurnaceBlockEntity.this.cookingTimer = value;
                case 1 -> AbstractElectricFurnaceBlockEntity.this.cookingTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    protected AbstractElectricFurnaceBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state, RecipeType<R> recipeType) {
        super(entityType, pos, state, recipeType);
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    protected int getBatterySlotIndex() {
        return SLOT_BATTERY;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.cookingTimer = input.getIntOr("cooking_time_spent", (short) 0);
        this.cookingTotalTime = input.getIntOr("cooking_total_time", (short) 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("cooking_time_spent", this.cookingTimer);
        output.putInt("cooking_total_time", this.cookingTotalTime);
    }

    public static <R extends AbstractCookingRecipe> void serverTick(ServerLevel level, BlockPos pos, BlockState state, AbstractElectricFurnaceBlockEntity<R> entity) {
        boolean changed = false;
        boolean isLit = entity.cookingTimer > 0;


        ItemStack ingredient = entity.inventory.get(SLOT_INPUT);
        boolean hasIngredient = !ingredient.isEmpty();
        boolean hasFuel = entity.hasEnergyToOperate();

        if (hasFuel && hasIngredient) {
            SingleRecipeInput input = new SingleRecipeInput(ingredient);
            RecipeHolder<R> recipe = entity.quickCheck.getRecipeFor(input, level).orElse(null);

            if (recipe != null) {
                int maxStackSize = entity.getMaxStackSize();
                ItemStack burnResult = recipe.value().assemble(input);

                if (!burnResult.isEmpty() && RecipeMachineBlockEntity.canProcess(entity.inventory, maxStackSize, burnResult, entity.capacitor, 25, SLOT_RESULT)) {
                    entity.cookingTimer++;

                    if (entity.cookingTimer == entity.cookingTotalTime) {
                        entity.cookingTimer = 0;
                        entity.cookingTotalTime = recipe.value().cookingTime();
                        burn(entity.inventory, ingredient, burnResult);
                        entity.setRecipeUsed(recipe);
                        changed = true;
                    }
                } else {
                    entity.cookingTimer = 0;
                }
            }
        } else if (entity.cookingTimer > 0) {
            entity.cookingTimer = Mth.clamp(entity.cookingTimer - 2, 0, entity.cookingTotalTime);
        }

        if (isLit && hasIngredient) {
            consumeBattery(entity.capacitor, 25);
        }

        if (isLit != (entity.cookingTimer > 0)) {
            changed = true;
            state = state.setValue(AbstractMachineBlock.LIT, isLit);
            level.setBlock(pos, state, 3);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    protected static boolean consumeBattery(EnergyHandler handler, int amountPerTick) {
        boolean changed;

        try (Transaction tx = Transaction.open(null)) {
            changed = handler.extract(amountPerTick, tx) >= amountPerTick;

            if (changed) tx.commit();
        }

        return changed;
    }

    protected static void burn(NonNullList<ItemStack> items, ItemStack inputItemStack, ItemStack result) {
        ItemStack resultItemStack = items.get(2);
        if (resultItemStack.isEmpty()) {
            items.set(2, result.copy());
        } else {
            resultItemStack.grow(result.getCount());
        }

        inputItemStack.shrink(1);
    }

    protected static int getTotalCookTime(ServerLevel level, AbstractElectricFurnaceBlockEntity<?> entity) {
        SingleRecipeInput input = new SingleRecipeInput(entity.getItem(SLOT_INPUT));

        return entity.quickCheck.getRecipeFor(input, level).map(recipeHolder -> recipeHolder.value().cookingTime()).orElse(200);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        }

        return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return canPlaceItem(slot, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN;
    }

    @Override
    protected void onItemChange(int slot) {
        if (slot == SLOT_INPUT && this.level instanceof ServerLevel serverLevel) {
            this.cookingTotalTime = getTotalCookTime(serverLevel, this);
            this.cookingTimer = 0;
            setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if (slot == SLOT_INPUT) {
            return true;
        }

        return slot == SLOT_BATTERY && itemStack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(itemStack)) != null;
    }
}
