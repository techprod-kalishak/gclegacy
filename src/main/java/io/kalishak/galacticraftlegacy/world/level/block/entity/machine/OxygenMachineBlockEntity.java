/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.transfer.capability.fluid.LimitedFluidStackResourceHandler;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.level.block.entity.NamedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public abstract class OxygenMachineBlockEntity extends AbstractMachineBlockEntity {
    public int oxygenPerTick;
    public float lastStoredOxygen;
    public static int timeSinceOxygenRequest;
    protected FluidStack tank;
    private final int oxygenCapacity;
    private final LimitedFluidStackResourceHandler tankHandler = new LimitedFluidStackResourceHandler(this.oxygenPerTick) {
        @Override
        protected FluidStack getStack() {
            return OxygenMachineBlockEntity.this.tank;
        }

        @Override
        protected void setStack(FluidStack stack) {
            OxygenMachineBlockEntity.this.tank = stack;
        }

        @Override
        protected int getCapacity(FluidResource resource) {
            return OxygenMachineBlockEntity.this.oxygenCapacity;
        }
    };

    public OxygenMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int oxygenCapacity) {
        super(type, pos, blockState);
        this.oxygenCapacity = oxygenCapacity;
    }

    private static void oxygenServerTick(ServerLevel level, BlockPos worldPosition, BlockState blockState, OxygenMachineBlockEntity blockEntity) {
        if (OxygenMachineBlockEntity.timeSinceOxygenRequest > 0)
        {
            OxygenMachineBlockEntity.timeSinceOxygenRequest--;
        }

        if (blockEntity.shouldUseOxygen()) {
            if (!blockEntity.tank.isEmpty()) {
                FluidStack stack = blockEntity.tank.copy();
                stack.setAmount(Math.max(blockEntity.tank.getAmount() - blockEntity.oxygenPerTick, 0));
                blockEntity.tank = stack;
            }
        }

        blockEntity.lastStoredOxygen = blockEntity.tank.getAmount();
    }

    public abstract boolean shouldUseOxygen();
}
