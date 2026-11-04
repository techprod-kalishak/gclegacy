/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public interface EnergyProducer {
    default boolean produceEnergy(EnergyHandler energyHandler, int amount, @Nullable Transaction tx) {
        try (Transaction transaction = Transaction.open(tx)) {
            if (energyHandler.insert(amount, transaction) > 0) {
                transaction.commit();

                return true;
            }
        }

        return false;
    }
}
