package io.kalishak.galacticraftlegacy.world.inventory;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.resource.Resource;
import org.jspecify.annotations.Nullable;

public interface SidedIndexedSource<R extends Resource> {
    default int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    default boolean canPlaceItemThroughFace(int index, R resource, @Nullable Direction direction) {
        return false;
    }

    default boolean canTakeItemThroughFace(int index, R resource, Direction direction) {
        return false;
    }
}
