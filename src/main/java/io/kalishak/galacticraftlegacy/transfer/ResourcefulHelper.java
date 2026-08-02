/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer;

import io.kalishak.galacticraftlegacy.world.item.component.FluidTankContents;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.*;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EmptyEnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.VoidingEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.*;

public interface ResourcefulHelper {
    /** Items */

    static void populatePlayerFromContainer(Player player, ResourceHandler<ItemResource> resourceHandler) {
        for(int i = 0; i < resourceHandler.size(); ++i) {
            dropOrPlaceInInventory(player, ItemUtil.getStack(resourceHandler, i));
        }
    }

    static void dropOrPlaceInInventory(Player player, ItemStack stack) {
        boolean playerRemovedNotChangingDimension = player.isRemoved() && player.getRemovalReason() != Entity.RemovalReason.CHANGED_DIMENSION;
        boolean serverPlayerHasDisconnected = player instanceof ServerPlayer serverPlayer && serverPlayer.hasDisconnected();

        if (playerRemovedNotChangingDimension || serverPlayerHasDisconnected) {
            player.drop(stack, false);
        } else if (player instanceof ServerPlayer) {
            player.getInventory().placeItemBackInInventory(stack);
        }
    }

    static void applyTankComponent(DataComponentGetter components, IndexModifier<FluidResource> modifier) {
        FluidTankContents contents = components.get(GalacticraftDataComponents.FLUID_TANK_CONTENTS);

        if (contents == null) {
            return;
        }

        NonNullList<FluidStack> fluids = NonNullList.withSize(contents.getSlots(), FluidStack.EMPTY);
        contents.copyInto(fluids);

        for (int i = 0; i < fluids.size(); i++) {
            FluidStack stack = fluids.get(i);
            modifier.set(i, FluidResource.of(stack), stack.getAmount());
        }
    }

    static void applyContainerComponent(DataComponentGetter components, IndexModifier<ItemResource> modifier) {
        ItemContainerContents contents = components.get(DataComponents.CONTAINER);

        if (contents == null) {
            return;
        }

        NonNullList<ItemStack> items = NonNullList.withSize(contents.getSlots(), ItemStack.EMPTY);
        contents.copyInto(items);

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            modifier.set(i, ItemResource.of(stack), stack.count());
        }
    }

    static void collectContainerComponent(DataComponentMap.Builder components, ResourceHandler<ItemResource> items) {
        NonNullList<ItemStack> stacks = nonNullList(items, ItemStack.EMPTY, ItemResource::toStack);

        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(stacks));
    }

    static void collectTankComponent(DataComponentMap.Builder components, ResourceHandler<FluidResource> fluids) {
        NonNullList<FluidStack> stacks = nonNullList(fluids, FluidStack.EMPTY, FluidResource::toStack);

        components.set(GalacticraftDataComponents.FLUID_TANK_CONTENTS, FluidTankContents.fromFluids(stacks));
    }

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

    @SuppressWarnings("unchecked")
    static <BE extends BlockEntity> BE readBlockEntity(BlockEntityType<BE> wantedType, Level level, RegistryFriendlyByteBuf data) {
        BlockEntity blockEntity = level.getBlockEntity(data.readBlockPos());

        if (blockEntity != null && blockEntity.getType().equals(wantedType)) {
            return (BE) blockEntity;
        }

        throw new IllegalArgumentException("BlockEntityType " + wantedType + " not found");
    }

    /** Energy */

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

    /** Generic */

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

    static <R extends Resource, S> NonNullList<S> nonNullList(ResourceHandler<R> resourceHandler, S emptyStack, BiFunction<R, Integer, S> stacker) {
        NonNullList<S> stacks = NonNullList.withSize(resourceHandler.size(), emptyStack);

        for (int i = 0; i < resourceHandler.size(); i++) {
            R resource = resourceHandler.getResource(i);
            int amount = resourceHandler.getAmountAsInt(i);
            S stack = stacker.apply(resource, amount);
            stacks.set(i, stack);
        }

        return stacks;
    }

    static <R extends Resource> void notPlaceable(int index, R resource, int amount) {
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

    static <R extends Resource> void clear(ResourceHandler<R> resourceHandler, IndexModifier<R> indexModifier, R emptyResource) {
        for (int i = 0; i <= resourceHandler.size(); i++) {
            indexModifier.set(i, emptyResource, 0);
        }
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
        if (resourceHandler.size() == 0) {
            return Collections.emptyList();
        }

        List<S> resources = new ArrayList<>(resourceHandler.size());

        for (int i = 0; i < resourceHandler.size(); i++) {
            resources.set(i, stacker.apply(resourceHandler, i));
        }

        return resources;
    }
}
