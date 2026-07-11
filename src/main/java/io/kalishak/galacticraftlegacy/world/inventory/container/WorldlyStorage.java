package io.kalishak.galacticraftlegacy.world.inventory.container;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public interface WorldlyStorage {
    int[] getSlotsForFace(Direction direction);

    boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction);

    boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction);
}
