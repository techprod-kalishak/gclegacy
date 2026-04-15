/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.world.level.node.NodeNetwork;
import net.neoforged.neoforge.transfer.resource.Resource;

public interface ResourceTransmitter<N extends NodeNetwork, R extends Resource> extends TransmitterBlockEntity<N> {
    R getResource();
    int getAmount();

    int getCapacity();
}
