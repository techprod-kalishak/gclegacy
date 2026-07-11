/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.container.WorldlyStorage;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ElectricCookingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public abstract class AbstractElectricFurnaceBlockEntity<R extends ElectricCookingRecipe> extends RecipeMachineBlockEntity<SingleRecipeInput, R> implements WorldlyStorage {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_BATTERY = 1;
    public static final int SLOT_RESULT = 2;
    public static final int SLOT_DOUBLE_RESULT = 3;
    protected static final int[] SLOTS_FOR_UP = { SLOT_INPUT };
    protected static final int[] SLOTS_FOR_DOWN = { SLOT_RESULT };
    protected static final int[] SLOTS_FOR_SIDES = { SLOT_BATTERY };
    protected static final int[] SLOTS_FOR_DOUBLE_DOWN = { SLOT_RESULT, SLOT_DOUBLE_RESULT };

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

    public static <BE extends AbstractElectricFurnaceBlockEntity<?>> void registerCapabilities(RegisterCapabilitiesEvent event, BlockEntityType<BE> entityType) {
        registerEnergyCapability(entityType, event);
        registerItemCapability(entityType, event);
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

    public static <R extends ElectricCookingRecipe> void serverTick(ServerLevel level, BlockPos pos, BlockState state, AbstractElectricFurnaceBlockEntity<R> entity) {
        AbstractMachineBlockEntity.extractBattery(entity, false, 250, null);

        if (entity.cookingTimer > 0) {
            consumeBattery(entity.capacitor, null);
        }

        boolean changed = false;

        ItemStack ingredient = ItemUtil.getStack(entity.items, SLOT_INPUT);
        boolean hasIngredient = !ingredient.isEmpty();
        boolean hasFuel = entity.hasEnergyToOperate();

        if (hasFuel && hasIngredient) {
            SingleRecipeInput input = new SingleRecipeInput(ingredient);
            RecipeHolder<R> recipe = entity.quickCheck.getRecipeFor(input, level).orElse(null);

            if (recipe != null) {
                ItemStack heatResult = recipe.value().assemble(input);

                try (Transaction transaction = Transaction.open(null)) {
                    if (!heatResult.isEmpty() && canHeat(entity.items, heatResult, entity.capacitor, transaction)) {
                        entity.cookingTimer++;

                        if (entity.cookingTimer == entity.cookingTotalTime) {
                            heat(entity.items, heatResult, entity.capacitor, transaction);
                            entity.cookingTimer = 0;
                            entity.cookingTotalTime = recipe.value().cookingTime();
                            entity.setRecipeUsed(recipe);
                            changed = true;
                            transaction.commit();
                        }
                    } else {
                        entity.cookingTotalTime = 0;
                    }
                }
            }
        } else if (entity.cookingTimer > 0) {
            entity.cookingTimer = Mth.clamp(entity.cookingTimer - 2, 0, entity.cookingTotalTime);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    protected static boolean consumeBattery(EnergyHandler handler, @Nullable Transaction rootTransaction) {
        try (Transaction tx = Transaction.open(rootTransaction)) {
            if (handler.extract(25, tx) > 0) {
                tx.commit();
                return true;
            }
        }

        return false;
    }

    private static boolean canHeat(ResourceHandler<ItemResource> items, ItemStack heatResult, EnergyHandler capacitor, @Nullable Transaction rootTransaction) {
        ItemResource heatResultResource = ItemResource.of(heatResult);
        int count = heatResult.count();

        if (!heatResult.isEmpty()) {
            ItemResource resultAtSlot = items.getResource(SLOT_RESULT);

            try (Transaction childTransaction = Transaction.open(rootTransaction)) {
                if (capacitor.extract(25, childTransaction) < 25) {
                    return false;
                }

                if (resultAtSlot.isEmpty() || items.insert(heatResultResource, count, childTransaction) > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    protected static boolean insertInOutputSlot(ResourceHandler<ItemResource> items, ItemStack toPut, Transaction tx) {
        int slotCandidate = items.insert(SLOT_RESULT, ItemResource.of(toPut), toPut.count(), tx);

        if (slotCandidate > 0) {
            return true;
        }

        return items.size() > 3 && items.insert(SLOT_DOUBLE_RESULT, ItemResource.of(toPut), toPut.count(), tx) > 0;
    }

    protected static void heat(ResourceHandler<ItemResource> items, ItemStack result, EnergyHandler capacitor, @Nullable Transaction rootTransaction) {
        try (Transaction childTransaction = Transaction.open(rootTransaction)) {
            if (insertInOutputSlot(items, result, rootTransaction)) {
                if (items.extract(items.getResource(SLOT_INPUT), 1, childTransaction) > 0) {

                    if (consumeBattery(capacitor, childTransaction)) {
                        childTransaction.commit();
                    }
                }
            }
        }
    }

    protected static int getTotalCookTime(ServerLevel level, AbstractElectricFurnaceBlockEntity<?> entity) {
        SingleRecipeInput input = new SingleRecipeInput(entity.getItem(SLOT_INPUT));

        return entity.quickCheck.getRecipeFor(input, level).map(recipeHolder -> recipeHolder.value().cookingTime()).orElse(100);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return isValid(slot, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN;
    }

    @Override
    protected void refreshSlots() {
        if (this.cookingTimer == 0 && this.level instanceof ServerLevel serverLevel) {
            ItemResource resource = this.items.getResource(SLOT_INPUT);

            if (!resource.isEmpty()) {
                this.cookingTotalTime = getTotalCookTime(serverLevel, this);
                this.cookingTimer = 0;
                setChanged();
            }
        }
    }

    @Override
    protected void onItemChange(int slot, ItemStack previousStack) {
        ItemResource resource = this.items.getResource(slot);

        if (!resource.matches(previousStack) && slot == SLOT_INPUT && this.level instanceof ServerLevel serverLevel) {
            this.cookingTotalTime = getTotalCookTime(serverLevel, this);
            this.cookingTimer = 0;
            setChanged();
        }
    }

    @Override
    protected boolean isValid(int slot, ItemStack stack) {
//        if (slot == getBatterySlotIndex()) {
//            return ItemAccessEnergyUtils.hasEnergyHandler(stack);
//        }

        return true;
    }
}
