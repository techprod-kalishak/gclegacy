/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node;

import com.google.common.collect.Maps;
import io.kalishak.galacticraftlegacy.network.payload.NetworkGridPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.*;

public abstract class ResourceNodeNetwork<R extends Resource, S> extends NodeNetwork {
    protected final Map<BlockPos, Acceptor<R>> acceptors = Maps.newHashMap();
    protected final R emptyResource;
    private final Snapshot snapshot = new Snapshot();
    private final BlockCapability<ResourceHandler<R>, @Nullable Direction> capability;
    @VisibleForTesting
    protected R buffer;
    @VisibleForTesting
    protected int amount;

    protected ResourceNodeNetwork(Level level, R emptyResource, BlockCapability<ResourceHandler<R>, @Nullable Direction> capability) {
        super(level);
        this.emptyResource = emptyResource;
        this.capability = capability;
    }

    protected ResourceNodeNetwork(Level level, R emptyResource, BlockCapability<ResourceHandler<R>, @Nullable Direction> capability, Collection<? extends ResourceNodeNetwork<R, S>> toMerge) {
        this(level, emptyResource, capability);
        merge(this, toMerge);
    }

    protected static <R extends Resource, S> void merge(ResourceNodeNetwork<R, S> target, Collection<? extends ResourceNodeNetwork<R, S>> toMerge) {
        for (var network : toMerge) {
            if (!network.buffer.isEmpty()) {
                if (target.buffer.isEmpty()) {
                    target.buffer = network.buffer;
                } else if (target.buffer.equals(network.buffer)) {
                    target.amount += network.amount;
                }
            }

            target.adoptNetwork(network);
            unregisterNode(network);
        }
    }

    protected abstract S toStack();
    protected abstract NetworkGridPacket createResourceUpdatePacket(BlockPos pos, S stack);

    @Override
    @SuppressWarnings("unchecked")
    public NodeNetwork merge(NodeNetwork other) {
        if (other instanceof ResourceNodeNetwork<?,?> resourceNetwork) {
            if (!resourceNetwork.buffer.isEmpty()) {
                if (this.buffer.isEmpty()) {
                    this.buffer = (R) resourceNetwork.buffer;
                } else if (this.buffer.equals(resourceNetwork.buffer)) {
                    this.amount += resourceNetwork.amount;
                }
            }

            this.adoptNetwork(resourceNetwork);
            unregisterNode(resourceNetwork);
        }

        return this;
    }

    public void clamp() {
        if (!this.buffer.isEmpty() && this.amount > this.capacity) {
            this.amount = this.capacity;
        }
    }

    public float getScale() {
        if (this.buffer.isEmpty() || getCapacity() == 0) {
            return 0.0F;
        }

        return Math.min(1.0F, this.amount / (float) getCapacity());
    }

    @Override
    public void updateCapacity() {
        this.capacity = this.acceptors.values()
                .stream()
                .mapToInt(acceptor -> acceptor.resourceHandler().getCapacityAsInt(0, this.emptyResource))
                .sum();
    }

    @Override
    public void update() {
        if (!this.acceptors.isEmpty()) {
            this.acceptors.clear();
        }

        super.update();
    }

    public int getRequest() {
        return this.capacity - (this.buffer.isEmpty() ? 0 : this.amount);
    }

    public int insert(R resource, int amount, boolean simulate, Transaction transaction) {
        if (resource.isEmpty() || !resource.equals(this.buffer)) {
            return 0;
        }

        int toExtract = Math.min(getRequest(), amount);

        if (!simulate) {
            if (this.buffer.isEmpty()) {
                this.buffer = resource;
            } else {
                this.amount += toExtract;
            }

            this.snapshot.updateSnapshots(transaction);
        }

        return toExtract;
    }

    @Override
    protected void transferTick(ServerLevel serverLevel) {
        int previousAmount = 0;

        if (this.transferDelay == 0) {
            this.hasTransferred = false;
        } else {
            this.transferDelay--;
        }

        int currentAmount = this.amount;

        if (currentAmount != previousAmount) {
            this.needsUpdate = true;
        }

        previousAmount = currentAmount;

        if (this.hasTransferred != this.previouslyTransferred || this.needsUpdate) {
            BlockPos pos = ((BlockEntity)this.pipes.iterator().next()).getBlockPos();
            PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), 20.0D, createResourceUpdatePacket(pos, toStack()));
            this.needsUpdate = false;
        }

        this.previouslyTransferred = this.hasTransferred;

        if (!this.buffer.isEmpty()) {
            try (Transaction tx = Transaction.open(null)) {
                int i = insert(this.buffer, this.amount, false, tx);

                if (i > 0) {
                    previousAmount = i;
                    tx.commit();
                }
            }

            if (!this.buffer.isEmpty()) {
                this.amount = previousAmount;

                if (this.amount <= 0) {
                    this.buffer = this.emptyResource;
                }
            }
        }
    }

    public List<Pair<BlockPos, Map<Direction, ResourceHandler<R>>>> getAcceptors(R resource, int amount) {
        List<Pair<BlockPos, Map<Direction, ResourceHandler<R>>>> acceptors = new LinkedList<>();

        if (this.level == null || this.level.isClientSide()) {
            return acceptors;
        }

        if (this.acceptors.isEmpty()) {
            update();
        }

        List<BlockPos> copiedPos = new ArrayList<>(this.acceptors.keySet());

        for (BlockPos pos : copiedPos) {
            EnumSet<Direction> faces = this.acceptors.get(pos).faces();
            BlockEntity blockEntity = this.level.getBlockEntity(pos);

            if (faces == null || faces.isEmpty() || blockEntity == null) continue;

            Map<Direction, ResourceHandler<R>> handlers = Maps.newHashMap();

            for (Direction direction : faces) {
                ResourceHandler<R> handler = this.level.getCapability(this.capability, pos, direction);

                if (handler != null) {
                    try (Transaction tx = Transaction.open(null)) {
                        if (handler.insert(resource, amount, tx) > 0) {
                            handlers.put(direction, handler);
                        }
                    }
                }
            }

            acceptors.add(new Pair<>(pos, handlers));
        }

        return acceptors;
    }

    protected int sendToAll(R resource, int amount, boolean simulate, Transaction transaction) {
        List<Pair<BlockPos, Map<Direction, ResourceHandler<R>>>> available = new ArrayList<>(getAcceptors(resource, amount));

        Collections.shuffle(available);

        int totalSent = 0;

        if (!available.isEmpty()) {
            int divider = available.size();
            int remaining = available.size() % divider;
            int each = (amount - remaining) / divider;

            for (Pair<BlockPos, Map<Direction, ResourceHandler<R>>> pair : available) {
                int currentlySent = each;
                Map<Direction, ResourceHandler<R>> acceptors = pair.getB();
                EnumSet<Direction> faces = this.acceptors.get(pair.getA()).faces();

                if (remaining > 0) {
                    currentlySent++;
                    remaining--;
                }

                for (Direction direction : faces) {
                    int previouslySent = totalSent;
                    ResourceHandler<R> handler = acceptors.get(direction);

                    if (handler != null) {
                        try (Transaction tx = Transaction.open(transaction)) {
                            totalSent += handler.insert(resource, currentlySent, tx);

                            if (!simulate && totalSent != previouslySent) {
                                tx.commit();
                            }
                        }
                    }

                    if (totalSent > previouslySent) {
                        break;
                    }
                }
            }
        }

        if (!simulate && totalSent > 0) {
            this.hasTransferred = true;
            this.transferDelay = 2;
        }

        return totalSent;
    }

    protected class Snapshot extends SnapshotJournal<S> {
        private S stack = toStack();

        @Override
        protected S createSnapshot() {
            return stack;
        }

        @Override
        protected void revertToSnapshot(S u) {
            this.stack = u;
        }

        @Override
        protected void onRootCommit(S originalState) {
            super.onRootCommit(originalState);
            ResourceNodeNetwork.this.needsUpdate = true;
        }
    }

    protected record Acceptor<R extends Resource>(ResourceHandler<R> resourceHandler, EnumSet<Direction> faces) {
    }
}
