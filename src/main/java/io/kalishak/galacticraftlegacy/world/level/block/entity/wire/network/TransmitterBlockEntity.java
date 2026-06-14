/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ConnectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface TransmitterBlockEntity extends NodeSourceBlockEntity, ConnectorBlockEntity {
    void updateNeighbouringTransmitters(Level level, BlockPos pos);
}
