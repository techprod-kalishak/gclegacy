/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.entity.CargoContainer;
import io.kalishak.galacticraftlegacy.world.entity.DockingEntity;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.AbstractAutoRocket;
import io.kalishak.galacticraftlegacy.world.level.block.MultipartBlock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.FuelableDock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.LandingPad;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.LaunchControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPadBlockEntity extends MultipartBlockEntity implements MultipartBlock, FuelableDock, CargoContainer {
    protected @Nullable EntityReference<DockingEntity> dockedVehicle;
    protected SingleTankResourceHandler fluid = new SingleTankResourceHandler(AbstractAutoRocket.FUEL_CAPACITY);

    public AbstractPadBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    protected void applyConnection(int xOffset, int zOffset, Collection<LandingPad> landingPads) {
        BlockPos offsetPos = this.worldPosition.offset(xOffset, 0, zOffset);
        BlockState offsetState = this.level.getBlockState(offsetPos);

        if (offsetState.is(getBlockState().typeHolder())) {
            BlockEntity blockEntity = this.level.getBlockEntity(offsetPos);

            if (blockEntity instanceof LandingPad landingPad && landingPad.canAttachTo(this.level, this.worldPosition)) {
                landingPads.add(landingPad);

                if (blockEntity instanceof LaunchControllerBlockEntity launchController) {
                    launchController.setAttachedPad(this);
                }
            }
        }
    }

    @Override
    public boolean onActivation(Player player) {
        return true;
    }

    @Override
    public void onCreation(Level level, BlockPos placedPosition) {
        this.coreBlockPosition = placedPosition;
        setChanged();
    }

    @Override
    public void updatePositions(BlockPos placedPosition, Set<BlockPos> otherPositions) {
        int y = placedPosition.getY();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if (x == 0 && z == 0)
                    continue;
                otherPositions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onRemoval(BlockEntity caller) {
        Set<BlockPos> positions = new HashSet<>();
        updatePositions(this.worldPosition, positions);

        for (BlockPos pos : positions) {
            BlockState otherState = this.level.getBlockState(pos);

            if (otherState.is(getBlockState().typeHolder())) {
                this.level.destroyBlock(pos, false);
            }
        }

        this.level.destroyBlock(this.worldPosition, true);

        if (this.dockedVehicle != null) {
            DockingEntity entity = this.dockedVehicle.getEntity(this.level, DockingEntity.class);

            if (entity != null) {
                entity.onPadDestroyed();
                this.dockedVehicle = null;
            }
        }
    }

    @Override
    public Set<LandingPad> getFuelPads() {
        Set<LandingPad> landingPads = new HashSet<>();

        for (int x = this.worldPosition.getX() - 1; x < this.worldPosition.getX() + 2; x++) {
            applyConnection(x, this.worldPosition.getZ() - 2, landingPads);
            applyConnection(x, this.worldPosition.getZ() + 2, landingPads);
        }

        for (int z = this.worldPosition.getZ() - 1; z < this.worldPosition.getZ() + 2; z++) {
            applyConnection(this.worldPosition.getX() - 2, z, landingPads);
            applyConnection(this.worldPosition.getX() + 2, z, landingPads);
        }

        return landingPads;
    }

    @Override
    public LoadingState addCargo(ItemStack stack, boolean simulate, @Nullable Transaction tx) {
        DockingEntity entity = getDockedEntity();

        if (entity != null) {
            return entity.addCargo(stack, simulate, tx);
        }

        return LoadingState.NO_TARGET;
    }

    @Override
    public Result removeCargo(boolean simulate, @Nullable Transaction tx) {
        DockingEntity entity = getDockedEntity();

        if (entity != null) {
            return entity.removeCargo(simulate, tx);
        }

        return Result.EMPTY;
    }

    @Override
    public boolean canAttachTo(LevelReader level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof LandingPad landingPad) {
            return landingPad.canAttachTo(level, this.worldPosition);
        }

        return false;
    }

    @Override
    public @Nullable DockingEntity getDockedEntity() {
        return this.dockedVehicle.getEntity(this.level, DockingEntity.class);
    }

    @Override
    public void dock(@Nullable DockingEntity dockedEntity) {
        if (dockedEntity != null) {
            this.dockedVehicle = EntityReference.of(dockedEntity);
        }
    }
}
