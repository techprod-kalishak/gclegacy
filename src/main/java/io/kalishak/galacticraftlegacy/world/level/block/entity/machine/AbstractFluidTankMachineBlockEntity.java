/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedFluidResource;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.Tank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

public abstract class AbstractFluidTankMachineBlockEntity extends AbstractMachineBlockEntity implements Tank {
    protected final FluidStacksResourceHandler tanks = new FluidStacksResourceHandler(getTanks(), getMaxFluidAmount());

    protected AbstractFluidTankMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.level != null && !this.level.isClientSide()) {
            AbstractFluidTankMachineBlockEntity.this.setData(GalacticraftAttachments.SYNC_FLUID_STORAGE, new SyncedFluidResource(ResourcefulHelper.orderedHandlerCopy(AbstractFluidTankMachineBlockEntity.this.tanks, FluidStack.EMPTY, FluidResource::toStack)));
        }
    }

    @Override
    public void onTankChange(int index, FluidStack previousContents) {
        AbstractFluidTankMachineBlockEntity.this.getExistingData(GalacticraftAttachments.SYNC_FLUID_STORAGE).ifPresentOrElse(syncedFluidResource -> {
            syncedFluidResource.updateFluidStack(index, previousContents);
        }, () -> {
            AbstractFluidTankMachineBlockEntity.this.setData(GalacticraftAttachments.SYNC_FLUID_STORAGE, new SyncedFluidResource(ResourcefulHelper.orderedHandlerCopy(AbstractFluidTankMachineBlockEntity.this.tanks, FluidStack.EMPTY, FluidResource::toStack)));
        });
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.tanks.deserialize(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.tanks.serialize(output);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        ResourcefulHelper.applyTankComponent(components, this.tanks::set);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        ResourcefulHelper.collectTankComponent(components, this.tanks);
    }
}
