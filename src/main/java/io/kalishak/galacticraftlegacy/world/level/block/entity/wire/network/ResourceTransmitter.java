package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.world.level.node.NodeNetwork;
import net.neoforged.neoforge.transfer.resource.Resource;

public interface ResourceTransmitter<N extends NodeNetwork, R extends Resource> extends TransmitterBlockEntity<N> {
    R getResource();
    int getAmount();

    int getCapacity();
}
