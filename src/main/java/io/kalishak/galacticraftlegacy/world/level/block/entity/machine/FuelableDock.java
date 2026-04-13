package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.entity.DockingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public interface FuelableDock {
    Set<LandingPad> getFuelPads();

    boolean canAttachTo(LevelReader level, BlockPos pos);

    @Nullable DockingEntity getDockedEntity();

    void dock(@Nullable DockingEntity dockedEntity);
}
