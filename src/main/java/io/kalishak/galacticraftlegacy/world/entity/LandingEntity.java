/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import net.minecraft.core.BlockPos;

public interface LandingEntity extends DockingEntity {
    void onLand(BlockPos pos);
}
