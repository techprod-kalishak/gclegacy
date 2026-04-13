package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ConnectorBlockEntity;

public interface NetworkConnectedBlockEntity extends ConnectorBlockEntity {
    void updateNetwork();
    void onNetworkUpdate();
}
