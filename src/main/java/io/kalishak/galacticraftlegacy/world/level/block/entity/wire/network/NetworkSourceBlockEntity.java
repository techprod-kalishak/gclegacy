/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.world.level.node.NodeNetwork;

public interface NetworkSourceBlockEntity<N extends NodeNetwork> {
    N getNetwork();

    boolean hasNetwork();

    void addNetwork(N network);
}
