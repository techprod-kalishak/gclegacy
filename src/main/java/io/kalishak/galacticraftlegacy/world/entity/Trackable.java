/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.world.level.savedata.TelemetryTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface Trackable {
    Level level();
    UUID getUUID();

    boolean isNewTrackable();
    void markAsTracked();

    void transmitData(int[] data);

    void receiveData(int[] data, String[] str);

    void adjustDisplay(int[] data);

    default boolean addToTracker() {
        if (isNewTrackable() && level() instanceof ServerLevel serverLevel) {
            TelemetryTracker tracker = serverLevel.getDataStorage().get(TelemetryTracker.SAVE_DATA_ID);

            if (tracker != null) {
                tracker.add(this);
                markAsTracked();

                return true;
            }
        }

        return false;
    }
}
