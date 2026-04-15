package io.kalishak.galacticraftlegacy.world.level.node;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.kalishak.galacticraftlegacy.network.payload.AddPipePayload;
import io.kalishak.galacticraftlegacy.network.payload.NetworkGridPacket;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.function.Predicate;

public abstract class ResourceNodeNetwork<R extends Resource, S> implements NodeNetwork {
    public final Map<BlockPos, ResourceHandler<R>> acceptors = Maps.newHashMap();
    public final Map<BlockPos, EnumSet<Direction>> acceptorDirections = Maps.newHashMap();
    public final Set<TransmitterBlockEntity<ResourceNodeNetwork<R, S>>> pipes = Sets.newHashSet();
    public @NonNull R resource = getEmptyResource();
    public int amount;
    public boolean hasTransferred;
    public boolean previouslyTransferred;
    public int transferDelay;
    private final Set<TransmitterBlockEntity<ResourceNodeNetwork<R, S>>> newPipes = Sets.newHashSet();
    private final Set<DelayQueue> updates = Sets.newHashSet();
    private int capacity;
    private @Nullable Level level;
    private int previousAmount;
    private boolean needsUpdate;
    private int updateDelay;
    private boolean firstUpdate = true;
    private final Snapshot snapshot = new Snapshot();
    private final BlockCapability<ResourceHandler<R>, @Nullable Direction> capability;

    public ResourceNodeNetwork(BlockCapability<ResourceHandler<R>, @Nullable Direction> capability) {
        this.capability = capability;
    }

    public ResourceNodeNetwork(Collection<ResourceNodeNetwork<R, S>> toMerge, BlockCapability<ResourceHandler<R>, @Nullable Direction> capability) {
        this.capability = capability;
        for (ResourceNodeNetwork<R, S> network : toMerge) {
            if (!network.resource.isEmpty()) {
                if (this.resource.isEmpty()) {
                    this.resource = network.resource;
                    this.amount = network.amount;
                } else if (this.resource.equals(network.resource)) {
                    this.amount += network.amount;
                }
            }

            adoptNetwork(network);
        }
    }

    protected abstract R getEmptyResource();

    protected abstract S produce(R resource, int amount, Predicate<BlockEntity> ignored);

    protected abstract S request(Predicate<BlockEntity> ignored);

    protected abstract S getEmptyStack();
    protected abstract S getStack();
    protected abstract S toStack();

    public void adoptNetwork(ResourceNodeNetwork<R, S> network) {
        for (TransmitterBlockEntity<ResourceNodeNetwork<R, S>> toMerge : network.pipes) {
            toMerge.addNetwork(this);
            this.pipes.add(toMerge);
            this.newPipes.add(toMerge);
        }

        this.updateDelay = this.firstUpdate ? 3 : 1;

        this.acceptors.putAll(network.acceptors);

        for (Map.Entry<BlockPos, EnumSet<Direction>> entry : network.acceptorDirections.entrySet()) {
            BlockPos pos = entry.getKey();

            if (this.acceptorDirections.containsKey(pos)) {
                this.acceptorDirections.get(pos).addAll(entry.getValue());
            } else {
                this.acceptorDirections.put(pos, entry.getValue());
            }
        }
    }

    public void addTransmitter(TransmitterBlockEntity<ResourceNodeNetwork<R, S>> toMerge) {
        this.pipes.add(toMerge);
        this.newPipes.add(toMerge);
        update();
        this.updateDelay = this.firstUpdate ? 20 : 1;
    }

    public void removeTransmitter(TransmitterBlockEntity<ResourceNodeNetwork<R, S>> toMerge) {
        this.pipes.remove(toMerge);
        updateCapacity();
    }

    public abstract void onTransmitterAdded(TransmitterBlockEntity<ResourceNodeNetwork<R, S>> toMerge);

    public void clamp() {
        if (!this.resource.isEmpty() && this.amount > this.capacity) {
            this.amount = this.capacity;
        }
    }

    public abstract void updateCapacity();

    public int getCapacity() {
        return this.capacity;
    }

    public int getRequest() {
        return this.capacity - (this.resource.isEmpty() ? 0 : this.amount);
    }

    private int sendToAll(R resource, int amount, boolean simulate) {
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
                EnumSet<Direction> faces = this.acceptorDirections.get(pair.getA());

                if (remaining > 0) {
                    currentlySent++;
                    remaining--;
                }

                for (Direction direction : faces) {
                    int previouslySent = totalSent;
                    ResourceHandler<R> handler = acceptors.get(direction);

                    if (handler != null) {
                        try (Transaction tx = Transaction.open(null)) {
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

    public int insert(R resource, int amount, boolean simulate, Transaction transaction) {
        if (resource.isEmpty() || !resource.equals(this.resource)) {
            return 0;
        }

        int toExtract = Math.min(getRequest(), amount);

        if (!simulate) {
            if (this.resource.isEmpty()) {
                this.resource = resource;
                this.amount = toExtract;
            } else {
                this.amount += toExtract;
            }

            this.snapshot.updateSnapshots(transaction);
        }

        return toExtract;
    }

    public void endTick() {
        serverTick();
    }

    public void addUpdater(Player player) {
        this.updates.add(new DelayQueue(player));
    }

    private BlockPos anyPos() {
        return ((BlockEntity) this.pipes.iterator().next()).getBlockPos();
    }

    public void serverTick() {
        if (this.level instanceof ServerLevel serverLevel) {
            for (DelayQueue update : this.updates) {
                if (update.delay > 0) {
                    update.delay--;
                } else {
                    this.newPipes.addAll(this.pipes);
                    PacketDistributor.sendToPlayer((ServerPlayer) update.player, new AddPipePayload(anyPos(), this.firstUpdate, this.newPipes.size()));
                    this.newPipes.clear();
                }
            }

            if (this.updateDelay > 0) {
                this.updateDelay--;

                if (this.updateDelay == 0) {
                    BlockPos pos = ((BlockEntity)this.pipes.iterator().next()).getBlockPos();
                    PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), 30.0D, new AddPipePayload(anyPos(), this.firstUpdate, this.newPipes.size()));
                    this.firstUpdate = false;
                    this.newPipes.clear();
                    this.needsUpdate = true;
                }
            }

            this.previousAmount = 0;

            if (this.transferDelay == 0) {
                this.hasTransferred = false;
            } else {
                this.transferDelay--;
            }

            int currentAmount = this.resource.isEmpty() ? 0 : this.amount;

            if (currentAmount != this.previousAmount) {
                this.needsUpdate = true;
            }

            this.previousAmount = currentAmount;

            if (this.hasTransferred != this.previouslyTransferred || this.needsUpdate) {
                BlockPos pos = ((BlockEntity)this.pipes.iterator().next()).getBlockPos();
                PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), 20.0D, createResourceUpdatePacket(pos, this.resource, this.amount));
                this.needsUpdate = false;
            }

            this.previouslyTransferred = this.hasTransferred;

            if (!this.resource.isEmpty()) {
                try (Transaction tx = Transaction.open(null)) {
                    int i = insert(this.resource, this.amount, false, tx);

                    if (i > 0) {
                        this.previousAmount = i;
                        tx.commit();
                    }
                }

                if (!this.resource.isEmpty()) {
                    this.amount = this.previousAmount;

                    if (this.amount <= 0) {
                        this.resource = getEmptyResource();
                    }
                }
            }
        }
    }

    protected abstract NetworkGridPacket createResourceUpdatePacket(BlockPos pos, R resource, int amount);

    public void clientTick() {

    }

    public float getScale() {
        if (this.resource.isEmpty() || getCapacity() == 0) {
            return 0.0F;
        }

        return Math.min(1.0F, this.amount / (float) getCapacity());
    }

    public List<Pair<BlockPos, Map<Direction, ResourceHandler<R>>>> getAcceptors(R resource, int amount) {
        List<Pair<BlockPos, Map<Direction, ResourceHandler<R>>>> acceptors = new LinkedList<>();

        if (this.level == null || this.level.isClientSide()) {
            return acceptors;
        }

        if (this.acceptors.isEmpty()) {
            updateAcceptors();
        }

        List<BlockPos> copiedPos = new ArrayList<>(this.acceptors.keySet());

        for (BlockPos pos : copiedPos) {
            EnumSet<Direction> faces = acceptorDirections.get(pos);
            BlockEntity blockEntity = this.level.getBlockEntity(pos);

            if (faces == null || faces.isEmpty() || blockEntity == null) continue;

            Map<Direction, ResourceHandler<R>> handlers = Maps.newHashMap();

            for (Direction direction : faces) {
                ResourceHandler<R> handler = this.level.getCapability(this.capability, pos, direction);

                if (handler != null) {
                    handlers.put(direction, handler);
                }
            }

            acceptors.add(new Pair<>(pos, handlers));
        }

        return acceptors;
    }

    @Override
    public void update() {
        if (!this.acceptors.isEmpty()) {
            this.acceptors.clear();
        }

        for (TransmitterBlockEntity<ResourceNodeNetwork<R, S>> transmitter : this.pipes) {
            BlockEntity blockEntity = (BlockEntity) transmitter;

            transmitter.onNetworkUpdate();

            if (!blockEntity.hasLevel()) {
                this.pipes.remove(transmitter);
            } else {
                this.level = blockEntity.getLevel();
            }

            transmitter.addNetwork(this);
        }

        updateCapacity();
    }

    public void updateAcceptors() {
        if (!this.acceptors.isEmpty()) {
            this.acceptors.clear();
        }

        for (TransmitterBlockEntity<ResourceNodeNetwork<R, S>> transmitter : this.pipes) {
            BlockEntity blockEntity = (BlockEntity) transmitter;

            if (!blockEntity.hasLevel()) {
                this.pipes.remove(transmitter);
            }

            transmitter.addNetwork(this);
        }
    }

    protected class Snapshot extends SnapshotJournal<S> {
        S stack = ResourceNodeNetwork.this.getEmptyStack();

        @Override
        protected S createSnapshot() {
            return ResourceNodeNetwork.this.getStack();
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

    protected static class DelayQueue {
        public final Player player;
        public int delay = 5;

        public DelayQueue(Player player, int delay) {
            this.player = player;
            this.delay = delay;
        }

        public DelayQueue(Player player) {
            this.player = player;
        }

        @Override
        public int hashCode() {
            return this.player.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof DelayQueue delayQueue && this.player.equals(delayQueue.player);
        }
    }
}
