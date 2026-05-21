/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NetworkExplorer {
    public final Level level;
    public final BlockPos initialPosition;
    private final NetworkType networkType;
    private final ResourceKey<Level> levelKey;
    private final BlockPos ignoredPosition;

    private final Set<BlockPos> checkedPositions = new HashSet<>();
    public List<TransmitterBlockEntity> foundBlocks = new LinkedList<>();

    public NetworkExplorer(Level level, BlockPos initialPosition, BlockPos ignoredPosition, NetworkType networkType) {
        this.level = level;
        this.initialPosition = initialPosition;
        this.ignoredPosition = ignoredPosition;
        this.networkType = networkType;
        this.levelKey = level.dimension();
    }

    private void loopAll(int x, int y, int z, Direction direction) {
        for (Direction face : Direction.values()) {
            if (face == direction) {
                continue;
            }

            BlockPos target = new BlockPos(x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ());

            if (!this.checkedPositions.contains(target)) {
                this.checkedPositions.add(target);

                BlockEntity b = this.level.getBlockEntity(target);

                if (b instanceof TransmitterBlockEntity transmitterBlock && transmitterBlock.canConnect(direction, this.networkType)) {
                    this.foundBlocks.add(transmitterBlock);
                    loopAll(target.getX(), target.getY(), target.getZ(), face);
                }
            }
        }
    }

    public List<TransmitterBlockEntity> explore() {
        if (this.level.getBlockEntity(this.initialPosition) instanceof TransmitterBlockEntity transmitterBlockEntity) {
            this.checkedPositions.add(this.initialPosition);
            this.checkedPositions.add(this.ignoredPosition);
            this.foundBlocks.add(transmitterBlockEntity);

            loopAll(this.initialPosition.getX(), this.initialPosition.getY(), this.initialPosition.getZ(), Direction.EAST);
        }

        return this.foundBlocks;
    }
}
