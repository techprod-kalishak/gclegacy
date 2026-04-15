/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.energy;

import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class AtomicItemAccessEnergyHandler extends InfiniteEnergyHandler {
    private final int extractLimit;

    public AtomicItemAccessEnergyHandler(int extractLimit) {
        super();
        this.extractLimit = extractLimit;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        return Math.min(amount, this.extractLimit);
    }
}
