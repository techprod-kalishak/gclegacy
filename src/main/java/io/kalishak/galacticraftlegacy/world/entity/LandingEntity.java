package io.kalishak.galacticraftlegacy.world.entity;

import net.minecraft.core.BlockPos;

public interface LandingEntity extends DockingEntity {
    void onLand(BlockPos pos);
}
