package io.kalishak.galacticraftlegacy.transfer.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.function.Predicate;

public interface ResourceRequest<R extends Resource> {
    R request();
    int requestAmount();

    /**
     * @param committed false when request was not committed, true when was
     */
    void cancelRequest(boolean committed);

    boolean requestMatches(R resource, int amount);
    ResourceHandler<R> recreateSource(Level level, BlockPos pos);

    static <R extends Resource> void validateRequest(ResourceRequest<R> request) {
        TransferPreconditions.checkNonEmptyNonNegative(request.request(), request.requestAmount());
    }

    /**
     *
     * @param from source
     * @param to target
     * @param request object to transfer
     * @param transaction self ex
     * @return true when committed, false otherwise
     * @param <R> resource instance, can be {@link net.neoforged.neoforge.transfer.item.ItemResource} or {@link net.neoforged.neoforge.transfer.fluid.FluidResource}
     */
    static <R extends Resource> boolean handleRequest(ResourceHandler<R> from, ResourceHandler<R> to, ResourceRequest<R> request, Transaction transaction) {
        validateRequest(request);
        R buffer = ResourceHandlerUtil.findExtractableResource(from, requestFilter(request), transaction);

        if (buffer != null && !buffer.isEmpty()) {
            int moved = ResourceHandlerUtil.move(from, to, requestFilter(request), request.requestAmount(), transaction);

            if (moved > 0 && moved == request.requestAmount()) {
                request.cancelRequest(true);
                transaction.commit();
                return true;
            }
        }

        request.cancelRequest(false);
        return false;
    }

    private static <R extends Resource> Predicate<R> requestFilter(ResourceRequest<R> request) {
        return buffer -> request.requestMatches(buffer, request.requestAmount());
    }
}
