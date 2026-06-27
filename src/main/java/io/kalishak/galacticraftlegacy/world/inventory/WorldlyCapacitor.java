/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import net.minecraft.core.Direction;

public interface WorldlyCapacitor {
    SlotType getSlotForFace(Direction direction);

    boolean canReceive(int amount, int transferRate, Direction direction);

    boolean canExtract(int amount, int transferRate, Direction direction);

    enum SlotType {
        INPUT,
        OUTPUT,
        NONE
    }
}
