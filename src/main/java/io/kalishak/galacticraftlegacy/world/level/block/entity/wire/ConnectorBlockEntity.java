package io.kalishak.galacticraftlegacy.world.level.block.entity.wire;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.core.Direction;

public interface ConnectorBlockEntity {
    boolean canConnect(Direction direction, NetworkType networkType);
}
