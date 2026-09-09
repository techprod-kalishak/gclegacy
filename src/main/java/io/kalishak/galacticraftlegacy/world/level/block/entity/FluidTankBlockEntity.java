/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import com.google.common.base.Predicates;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public class FluidTankBlockEntity extends BlockEntity {
    private final SingleTankResourceHandler tank = new SingleTankResourceHandler(16000);

    public FluidTankBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(GalacticraftBlockEntityType.FLUID_TANK.get(), worldPosition, blockState);
    }

    public static void serverTick(ServerLevel level, BlockPos blockPos, BlockState blockState, FluidTankBlockEntity blockEntity) {
        FluidStack ownStack = blockEntity.tank.getFluidStack();

        if (!ownStack.isEmpty()) {
            BlockEntity tankBelow = level.getBlockEntity(blockPos.below());

            if (tankBelow instanceof FluidTankBlockEntity anotherTank) {
                try (Transaction tx = Transaction.open(null)) {
                    if (ResourceHandlerUtil.move(blockEntity.tank, anotherTank.tank, resource -> resource.is(ownStack.typeHolder()), ownStack.getAmount(), tx) > 0) {
                        tx.commit();
                    }
                }
            }
        }
    }

    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                GalacticraftBlockEntityType.FLUID_TANK.get(),
                (entity, side) -> entity.tank
        );
    }

    public InteractionResult interactWithItem(ResourceHandler<FluidResource> fluidHandler, Level level, BlockPos pos, Player player) {
        FluidStack ownStack = getFluidStack();

        if (ownStack.isEmpty()) {
            FluidResource fluidResource = fluidHandler.getResource(0);

            if (fluidResource.isEmpty()) {
                return InteractionResult.PASS;
            }

            try (Transaction tx = Transaction.open(null)) {
                int moved = ResourceHandlerUtil.move(fluidHandler, this.tank, Predicates.alwaysTrue(), fluidHandler.getAmountAsInt(0), tx);

                if (moved > 0) {
                    tx.commit();
                    playSound(level, pos, fluidResource.getFluidType().getSound(SoundActions.BUCKET_EMPTY));

                    return InteractionResult.SUCCESS;
                }
            }
        } else if (fluidHandler.isValid(0, FluidResource.of(ownStack))) {
            try (Transaction tx = Transaction.open(null)) {
                int moved = ResourceHandlerUtil.move(this.tank, fluidHandler, Predicates.alwaysTrue(), ownStack.getAmount(), tx);

                if (moved > 0) {
                    tx.commit();
                    playSound(level, pos, ownStack.getFluidType().getSound(SoundActions.BUCKET_FILL));

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    private void playSound(Level level, BlockPos pos, @Nullable SoundEvent soundEvent) {
        if (soundEvent != null) {
            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public FluidStack getFluidStack() {
        return this.tank.getFluidStack();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.tank.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.tank.deserialize(input);
    }

    @Override
    public void saveWithoutMetadata(ValueOutput output) {
        saveAdditional(output);
    }
}
