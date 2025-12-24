package io.kalishak.galacticraftlegacy.transfer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

public interface ResourcefulHelper {
    static int getRedstoneSignalFromBlockEntity(Level level, BlockPos pos, BlockState state, @Nullable Direction side) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity != null) {
            ResourceHandler<ItemResource> resourceHandler = level.getCapability(Capabilities.Item.BLOCK, pos, state, blockEntity, side);

            if (resourceHandler != null) {
                return ResourceHandlerUtil.getRedstoneSignalFromResourceHandler(resourceHandler);
            }
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    static <BE extends BlockEntity> BE readBlockEntity(BlockEntityType<BE> wantedType, Level level, RegistryFriendlyByteBuf data) {
        BlockEntity blockEntity = level.getBlockEntity(data.readBlockPos());

        if (blockEntity != null && blockEntity.getType().equals(wantedType)) {
            return (BE) blockEntity;
        }

        throw new IllegalArgumentException("BlockEntityType " + wantedType + " not found");
    }

    static ResourceHandler<ItemResource> getItemResourceHandler(BlockEntity blockEntity, @Nullable Direction side) {
        Level level = blockEntity.getLevel();

        if (level == null) {
            return EmptyResourceHandler.instance();
        }

        BlockPos pos = blockEntity.getBlockPos();
        BlockState state = level.getBlockState(pos);
        ResourceHandler<ItemResource> cap = level.getCapability(Capabilities.Item.BLOCK, pos, state, blockEntity, side);

        return cap != null ? cap :  EmptyResourceHandler.instance();
    }

    static <R extends Resource> int exchange(ResourceHandler<R> resourceHandler, int index, R newResource, int amount, TransactionContext tx) {
        TransferPreconditions.checkNonEmptyNonNegative(newResource, amount);
        R currentResource = resourceHandler.getResource(index);
        TransferPreconditions.checkNonEmpty(currentResource);

        try (Transaction childTx = Transaction.open(tx)) {
            int extracted = resourceHandler.extract(currentResource, amount, childTx);

            if (extracted > 0) {
                var inserted = resourceHandler.insert(newResource, extracted, childTx);

                if (inserted == extracted) {
                    childTx.commit();

                    return extracted;
                }
            }
        }

        return 0;
    }
}
