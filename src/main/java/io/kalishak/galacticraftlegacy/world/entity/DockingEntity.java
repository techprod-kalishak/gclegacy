/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.FuelableDock;
import net.minecraft.world.level.entity.UniquelyIdentifyable;

public interface DockingEntity extends UniquelyIdentifyable, CargoContainer {
    void setPad(FuelableDock pad);

    FuelableDock getPad();

    void onPadDestroyed();

    boolean isDockValid(FuelableDock dock);

    boolean inFlight();
}
