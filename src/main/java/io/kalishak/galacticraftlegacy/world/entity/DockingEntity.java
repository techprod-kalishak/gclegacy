package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.FuelableDock;

public interface DockingEntity extends CargoContainer {
    void setPad(FuelableDock pad);

    FuelableDock getPad();

    void onPadDestroyed();

    boolean isDockValid(FuelableDock dock);

    boolean inFlight();
}
