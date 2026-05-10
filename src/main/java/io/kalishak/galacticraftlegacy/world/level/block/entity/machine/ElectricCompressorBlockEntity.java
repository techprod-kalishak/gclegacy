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
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public class ElectricCompressorBlockEntity extends RecipeMachineBlockEntity<CompressingRecipeInput, ElectricCompressingRecipe> implements AlloyCompressor {
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
        registerSingleEnergyInputEnergyHandler(Direction.EAST, GalacticraftBlockEntityType.ELECTRIC_COMPRESSOR.get(), event);
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.ELECTRIC_COMPRESSOR.get(),
                (blockEntity, cxt) -> {
                    if (cxt != null && cxt.getAxis().isVertical()) {
                        return cxt == Direction.UP
                                ? RangedResourceHandler.of(() -> blockEntity.innerResourceHandler, CRAFTING_SLOT_START, FUEL_SLOT)
                                : RangedResourceHandler.of(() -> blockEntity.innerResourceHandler, RESULT_SLOT_START, RESULT_SLOT_END);
                    }

                    return new DelegatingResourceHandler<>(() -> blockEntity.innerResourceHandler);
                }
        );
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, ElectricCompressorBlockEntity compressor) {
        AbstractMachineBlockEntity.energyTransferTick(compressor.innerResourceHandler, compressor.energyHandler, compressor.compressingTimer > 0, false, AbstractMachineBlockEntity.BASIC_MACHINE_MAX_TRANSFER_RATE, AlloyCompressor.FUEL_SLOT, 250, null);
        boolean changed = false;

        int currentEnergy = compressor.energyHandler.getAmountAsInt();
        ResourceHandler<ItemResource> ingredients = RangedResourceHandler.of(() -> compressor.innerResourceHandler, AlloyCompressor.CRAFTING_SLOT_START, AlloyCompressor.CRAFTING_SLOT_END);
        boolean hasFuel = currentEnergy > 0;

        if (hasFuel) {
            CompressingRecipeInput input = new CompressingRecipeInput(3, 3, ingredients);
            RecipeHolder<ElectricCompressingRecipe> recipe = compressor.quickCheck.getRecipeFor(input, level).orElse(null);
            boolean hasIngredients = recipe != null;

            if (hasIngredients) {
                try (Transaction tx = Transaction.open(null)) {
                    if (compressor.energyHandler.extract(25, tx) > 0) {
                        tx.commit();
                    }
                }

                ItemStack recipeResult = recipe.value().assemble(input);
                ItemResource resourceInResultSlot = compressor.innerResourceHandler.getResource(10);
                int maxStackSize = compressor.innerResourceHandler.getCapacityAsInt(10, resourceInResultSlot);

                if (!recipeResult.isEmpty() && AlloyCompressor.canCompress(compressor.innerResourceHandler, maxStackSize, recipeResult)) {
                    compressor.compressingTimer++;

                    if (compressor.compressingTimer == compressor.compressingTotalTime) {
                        compressor.compressingTimer = 0;
                        compressor.compressingTotalTime = recipe.value().compressingTime();
                        AlloyCompressor.compress(compressor.innerResourceHandler, ingredients, recipeResult);
                        compressor.setRecipeUsed(recipe);
                        changed = true;
                    }
                } else {
                    compressor.compressingTimer = 0;
                }
            } else {
                compressor.compressingTimer = 0;
            }
        } else if (compressor.compressingTimer > 0) {
            compressor.compressingTimer = Mth.clamp(compressor.compressingTimer - 2, 0, compressor.compressingTotalTime);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    @Override
    protected int getBatterySlotIndex() {
        return AlloyCompressor.FUEL_SLOT;
    }

    @Override
    protected int size() {
        return AlloyCompressor.INVENTORY_SIZE_ADVANCED;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("galacticraftlegacy.block.electric_compressor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ElectricCompressorMenu(containerId, inventory, this, this.dataAccess);
    }
}
