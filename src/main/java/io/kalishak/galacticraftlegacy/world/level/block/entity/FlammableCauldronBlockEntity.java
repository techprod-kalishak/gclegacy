/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.level.block.cauldron.FlammableCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.NonNull;

public class FlammableCauldronBlockEntity extends BlockEntity {
    private FluidStack stack = FluidStack.EMPTY;
    private final SingleTankResourceHandler handler = new SingleTankResourceHandler() {
        @Override
        public @NonNull FluidStack getFluidStack() {
            return FlammableCauldronBlockEntity.this.stack;
        }

        @Override
        public void setFluidStack(@NonNull FluidStack stack) {
            FlammableCauldronBlockEntity.this.stack = stack;
        }

        @Override
        public int getCapacity() {
            return FluidType.BUCKET_VOLUME;
        }

        @Override
        protected void notifyChange() {
            FlammableCauldronBlockEntity.this.refreshBlockState();
        }
    };

    public FlammableCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.FLAMMABLE_CAULDRON.get(), pos, blockState);
    }

    public FlammableCauldronBlockEntity(BlockPos pos, BlockState blockState, FluidStack initialStack) {
        this(pos, blockState);
        this.stack = initialStack;
    }

    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GalacticraftBlockEntityType.FLAMMABLE_CAULDRON.get(), (blockEntity, cxt) -> blockEntity.handler);
    }

    private void refreshBlockState() {
        Level level = getLevel();

        if (level != null) {
            int newAmount = this.stack.getAmount();
            boolean wasFull = getBlockState().getValue(FlammableCauldronBlock.FULL);

            if (wasFull && newAmount < FluidType.BUCKET_VOLUME) {
                level.setBlock(getBlockPos(), getBlockState().setValue(FlammableCauldronBlock.FULL, false), 3);
            } else if (newAmount == FluidType.BUCKET_VOLUME) {
                level.setBlock(getBlockPos(), getBlockState().setValue(FlammableCauldronBlock.FULL, true), 3);
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.handler.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.handler.deserialize(input);
    }
}
