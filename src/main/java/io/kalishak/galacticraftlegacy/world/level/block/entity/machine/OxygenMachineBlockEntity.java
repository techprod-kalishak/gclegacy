/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.transfer.capability.fluid.LimitedFluidResourceHandler;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class OxygenMachineBlockEntity extends AbstractMachineBlockEntity {
    public int oxygenPerTick;
    public float lastStoredOxygen;
    public static int timeSinceOxygenRequest;
    protected final SingleTankResourceHandler tankHandler = new LimitedFluidResourceHandler(getProperties().getTankCapacity(), getProperties().getFluidExtractionRate(), getProperties().getFluidInsertionRate());;

    public OxygenMachineBlockEntity(BlockEntityType<?> type, MachineInstance.Properties properties, BlockPos pos, BlockState blockState, int oxygenCapacity) {
        super(type, pos, blockState);
    }

    private static void oxygenServerTick(ServerLevel level, BlockPos worldPosition, BlockState blockState, OxygenMachineBlockEntity blockEntity) {
        if (OxygenMachineBlockEntity.timeSinceOxygenRequest > 0)
        {
            OxygenMachineBlockEntity.timeSinceOxygenRequest--;
        }

        if (blockEntity.shouldUseOxygen()) {
            if (!blockEntity.tankHandler.getFluidStack().isEmpty()) {
                FluidStack stack = blockEntity.tankHandler.getFluidStack();
                stack.setAmount(Math.max(blockEntity.tankHandler.getAmount() - blockEntity.oxygenPerTick, 0));
                blockEntity.tankHandler.setFluidStack(stack);
            }
        }

        blockEntity.lastStoredOxygen = blockEntity.tankHandler.getAmount();
    }

    public abstract boolean shouldUseOxygen();
}
