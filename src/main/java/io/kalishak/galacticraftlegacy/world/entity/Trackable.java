/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.world.level.telemetry.GloballyReferencedEntity;
import io.kalishak.galacticraftlegacy.world.level.telemetry.TelemetryTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.UniquelyIdentifyable;

public interface Trackable<StoredEntityType extends UniquelyIdentifyable> {
    Level level();
    GloballyReferencedEntity<StoredEntityType> asReference();

    boolean isNewTrackable();
    void markAsTracked();

    void transmitData(int[] data);

    void receiveData(int[] data, String[] str);

    void adjustDisplay(int[] data);

    default boolean addToTracker() {
        if (isNewTrackable() && level() instanceof ServerLevel serverLevel) {
            TelemetryTracker tracker = TelemetryTracker.get(serverLevel.getServer());

            tracker.addTrackable(this);
            markAsTracked();

            return true;
        }

        return false;
    }
}
