package io.kalishak.galacticraftlegacy.transfer.node.object;

import net.minecraft.core.Direction;

public interface OxygenConsumer {
    boolean shouldConsumeOxygen();
    int getRequest(Direction side);
    int getProduction(Direction side);
}
