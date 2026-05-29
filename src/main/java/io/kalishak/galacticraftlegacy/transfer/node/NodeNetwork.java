/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class NodeNetwork {
    private static final Map<ResourceKey<Level>, NodeNetworkRegistry> NODE_NETWORKS = new LinkedHashMap<>();

    public abstract NetworkType getType();

    public static NodeNetworkRegistry getNetworkFor(ServerLevel serverLevel, @Nullable BlockEntity blockEntity) {
        return NODE_NETWORKS.computeIfAbsent(serverLevel.dimension(), _ -> NodeNetworkRegistry.getOrCreate(serverLevel, blockEntity));
    }
}
