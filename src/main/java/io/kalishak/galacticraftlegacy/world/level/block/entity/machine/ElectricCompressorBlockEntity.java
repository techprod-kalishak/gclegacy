/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.ElectricCompressorMenu;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public class ElectricCompressorBlockEntity extends AbstractCompressorBlockEntity {
    private final SimpleEnergyHandler capacitor = new SimpleEnergyHandler(25000, 250);
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case 0 -> ElectricCompressorBlockEntity.this.compressingTimer;
                case 1 -> ElectricCompressorBlockEntity.this.compressingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case 0 -> ElectricCompressorBlockEntity.this.compressingTimer = value;
                case 1 -> ElectricCompressorBlockEntity.this.compressingTotalTime = value;
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

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, ElectricCompressorBlockEntity compressor) {
        AbstractMachineBlockEntity.energyTransferTick(compressor.innerResourceHandler, compressor.capacitor, compressor.compressingTimer > 0, false, AbstractMachineBlockEntity.BASIC_MACHINE_MAX_TRANSFER_RATE, 11, 250, null);
        boolean changed = false;

        int currentEnergy = compressor.capacitor.getAmountAsInt();
        NonNullList<ItemStack> ingredients = NonNullList.copyOf(compressor.items.subList(0, 9));

        boolean hasIngredients = !ingredients.isEmpty();
        boolean hasFuel = currentEnergy > 0;

        if (hasFuel) {
            if (hasIngredients) {
                ResourceHandler<ItemResource> ingredientsHandler = new ItemStacksResourceHandler(ingredients);
                CompressingRecipeInput input = new CompressingRecipeInput(3, 3, () -> ingredientsHandler);
                RecipeHolder<? extends CompressingRecipe> recipe = compressor.quickCheck.getRecipeFor(input, level).orElse(null);

                if (recipe != null) {
                    try (Transaction tx = Transaction.open(null)) {
                        if (compressor.capacitor.extract(25, tx) > 0) {
                            tx.commit();
                        }
                    }

                    ItemStack recipeResult = recipe.value().assemble(input);
                    ItemResource resourceInResultSlot = compressor.innerResourceHandler.getResource(10);
                    int maxStackSize = compressor.innerResourceHandler.getCapacityAsInt(10, resourceInResultSlot);

                    if (!recipeResult.isEmpty() && canCompress(compressor.innerResourceHandler, maxStackSize, recipeResult)) {
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
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.capacitor.deserialize(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.capacitor.serialize(output);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        this.capacitor.set(componentGetter.getOrDefault(GalacticraftDataComponents.STORED_ENERGY, 0));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(GalacticraftDataComponents.STORED_ENERGY, this.capacitor.getAmountAsInt());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("galacticraftlegacy.block.electric_compressor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ElectricCompressorMenu(containerId, inventory, this.dataAccess);
    }
}
