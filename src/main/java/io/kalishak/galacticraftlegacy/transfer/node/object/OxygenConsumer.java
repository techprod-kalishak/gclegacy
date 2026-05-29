/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node.object;

import net.minecraft.core.Direction;

public interface OxygenConsumer {
    boolean shouldConsumeOxygen();
    int getRequest(Direction side);
    int getProduction(Direction side);
}
