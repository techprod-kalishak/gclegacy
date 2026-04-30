/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.CompressorMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public class CompressorBlockEntity extends AbstractCompressorBlockEntity {
    protected int fuelTimeRemaining;
    protected int fuelTotalTime;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case 0 -> {
                    if (fuelTotalTime > Short.MAX_VALUE) {
                        yield Mth.floor(((double) fuelTimeRemaining / fuelTotalTime) * Short.MAX_VALUE);
                    }

                    yield CompressorBlockEntity.this.fuelTimeRemaining;
                }
                case 1 -> Math.min(CompressorBlockEntity.this.fuelTotalTime, Short.MAX_VALUE);
                case 2 -> CompressorBlockEntity.this.compressingTimer;
                case 3 -> CompressorBlockEntity.this.compressingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case 0 -> CompressorBlockEntity.this.fuelTimeRemaining = value;
                case 1 -> CompressorBlockEntity.this.fuelTotalTime = value;
                case 2 -> CompressorBlockEntity.this.compressingTimer = value;
                case 3 -> CompressorBlockEntity.this.compressingTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public CompressorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(GalacticraftBlockEntityType.COMPRESSOR.get(), blockPos, blockState, GalacticraftRecipeType.COMPRESSING.get());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.fuelTimeRemaining = input.getIntOr("FuelTimeRemaining", (short) 0);
        this.fuelTotalTime = input.getIntOr("FuelTotalTime", (short) 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("FuelTimeRemaining", this.fuelTimeRemaining);
        output.putInt("FuelTotalTime", this.fuelTotalTime);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, CompressorBlockEntity compressor) {
        boolean changed = false;
        boolean isLit;
        boolean wasLit;
        if (compressor.fuelTimeRemaining > 0) {
            wasLit = true;
            compressor.fuelTimeRemaining--;
            isLit = compressor.fuelTimeRemaining > 0;
        } else {
            wasLit = false;
            isLit = false;
        }

        ItemStack fuel = compressor.items.get(9);
        NonNullList<ItemStack> ingredients = NonNullList.copyOf(compressor.items.subList(0, 9));

        boolean hasIngredients = !ingredients.isEmpty();
        boolean hasFuel = !fuel.isEmpty();

        if (isLit || hasFuel && hasIngredients) {
            if (hasIngredients) {
                ResourceHandler<ItemResource> ingredientsHandler = new ItemStacksResourceHandler(ingredients);
                CompressingRecipeInput input = new CompressingRecipeInput(3, 3, () -> ingredientsHandler);
                RecipeHolder<? extends CompressingRecipe> recipe = compressor.quickCheck.getRecipeFor(input, level).orElse(null);

                if (recipe != null) {
                    ItemStack recipeResult = recipe.value().assemble(input);
                    ItemResource resourceInResultSlot = compressor.innerResourceHandler.getResource(10);
                    int maxStackSize = compressor.innerResourceHandler.getCapacityAsInt(10, resourceInResultSlot);

                    if (!recipeResult.isEmpty() && canCompress(compressor.innerResourceHandler, maxStackSize, recipeResult)) {
                        if (!isLit) {
                            int newLitTime = fuelDuration(level, fuel);
                            compressor.fuelTimeRemaining = newLitTime;
                            compressor.fuelTotalTime = newLitTime;

                            if (newLitTime > 0) {
                                consumeFuel(compressor.innerResourceHandler, fuel);
                                isLit = true;
                                changed = true;
                            }
                        }

                        if (isLit) {
                            compressor.compressingTimer++;

                            if (compressor.compressingTimer == compressor.compressingTotalTime) {
                                compressor.compressingTimer = 0;
                                compressor.compressingTotalTime = recipe.value().compressingTime();
                                compress(compressor.innerResourceHandler, ingredients, recipeResult);
                                compressor.setRecipeUsed(recipe);
                                changed = true;
                            }
                        } else {
                            compressor.compressingTimer = 0;
                        }
                    } else {
                        compressor.compressingTimer = 0;
                    }
                }
            } else {
                compressor.compressingTimer = 0;
            }
        } else if (compressor.compressingTimer > 0) {
            compressor.compressingTimer = Mth.clamp(compressor.compressingTimer - 2, 0, compressor.compressingTotalTime);
        }

        if (wasLit != isLit) {
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    protected static int fuelDuration(Level level, ItemStack itemStack) {
        FurnaceFuel fuel = level.registryAccess()
                .lookupOrThrow(Registries.ITEM)
                .getData(NeoForgeDataMaps.FURNACE_FUELS, itemStack.typeHolder().unwrapKey().orElseThrow());
        return fuel == null ? 0 : fuel.burnTime();
    }

    protected static void consumeFuel(ResourceHandler<ItemResource> items, ItemStack fuelStack) {
        try (Transaction transaction = Transaction.open(null)) {
            int extractFuel = items.extract(ItemResource.of(fuelStack), 1, transaction);

            if (extractFuel > 0) {
                ItemStackTemplate remainder = fuelStack.getCraftingRemainder();

                if (remainder != null) {
                    if (items.insert(ItemResource.of(remainder), remainder.count(), transaction) > 0) {
                        transaction.commit();
                    }
                }
            }
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CompressorMenu(containerId, inventory, this.dataAccess);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("galacticraftlegacy.block.compressor");
    }
}
