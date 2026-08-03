/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.machine.ElectricCompressorMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ElectricCompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ElectricCompressorBlockEntity extends RecipeMachineBlockEntity<CraftingInput, ElectricCompressingRecipe> implements AlloyCompressor {
    private int compressingTimer;
    private int compressingTotalTime;
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case AlloyCompressor.DATA_SLOT_COMPRESSING_TIMER -> ElectricCompressorBlockEntity.this.compressingTimer;
                case AlloyCompressor.DATA_SLOT_COMPRESSING_TIME_TOTAL -> ElectricCompressorBlockEntity.this.compressingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case AlloyCompressor.DATA_SLOT_COMPRESSING_TIMER -> ElectricCompressorBlockEntity.this.compressingTimer = value;
                case AlloyCompressor.DATA_SLOT_COMPRESSING_TIME_TOTAL -> ElectricCompressorBlockEntity.this.compressingTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public ElectricCompressorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.ELECTRIC_COMPRESSOR.get(), pos, blockState, GalacticraftRecipeType.ELECTRIC_COMPRESSING.get());
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerEnergyCapability(GalacticraftBlockEntityType.ELECTRIC_COMPRESSOR.get(), event);
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.ELECTRIC_COMPRESSOR.get(),
                (entity, side) -> switch (side) {
                    case UP -> RangedResourceHandler.of(() -> entity.items, 0, 9); // crafting grid
                    case DOWN -> RangedResourceHandler.of(() -> entity.items, 10, 12); // double output
                    case null -> entity.items;
                    default -> RangedResourceHandler.ofSingleIndex(() -> entity.items, 9); // Fuel
                }
        );
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, ElectricCompressorBlockEntity entity) {
        AbstractMachineBlockEntity.extractBattery(entity, false, 250, null);
        boolean changed = false;

        boolean hasFuel = entity.hasEnergyToOperate();

        if (hasFuel) {
            CraftingInput input = CraftingInput.of(3, 3, entity.items.copyToList().subList(AlloyCompressor.CRAFTING_SLOT_START, FUEL_SLOT));
            RecipeHolder<ElectricCompressingRecipe> recipe = entity.quickCheck.getRecipeFor(input, level).orElse(null);

            if (recipe != null) {
                try (Transaction tx = Transaction.open(null)) {
                    if (entity.capacitor.extract(25, tx) > 0) {
                        tx.commit();
                    }
                }

                ItemStack recipeResult = recipe.value().assemble(input);
                ItemStack stackInResultSlot = entity.getItem(10);
                int maxStackSize = entity.items.getCapacityAsInt(10, ItemResource.of(stackInResultSlot));

                if (recipeResult.isEmpty() || !AlloyCompressor.canCompress(entity.items, maxStackSize, recipeResult)) {
                    entity.compressingTimer++;

                    if (entity.compressingTimer == entity.compressingTotalTime) {
                        entity.compressingTimer = 0;
                        entity.compressingTotalTime = recipe.value().compressingTime();
                        AlloyCompressor.compress(RangedResourceHandler.of(() -> entity.items, CRAFTING_SLOT_START, CRAFTING_SLOT_END + 1), recipeResult);
                        entity.setRecipeUsed(recipe);
                        changed = true;
                    }
                } else {
                    entity.compressingTimer = 0;
                }
            } else {
                entity.compressingTimer = 0;
            }
        } else if (entity.compressingTimer > 0) {
            entity.compressingTimer = Mth.clamp(entity.compressingTimer - 2, 0, entity.compressingTotalTime);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    @Override
    public List<ItemStack> getCraftingItems() {
        return this.items.copyToList().subList(CRAFTING_SLOT_START, FUEL_SLOT);
    }

    @Override
    public int getSize() {
        return AlloyCompressor.INVENTORY_SIZE_ADVANCED;
    }

    @Override
    protected int getBatterySlotIndex() {
        return AlloyCompressor.FUEL_SLOT;
    }

    @Override
    public int[] getSlotsForOutput() {
        return new int[] { AlloyCompressor.RESULT_SLOT_START, AlloyCompressor.RESULT_SLOT_END };
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ElectricCompressorMenu(containerId, inventory, this, this.dataAccess);
    }
}
