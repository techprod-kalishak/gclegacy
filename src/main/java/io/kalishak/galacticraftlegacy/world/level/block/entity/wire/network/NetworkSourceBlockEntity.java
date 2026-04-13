package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.world.level.node.NodeNetwork;

public interface NetworkSourceBlockEntity<N extends NodeNetwork> {
    N getNetwork();

    boolean hasNetwork();

    void addNetwork(N network);
}
