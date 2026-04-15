/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.wire;

import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.core.Direction;
import org.jspecify.annotations.NonNull;

public interface ConnectingBlock {
    @NonNull NetworkType getNetworkType(@NonNull Direction direction);
}
