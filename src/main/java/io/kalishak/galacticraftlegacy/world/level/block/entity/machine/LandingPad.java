/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public interface LandingPad {
    boolean canAttachTo(BlockGetter level, BlockPos pos);

    boolean isControlEnabled();
}
