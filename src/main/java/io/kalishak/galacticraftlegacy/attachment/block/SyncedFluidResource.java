/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.block;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractFluidTankMachineBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SyncedFluidResource {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncedFluidResource> STREAM_CODEC = FluidStack.OPTIONAL_STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(SyncedFluidResource::new, syncedFluidResource -> syncedFluidResource.tanks);

    private final List<FluidStack> tanks;

    public SyncedFluidResource(List<FluidStack> tanks) {
        this.tanks = tanks;
    }

    public void updateFluidStack(int index, @NonNull FluidStack stack) {
        this.tanks.set(index, stack);
    }

    public void setAmount(int index, int amount) {
        FluidStack stack = this.tanks.get(index);

        if (stack == null || stack.isEmpty()) {
            return;
        }

        updateFluidStack(index, stack.copyWithAmount(amount));
    }

    public FluidStack getFluidStack(int index) {
        FluidStack stack = this.tanks.get(index);

        return stack != null ? stack : FluidStack.EMPTY;
    }

    public static SyncedFluidResource fromBlockEntity(IAttachmentHolder attachmentHolder) {
        if (!(attachmentHolder instanceof BlockEntity)) {
            throw new IllegalArgumentException(attachmentHolder.getClass() + " is not a BlockEntity");
        }

        if (attachmentHolder instanceof AbstractFluidTankMachineBlockEntity machine) {
            Level level = machine.getLevel();

            if (level != null) {
                ResourceHandler<FluidResource> fluidResourceHandler = level.getCapability(Capabilities.Fluid.BLOCK, machine.getBlockPos(), null);

                if (fluidResourceHandler != null) {
                    return new SyncedFluidResource(ResourcefulHelper.asList(fluidResourceHandler, FluidUtil::getStack));
                }
            }
        }

        return new SyncedFluidResource(List.of());
    }
}
