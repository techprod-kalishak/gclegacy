/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.level.block.entity.AbstractPadBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.telemetry.TelemetryTracker;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.AbstractAutoRocket;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LaunchControllerBlockEntity extends BlockEntity {
    private int targetFrequency = -1;

    public LaunchControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.LAUNCH_CONTROLLER.get(), pos, blockState);
    }

    @Override
    public void setRemoved() {
        if (this.level instanceof ServerLevel serverLevel) {
            TelemetryTracker tracker = TelemetryTracker.get(serverLevel.getServer());

            if (tracker != null) {
                tracker.removeLaunchControllerAt(this.level.dimension(), getBlockPos());
            }
        }

        super.setRemoved();
    }

    public boolean receivesRedstoneSignal(Level level, BlockPos pos) {
        return false;
    }

    public boolean canAutoLaunch() {
        return false;
    }

    public void setTargetFrequency(int targetFrequency) {
        this.targetFrequency = targetFrequency;
        setChanged();
    }

    public int getTargetFrequency() {
        return this.targetFrequency;
    }

    public AbstractAutoRocket.AutoLaunchState getAutoLaunchState() {
        return AbstractAutoRocket.AutoLaunchState.CARGO_IS_FULL;
    }

    public void setAttachedPad(AbstractPadBlockEntity abstractPadBlockEntity) {

    }
}
