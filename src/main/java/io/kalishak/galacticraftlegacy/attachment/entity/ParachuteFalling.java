/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public interface ParachuteFalling {
    void onLand(Entity owner, BlockPos landedPos);

    void setFallingTicks(int ticks);

    int getFallingTicks();

    default boolean usesParachute() {
        return getFallingTicks() > 0;
    }
}
