/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ConnectorBlockEntity;

public interface NodeAcceptingBlockEntity extends ConnectorBlockEntity {
    void updateNetwork();
    void onNetworkUpdate();
}
