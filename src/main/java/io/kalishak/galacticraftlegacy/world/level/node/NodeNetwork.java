package io.kalishak.galacticraftlegacy.world.level.node;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public interface NodeNetwork {
    NetworkType getType();

    Set<BlockEntity> getConnections();

    NodeNetwork merge(NodeNetwork other);

    void split(BlockEntity connection);

    void update();
}
