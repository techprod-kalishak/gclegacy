package io.kalishak.galacticraftlegacy.world.level.block.wire;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.core.Direction;
import org.jspecify.annotations.NonNull;

public interface ConnectingBlock {
    @NonNull NetworkType getNetworkType(@NonNull Direction direction);
}
