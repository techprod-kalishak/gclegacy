/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public interface CargoContainer {
    LoadingState addCargo(ItemStack stack, boolean simulate, @Nullable Transaction tx);

    Result removeCargo(boolean simulate, @Nullable Transaction tx);

    enum LoadingState {
        FULL,
        EMPTY,
        NO_TARGET,
        NO_INVENTORY,
        SUCCESS
    }

    record Result(LoadingState loadingState, ItemStack stack) {
        public static final Result EMPTY = new Result(LoadingState.NO_TARGET, ItemStack.EMPTY);
    }
}
