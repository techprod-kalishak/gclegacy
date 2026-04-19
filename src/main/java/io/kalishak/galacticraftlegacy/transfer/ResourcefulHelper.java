/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.VoidingResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EmptyEnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.VoidingEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.DataComponentHolderResource;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

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

    static <R extends Resource> ResourceHandler<R> getResourceHandler(BlockCapability<ResourceHandler<R>, @Nullable Direction> capability, R emptyResource, BlockEntity blockEntity, @Nullable Direction side) {
        Level level = blockEntity.getLevel();

        if (level == null) {
            return new VoidingResourceHandler<>(emptyResource);
        }

        BlockPos pos = blockEntity.getBlockPos();
        BlockState state = level.getBlockState(pos);
        ResourceHandler<R> cap = level.getCapability(capability, pos, state, blockEntity, side);

        return cap != null ? cap : EmptyResourceHandler.instance();
    }

    static EnergyHandler getEnergyHandler(BlockEntity blockEntity, @Nullable Direction side) {
        Level level = blockEntity.getLevel();

        if (level == null) {
            return VoidingEnergyHandler.INSTANCE;
        }

        BlockPos pos = blockEntity.getBlockPos();
        BlockState state = level.getBlockState(pos);
        EnergyHandler cap = level.getCapability(Capabilities.Energy.BLOCK, pos, state, blockEntity, side);

        return cap != null ? cap : EmptyEnergyHandler.INSTANCE;
    }

    static <R extends Resource, S> boolean handlerMatches(ResourceHandler<R> resourceHandler, ResourceHandler<R> otherResourceHandler, BiFunction<ResourceHandler<R>, Integer, S> toStack, BiPredicate<S, S> stackComparator) {
        if (resourceHandler.size() != otherResourceHandler.size()) {
            return false;
        }

        for (int i = 0; i < resourceHandler.size(); i++) {
            S stack = toStack.apply(resourceHandler, i);
            S otherStack = toStack.apply(otherResourceHandler, i);

            if (!stackComparator.test(stack, otherStack)) {
                return false;
            }
        }

        return true;
    }

    static <R extends Resource, S> int hashResourceHandler(ResourceHandler<R> resourceHandler, BiFunction<ResourceHandler<R>, Integer, S> toStack, ToIntFunction<S> stackHashGetter) {
        int hash = 0;

        for (int i = 0; i < resourceHandler.size(); i++) {
            hash = hash * 31 + stackHashGetter.applyAsInt(toStack.apply(resourceHandler, i));
        }

        return hash;
    }

    static <R extends Resource, S> int clearOrCountMatching(ResourceHandler<R> resourceHandler, BiFunction<ResourceHandler<R>, Integer, S> transformer, Predicate<S> stackPredicate, int maxAmount, boolean simulate) {
        int clearedItems = 0;

        for (int i = 0; i <= resourceHandler.size(); i++) {
            int submitted = clearOrCountMatching(resourceHandler, transformer, i, stackPredicate, maxAmount - clearedItems, simulate);
            R resource = resourceHandler.getResource(i);

            if (submitted > 0 && !simulate && !resource.isEmpty()) {
                try (Transaction tx = Transaction.open(null)) {
                    if (resourceHandler.extract(i, resource, resourceHandler.getAmountAsInt(i), tx) > 0) {
                        tx.commit();

                        clearedItems += submitted;
                    }
                }
            }
        }

        return clearedItems;
    }

    static <R extends Resource, S> int clearOrCountMatching(ResourceHandler<R> resourceHandler, BiFunction<ResourceHandler<R>, Integer, S> transformer, int index, Predicate<S> predicate, int maxAmount, boolean simulate) {
        R resource = resourceHandler.getResource(index);

        if (resource.isEmpty() || !predicate.test(transformer.apply(resourceHandler, index))) {
            return 0;
        } else if (simulate) {
            return resourceHandler.getAmountAsInt(index);
        }

        int amount = resourceHandler.getAmountAsInt(index);
        int i = maxAmount < 0 ? amount : Math.min(amount, maxAmount);

        try (Transaction tx = Transaction.open(null)) {
            if (resourceHandler.extract(resource, i, tx) > 0) {
                tx.commit();

                return i;
            }
        }

        return 0;
    }

    static ItemResource fillTank(ResourceHandler<FluidResource> tank, Predicate<FluidResource> extractedFluidPredicate, ItemStack emptyTank, @Nullable Transaction tx) {
        ResourceHandler<FluidResource> itemTank = emptyTank.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(emptyTank));

        if (itemTank != null) {
            boolean isBucket = emptyTank.is(Tags.Items.BUCKETS_EMPTY);

            try (Transaction childTx = Transaction.open(tx)) {
                FluidResource toExtract = ResourceHandlerUtil.findExtractableResource(tank, extractedFluidPredicate, childTx);
                int maxAmount = isBucket ? FluidType.BUCKET_VOLUME : tank.getAmountAsInt(0);

                if (toExtract != null) {
                    int extracted = tank.extract(toExtract, maxAmount, childTx);

                    if (isBucket && extracted == FluidType.BUCKET_VOLUME) {
                        childTx.commit();
                        return ItemResource.of(toExtract.getFluid().getBucket());
                    } else if (!isBucket) {
                        int filled = itemTank.insert(toExtract, extracted, childTx);

                        if (filled == extracted) {
                            childTx.commit();
                        }
                    }
                }
            }
        }

        return ItemResource.of(emptyTank);
    }

    static <R extends Resource, S> boolean areResourcesEqual(R resource, R otherResource, int resourceCount, int otherResourceCount, BiFunction<R, Integer, S> stacker, BiPredicate<S, S> stackComparator) {
        if (resource.isEmpty() && otherResource.isEmpty()) {
            return true;
        } else if (resource.isEmpty() || otherResource.isEmpty()) {
            return false;
        }

        return stackComparator.test(stacker.apply(resource, resourceCount), stacker.apply(otherResource, otherResourceCount));
    }

    static <R extends Resource, S> boolean areResourcesEqual(R resource, R otherResource, Function<R, S> stacker, BiPredicate<S, S> stackComparator) {
        return areResourcesEqual(resource, otherResource, 1, 1, (r, i) -> stacker.apply(r), stackComparator);
    }

    static <R extends Resource, S> List<S> asList(ResourceHandler<R> resourceHandler, BiFunction<ResourceHandler<R>, Integer, S> stacker) {
        List<S> resources = new ArrayList<>(resourceHandler.size());

        for (int i = 0; i < resourceHandler.size(); i++) {
            resources.set(i, stacker.apply(resourceHandler, i));
        }

        return resources;
    }
}
