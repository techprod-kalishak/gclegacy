/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.core.Direction;

public interface ConnectorBlockEntity {
    boolean canConnect(Direction direction, NetworkType networkType);

    static Direction getRelativeFace(Direction declared, Direction actual) {
        int data = declared.get2DDataValue() + actual.get2DDataValue();

        if (data > 3) {
            data -= 3;
        }

        return Direction.from2DDataValue(data);
    }
}
