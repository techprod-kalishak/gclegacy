/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import net.neoforged.neoforge.transfer.fluid.FluidResource;

public interface ResourceTransmitter extends TransmitterBlockEntity {
    FluidResource getResource();
    int getAmount();

    int getCapacity();
}
