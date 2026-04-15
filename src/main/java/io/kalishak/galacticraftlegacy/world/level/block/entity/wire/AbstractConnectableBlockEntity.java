/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.node.NodeNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public abstract class AbstractConnectableBlockEntity<N extends NodeNetwork> extends BlockEntity implements TransmitterBlockEntity<N> {
    protected @Nullable N network;
    protected Set<BlockEntity> surroundingBlockEntities;
    protected boolean isValid = true;

    protected AbstractConnectableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public N getNetwork() {
        if (!hasNetwork()) {
            resetNetwork();
        }

        return this.network;
    }

    @Override
    public boolean hasNetwork() {
        return this.network != null;
    }

    @Override
    public void onNetworkUpdate() {
    }

    protected abstract void resetNetwork();

    @Override
    public void updateNetwork() {
//        if (this.level != null && !this.level.isClientSide()) {
//            for (Direction direction : Direction.values()) {
//                BlockPos neighbourPos = getBlockPos().relative(direction);
//                BlockEntity neighbourBlockEntity = this.level.getBlockEntity(neighbourPos);
//
//                if (neighbourBlockEntity instanceof TransmitterBlockEntity neighbourTransmitter) {
//                    if (neighbourTransmitter.hasNetwork() && canConnect(direction.getOpposite(), neighbourTransmitter.getNetwork().getType())) {
//                        if (!hasNetwork()) {
//                            addNetwork(neighbourTransmitter.getNetwork());
//                            getNetwork().addConnection(this);
//                        } else if (!getNetwork().equals(neighbourTransmitter.getNetwork())) {
//                            addNetwork(getNetwork().merge(neighbourTransmitter.getNetwork()));
//                        }
//                    }
//                }
//            }
//        }
    }

    @Override
    public void updateNeighbouringTransmitters(Level level, BlockPos pos) {
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType networkType) {
       return false;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("IsValid", this.isValid);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.isValid = input.getBooleanOr("IsValid", true);
    }
}
