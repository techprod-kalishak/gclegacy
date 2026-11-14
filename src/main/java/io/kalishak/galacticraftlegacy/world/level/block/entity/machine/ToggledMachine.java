package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ToggledMachine {
    boolean isDisabled();

    void toggleGeneration();

    default void onToggle(Level level, BlockEntity blockEntity) {
        blockEntity.setChanged();
    }
}
