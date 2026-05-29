/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NodeNetworkRegistry {
    private final Map<NetworkType, List<NodeNetwork>> NODES_IN_LEVEL = new TreeMap<>();

    static NodeNetworkRegistry getOrCreate(ServerLevel level, @Nullable BlockEntity blockEntity) {
        throw new NotImplementedException();
    }
}
