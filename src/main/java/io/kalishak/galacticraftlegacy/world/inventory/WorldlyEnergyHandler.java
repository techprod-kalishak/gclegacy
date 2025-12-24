package io.kalishak.galacticraftlegacy.world.inventory;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.energy.DelegatingEnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

public class WorldlyEnergyHandler implements EnergyHandler {
    private final SidedSource sidedSource;
    private final @Nullable Direction side;
    private final EnergyHandler energyHandler;

    public WorldlyEnergyHandler(SidedSource sidedSource, @Nullable Direction side, EnergyHandler energyHandler) {
        this.sidedSource = sidedSource;
        this.side = side;
        this.energyHandler = new DelegatingEnergyHandler(() -> energyHandler);
    }

    @Override
    public long getAmountAsLong() {
        return this.energyHandler.getAmountAsLong();
    }

    @Override
    public long getCapacityAsLong() {
        return this.energyHandler.getCapacityAsLong();
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        if (this.side != null && this.sidedSource.getInputDirection() == this.side) {
            return this.energyHandler.insert(amount, transaction);
        }

        return 0;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        if (this.side != null && this.sidedSource.getOutputDirection() == this.side) {
            return this.energyHandler.extract(amount, transaction);
        }

        return 0;
    }

    public interface SidedSource {
        default @Nullable Direction getInputDirection() {
            return null;
        }

        default @Nullable Direction getOutputDirection() {
            return null;
        }
    }
}
