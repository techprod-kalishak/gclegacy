package io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network;

import io.kalishak.galacticraftlegacy.world.level.node.NodeNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface TransmitterBlockEntity<N extends NodeNetwork> extends NetworkSourceBlockEntity<N>, NetworkConnectedBlockEntity {
    void updateNeighbouringTransmitters(Level level, BlockPos pos);
}
