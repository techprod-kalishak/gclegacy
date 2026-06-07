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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import org.jspecify.annotations.NonNull;

public abstract class AbstractFluidTankMachineBlockEntity extends AbstractMachineBlockEntity {
    protected final FluidStacksResourceHandler tanks = new FluidStacksResourceHandler(tankSize(), tankCapacity()) {
        @Override
        protected void onContentsChanged(int index, FluidStack previousContents) {
            AbstractFluidTankMachineBlockEntity.this.getExistingData(GalacticraftAttachments.SYNC_FLUID_STORAGE).ifPresentOrElse(syncedFluidResource -> {
                syncedFluidResource.updateFluidStack(index, previousContents);
            }, () -> {
                AbstractFluidTankMachineBlockEntity.this.setData(GalacticraftAttachments.SYNC_FLUID_STORAGE, new SyncedFluidResource(ResourcefulHelper.orderedHandlerCopy(AbstractFluidTankMachineBlockEntity.this.tanks, FluidStack.EMPTY, FluidResource::toStack)));
            });
        }
    };

    protected AbstractFluidTankMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected static <M extends AbstractFluidTankMachineBlockEntity> void registerFluidEnergyHandler(@NonNull Direction direction, BlockEntityType<M> type, RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                type,
                (machine, cxt) -> {
                    if (cxt == null || cxt == direction) {
                        return machine.tanks;
                    }

                    return new DelegatingResourceHandler<>(machine.tanks);
                }
        );
    }

    protected abstract int tankSize();
    protected abstract int tankCapacity();

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.level != null && !this.level.isClientSide()) {
            AbstractFluidTankMachineBlockEntity.this.setData(GalacticraftAttachments.SYNC_FLUID_STORAGE, new SyncedFluidResource(ResourcefulHelper.orderedHandlerCopy(AbstractFluidTankMachineBlockEntity.this.tanks, FluidStack.EMPTY, FluidResource::toStack)));
        }
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
