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
