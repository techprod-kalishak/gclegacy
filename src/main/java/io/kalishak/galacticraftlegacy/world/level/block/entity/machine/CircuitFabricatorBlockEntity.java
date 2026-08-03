/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.machine.CircuitFabricatorMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CircuitRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public class CircuitFabricatorBlockEntity extends RecipeMachineBlockEntity<CraftingInput, CircuitRecipe> {
    public static final int DATA_PROCESS_PROGRESS = 0;
    public static final int DATA_PROGRESS_TIME_TOTAL = 1;
    public static final int PROCESS_RETRACT_SPEED = 2;
    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_DIAMOND = 1;
    public static final int SLOT_SILICON_1 = 2;
    public static final int SLOT_SILICON_2 = 3;
    public static final int SLOT_REDSTONE = 4;
    public static final int SLOT_INGREDIENT = 5;
    public static final int SLOT_OUTPUT = 6;
    public static final int SLOT_COUNT = 7;
    public static final int[] BATTERY_SLOTS = new int[] { 0 };
    public static final int[] INPUT_SLOTS = new int[] { 1, 2, 3, 4, 5 };
    public static final int[] OUTPUT_SLOTS = new int[] { 6 };
    private int processProgress;
    private int processTimeTotal;
    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_PROCESS_PROGRESS -> CircuitFabricatorBlockEntity.this.processProgress;
                case DATA_PROGRESS_TIME_TOTAL -> CircuitFabricatorBlockEntity.this.processTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_PROCESS_PROGRESS -> CircuitFabricatorBlockEntity.this.processProgress = value;
                case DATA_PROGRESS_TIME_TOTAL -> CircuitFabricatorBlockEntity.this.processTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public CircuitFabricatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(), pos, blockState, GalacticraftRecipeType.CIRCUIT.get());
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, CircuitFabricatorBlockEntity entity) {
        AbstractMachineBlockEntity.extractBattery(entity, false, 250, null);

        boolean changed = false;

        ItemStack mainIngredient = entity.getItem(SLOT_INGREDIENT);
        boolean canOperate = entity.hasEnergyToOperate();

        if (canOperate && !mainIngredient.isEmpty()) {
            CraftingInput input = CraftingInput.of(2, 3, entity.items.copyToList());

            RecipeHolder<CircuitRecipe> recipe = entity.quickCheck.getRecipeFor(input, level).orElse(null);

            if (recipe != null) {
                ItemStack result = recipe.value().assemble(input);
                int maxStackSize = entity.items.getCapacityAsInt(SLOT_OUTPUT, ItemResource.of(result));

                if (!result.isEmpty() && RecipeMachineBlockEntity.canProcess(entity, maxStackSize, result, entity.capacitor, entity.getMaxEnergyTransferRate(), SLOT_OUTPUT)) {
                    entity.processProgress++;

                    if (entity.processProgress == entity.processTimeTotal) {
                        entity.processProgress = 0;
                        entity.processTimeTotal = 400;
                        process(entity, result, entity.capacitor, entity.getMaxEnergyTransferRate());
                        changed = true;
                    }
                }
            }
        } else if (entity.processProgress > 0) {
            entity.processProgress = Mth.clamp(entity.processProgress - PROCESS_RETRACT_SPEED, 0, entity.processTimeTotal);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private static void process(CircuitFabricatorBlockEntity entity, ItemStack result, EnergyHandler energyHandler, int energyPerTick) {
        try (Transaction tx = Transaction.open(null)) {
            if (energyHandler.extract(energyPerTick, tx) >= energyPerTick) {
                ItemStack inResultSlot = entity.getItem(SLOT_OUTPUT);

                if (inResultSlot.isEmpty()) {
                    entity.setItem(SLOT_OUTPUT, result.copy());
                } else {
                    if (entity.items.insert(SLOT_OUTPUT, ItemResource.of(result), result.getCount(), tx) == result.getCount()) {
                        for (int i = SLOT_DIAMOND; i < SLOT_OUTPUT; i++) {
                            ItemStack inSlot = entity.getItem(i);

                            if (entity.items.extract(i, ItemResource.of(inSlot), inSlot.getCount(), tx) <= 0) {
                                return;
                            }
                        }
                    }
                }

                tx.commit();
            }
        }
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerEnergyCapability(GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(), event);
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(),
                (entity, side) -> switch (side) {
                    case UP -> RangedResourceHandler.of(() -> entity.items, SLOT_DIAMOND, SLOT_OUTPUT);
                    case DOWN -> RangedResourceHandler.ofSingleIndex(() -> entity.items, SLOT_OUTPUT);
                    case null -> entity.items;
                    default -> RangedResourceHandler.ofSingleIndex(() -> entity.items, SLOT_BATTERY);
                }
        );
    }

    private static boolean checkRecipe(CircuitFabricatorBlockEntity machine, ServerLevel serverLevel) {
        CraftingInput input = CraftingInput.of(2, 3, machine.items.copyToList());
        return machine.quickCheck.getRecipeFor(input, serverLevel).isPresent();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("ProcessProgress", this.processProgress);
        output.putInt("ProcessTotalTime", this.processTimeTotal);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.processProgress = input.getIntOr("ProcessProgress", 0);
        this.processTimeTotal = input.getIntOr("ProcessTotalTime", 0);
    }

    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return OUTPUT_SLOTS;
        }

        return direction == Direction.UP ? INPUT_SLOTS : BATTERY_SLOTS;
    }

    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
//        return canPlaceItem(slot, itemStack);
        return true;
    }

    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        if (hasRequiredIngredients(this, SLOT_DIAMOND, SLOT_OUTPUT)) {
            if (this.level instanceof ServerLevel serverLevel) {
                if (checkRecipe(this, serverLevel) && this.processTimeTotal == 0) {
                    this.processProgress = 0;
                    this.processTimeTotal = 400;

                    setChanged();
                }
            }
        }

        super.setItem(slot, itemStack);
    }

    @Override
    public int getSize() {
        return SLOT_COUNT;
    }

    @Override
    protected int getBatterySlotIndex() {
        return SLOT_BATTERY;
    }

    @Override
    protected Component getDefaultName() {
        return GalacticraftBlocks.CIRCUIT_FABRICATOR.get().getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new CircuitFabricatorMenu(containerId, playerInventory, this, this.containerData);
    }
}
