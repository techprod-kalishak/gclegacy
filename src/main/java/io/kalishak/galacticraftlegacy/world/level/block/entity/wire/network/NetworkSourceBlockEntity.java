/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.transfer.node.FluidNodeNetwork;

public interface NetworkSourceBlockEntity {
    FluidNodeNetwork getNetwork();

    boolean hasNetwork();

    void addNetwork(FluidNodeNetwork network);
}
