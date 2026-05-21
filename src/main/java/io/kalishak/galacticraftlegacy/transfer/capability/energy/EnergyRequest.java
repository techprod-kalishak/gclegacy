package io.kalishak.galacticraftlegacy.transfer.capability.energy;

import io.kalishak.galacticraftlegacy.transfer.node.NodeNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;

public interface EnergyRequest {
    int request();

    void cancelRequest(boolean committed);
    boolean requestMatches(int amount);

    @NonNull EnergyHandler recreateFromSource(Level level, BlockPos pos);

    static void validateRequest(EnergyRequest request) {
        TransferPreconditions.checkNonNegative(request.request());
    }

    static boolean handleRequest(EnergyHandler from, EnergyHandler to, EnergyRequest request, Transaction transaction) {
        validateRequest(request);
        int buffer = from.extract(request.request(), transaction);

        if (request.requestMatches(buffer)) {
            int received = to.insert(buffer, transaction);

            if (request.requestMatches(received)) {
                transaction.commit();
                return true;
            }
        }

        return false;
    }

    static boolean findSource(NodeNetwork node, EnergyRequest request, Transaction transaction) {
        return false;
    }
}
