/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.transfer.node.NodeNetwork;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

public interface NodeSourceBlockEntity {
    NodeNetwork getNetwork(@Nullable Direction side);
    boolean hasNetwork();
    void addNetwork(NodeNetwork network);

    void updateNetwork();
    void onNetworkUpdate();
}
