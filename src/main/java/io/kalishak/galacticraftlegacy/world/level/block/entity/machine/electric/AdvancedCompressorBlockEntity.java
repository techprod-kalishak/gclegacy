/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric;

import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ElectricCompressingRecipe;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AlloyCompressor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class AdvancedCompressorBlockEntity extends ElectricCompressorBlockEntity {
    public AdvancedCompressorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityTypes.ADVANCED_COMPRESSOR.get(), pos, blockState);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, AdvancedCompressorBlockEntity entity) {
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
                        AlloyCompressor.compress(RangedResourceHandler.of(() -> entity.items, CRAFTING_SLOT_START, CRAFTING_SLOT_END + 1), recipeResult, 2);
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
}
