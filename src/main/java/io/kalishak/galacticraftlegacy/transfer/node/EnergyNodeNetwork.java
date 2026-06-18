/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.network.payload.UpdateEnergyNodeNetworkPayload;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.NonNull;
import oshi.util.tuples.Pair;

import java.util.*;

public class EnergyNodeNetwork extends NodeNetwork {
    protected final Map<BlockPos, Acceptor> acceptors = Maps.newHashMap();
    private final Snapshot snapshot = new Snapshot();
    @VisibleForTesting
    protected int amount;

    public EnergyNodeNetwork(Level level) {
        super(level);

        registerNode(this);
    }

    @Override
    public void adoptNetwork(NodeNetwork network) {
        super.adoptNetwork(network);

        if (network instanceof EnergyNodeNetwork nodeNetwork) {
            this.acceptors.putAll(nodeNetwork.acceptors);
        }
    }

    @Override
    public NodeNetwork merge(NodeNetwork other) {
        if (other instanceof EnergyNodeNetwork nodeNetwork) {
            this.amount += nodeNetwork.amount;
            adoptNetwork(nodeNetwork);
            unregisterNode(other);
        }

        return this;
    }

    public int getRequest() {
        return this.capacity - this.amount;
    }

    public int insert(int amount, boolean simulate, Transaction transaction) {
        int toExtract = Math.min(getRequest(), amount);

        if (!simulate) {
            this.amount += toExtract;

            this.snapshot.updateSnapshots(transaction);
        }

        return toExtract;
    }

    @Override
    public void updateCapacity() {
        this.capacity = this.acceptors.values()
                .stream()
                .mapToInt(acceptor -> acceptor.energyHandler().getCapacityAsInt())
                .sum();
    }

    @Override
    public void update() {
        if (!this.acceptors.isEmpty()) {
            this.acceptors.clear();
        }

        super.update();
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
            BlockPos pos = ((BlockEntity) this.pipes.iterator().next()).getBlockPos();
            PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), 20.0D, new UpdateEnergyNodeNetworkPayload(pos, this.amount));
            this.needsUpdate = false;
        }

        this.previouslyTransferred = this.hasTransferred;

        if (this.amount > 0) {
            try (Transaction tx = Transaction.open(null)) {
                int i = insert(this.amount, false, tx);

                if (i > 0) {
                    previousAmount = i;
                    tx.commit();
                }
            }

            if (this.amount >= 0) {
                this.amount = previousAmount;

                if (this.amount <= 0) {
                    this.amount = 0;
                }
            }
        }
    }

    @Override
    public NetworkType getType() {
        return NetworkType.POWER;
    }

    @Override
    protected PackedNode save() {
        unregisterNode(this);
        return new Packed(
                this.level.dimension(),
                this.amount,
                this.capacity,
                new HashSet<>(this.acceptors.keySet())
        );
    }

    public List<Pair<BlockPos, Map<Direction, EnergyHandler>>> getAcceptors(int amount) {
        List<Pair<BlockPos, Map<Direction, EnergyHandler>>> acceptors = new LinkedList<>();

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

            Map<Direction, EnergyHandler> handlers = Maps.newHashMap();

            for (Direction direction : faces) {
                EnergyHandler handler = this.level.getCapability(Capabilities.Energy.BLOCK, pos, direction);

                if (handler != null) {
                    try (Transaction tx = Transaction.open(null)) {
                        if (handler.insert(amount, tx) > 0) {
                            handlers.put(direction, handler);
                        }
                    }
                }
            }

            acceptors.add(new Pair<>(pos, handlers));
        }

        return acceptors;
    }

    public int sendToAll(int amount, boolean simulate, Transaction transaction) {
        List<Pair<BlockPos, Map<Direction, EnergyHandler>>> available = new ArrayList<>(getAcceptors(amount));

        Collections.shuffle(available);

        int totalSent = 0;

        if (!available.isEmpty()) {
            int divider = available.size();
            int remaining = available.size() % divider;
            int each = (amount - remaining) / divider;

            for (Pair<BlockPos, Map<Direction, EnergyHandler>> pair : available) {
                int currentlySent = each;
                Map<Direction, EnergyHandler> acceptors = pair.getB();
                EnumSet<Direction> faces = this.acceptors.get(pair.getA()).faces();

                if (remaining > 0) {
                    currentlySent++;
                    remaining--;
                }

                for (Direction direction : faces) {
                    int previouslySent = totalSent;
                    EnergyHandler handler = acceptors.get(direction);

                    if (handler != null) {
                        try (Transaction tx = Transaction.open(transaction)) {
                            totalSent += handler.insert(currentlySent, tx);

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

    protected record Acceptor(EnergyHandler energyHandler, EnumSet<Direction> faces) {

    }

    private class Snapshot extends SnapshotJournal<Integer> {
        private int previousAmount;

        @Override
        protected Integer createSnapshot() {
            return this.previousAmount;
        }

        @Override
        protected void revertToSnapshot(Integer snapshot) {
            this.previousAmount = snapshot;
        }

        @Override
        protected void onRootCommit(Integer originalState) {
            super.onRootCommit(originalState);
            EnergyNodeNetwork.this.needsUpdate = true;
        }
    }

    public record Packed(ResourceKey<Level> dimension, int amount, int capacity, HashSet<BlockPos> networkChildren) implements PackedNode {
        public static final MapCodec<Packed> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(Packed::dimension),
                Codec.INT.fieldOf("amount").forGetter(Packed::amount),
                Codec.INT.fieldOf("capacity").forGetter(Packed::capacity),
                BlockPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList).fieldOf("networkChildren").forGetter(Packed::networkChildren)
        ).apply(instance, Packed::new));

        @Override
        public NodeNetwork load(@NonNull ServerLevel level) {
            EnergyNodeNetwork nodeNetwork = new EnergyNodeNetwork(level);
            nodeNetwork.amount = this.amount;
            nodeNetwork.capacity = this.capacity;

            this.networkChildren().stream()
                    .map(level::getBlockEntity)
                    .filter(blockEntity -> blockEntity instanceof TransmitterBlockEntity)
                    .forEach(blockEntity -> nodeNetwork.addTransmitter((TransmitterBlockEntity) blockEntity));
            return nodeNetwork;
        }

        @Override
        public MapCodec<Packed> codec() {
            return MAP_CODEC;
        }
    }
}
