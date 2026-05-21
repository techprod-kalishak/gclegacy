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
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.network.payload.AddPipePayload;
import io.kalishak.galacticraftlegacy.network.payload.NetworkGridPacket;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.*;

public class FluidNodeNetwork implements NodeNetwork {
    private static final Map<ResourceKey<Level>, List<FluidNodeNetwork>> networkRegistry = new HashMap<>();

    protected final Map<BlockPos, Acceptor> acceptors = Maps.newHashMap();
    protected final Set<TransmitterBlockEntity> pipes = Sets.newHashSet();
    protected @NonNull FluidStack buffer = FluidStack.EMPTY;
    protected boolean hasTransferred;
    protected boolean previouslyTransferred;
    protected int transferDelay;
    protected int capacity;
    private final Set<TransmitterBlockEntity> newPipes = Sets.newHashSet();
    private final Set<DelayQueue> updates = Sets.newHashSet();
    private final Level level;
    private int previousAmount;
    private boolean needsUpdate;
    private int updateDelay;
    private boolean firstUpdate = true;
    private final Snapshot snapshot = new Snapshot();

    public FluidNodeNetwork(Level level, Collection<FluidNodeNetwork> toMerge) {
        this.level = level;

        for (FluidNodeNetwork network : toMerge) {
            if (!network.buffer.isEmpty()) {
                if (this.buffer.isEmpty()) {
                    this.buffer = network.buffer;
                } else if (FluidStack.isSameFluidSameComponents(this.buffer, network.buffer)) {
                    this.buffer.grow(network.buffer.getAmount());
                }
            }

            adoptNetwork(network);
            unregisterNode(network);
        }

        registerNode(this);
    }

    public static void registerNode(FluidNodeNetwork node) {
        ResourceKey<Level> dimension = node.level.dimension();
        var networksInLevel = networkRegistry.computeIfAbsent(dimension, _ -> Lists.newArrayList());

        networksInLevel.add(node);
        networkRegistry.put(dimension, networksInLevel);
    }

    public static void unregisterNode(FluidNodeNetwork node) {
        ResourceKey<Level> dimension = node.level.dimension();
        var networksInLevel = networkRegistry.get(dimension);

        if (networksInLevel != null) {
            networksInLevel.remove(node);
            networkRegistry.put(dimension, networksInLevel);
        }
    }

    public static void onServerLoad(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        NetworkNodesSaveData.get(server).forEach(packed -> load(server.getLevel(packed.dimension()), packed));
    }

    public static void onServerStop(ServerStoppingEvent event) {
        MinecraftServer server = event.getServer();
        NetworkNodesSaveData saveData = server.getDataStorage().computeIfAbsent(NetworkNodesSaveData.SAVE_DATA_ID);
        networkRegistry.forEach((dimension, networks) -> {
            ServerLevel serverLevel = server.getLevel(dimension);
            if (serverLevel != null) {
                saveData.updateNetworks(networks.stream().map(FluidNodeNetwork::save).toList());
            }
        });
    }

    public static FluidNodeNetwork fromBlockEntity(BlockEntity blockEntity, Direction side) {
        if (blockEntity instanceof TransmitterBlockEntity transmitter) {
            return transmitter.getNetwork();
        }

        return null;
    }

    private static void load(@Nullable ServerLevel level, Packed packed) {
        if (level != null) {
            FluidNodeNetwork nodeNetwork = new FluidNodeNetwork(level, Collections.emptyList());
            nodeNetwork.buffer = packed.buffer;
            nodeNetwork.capacity = packed.capacity;

            packed.networkChildren().stream()
                    .map(level::getBlockEntity)
                    .filter(blockEntity -> blockEntity instanceof TransmitterBlockEntity)
                    .forEach(blockEntity -> nodeNetwork.addTransmitter((TransmitterBlockEntity) blockEntity));
            registerNode(nodeNetwork);
        }
    }

    private Packed save() {
        Packed packed = new Packed(
                this.level.dimension(),
                this.buffer.copy(),
                this.capacity,
                new HashSet<>(this.acceptors.keySet())
        );

        unregisterNode(this);

        return packed;
    }

    protected FluidStack getStack() {
        return this.buffer;
    }

    public void adoptNetwork(FluidNodeNetwork network) {
        for (TransmitterBlockEntity toMerge : network.pipes) {
            toMerge.addNetwork(this);
            this.pipes.add(toMerge);
            this.newPipes.add(toMerge);
        }

        this.updateDelay = this.firstUpdate ? 3 : 1;

        this.acceptors.putAll(network.acceptors);
    }

    @Override
    public NetworkType getType() {
        return NetworkType.FLUID;
    }

    public void addTransmitter(TransmitterBlockEntity toMerge) {
        this.pipes.add(toMerge);
        this.newPipes.add(toMerge);
        update();
        onTransmitterAdded(toMerge);
        this.updateDelay = this.firstUpdate ? 20 : 1;
    }

    public void removeTransmitter(TransmitterBlockEntity toMerge) {
        this.pipes.remove(toMerge);
        updateCapacity();
    }

    public void onTransmitterAdded(TransmitterBlockEntity toMerge) {

    }

    public void clamp() {
        if (!this.buffer.isEmpty() && this.buffer.getAmount() > this.capacity) {
            this.buffer.setAmount(this.capacity);
        }
    }

    public void updateCapacity() {
        this.capacity = this.acceptors.values()
                .stream()
                .mapToInt(acceptor -> acceptor.resourceHandler().getCapacityAsInt(0, FluidResource.EMPTY))
                .sum();
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getRequest() {
        return this.capacity - (this.buffer.isEmpty() ? 0 : this.buffer.getAmount());
    }

    private int sendToAll(FluidResource resource, int amount, boolean simulate, Transaction transaction) {
        List<Pair<BlockPos, Map<Direction, ResourceHandler<FluidResource>>>> available = new ArrayList<>(getAcceptors(resource, amount));

        Collections.shuffle(available);

        int totalSent = 0;

        if (!available.isEmpty()) {
            int divider = available.size();
            int remaining = available.size() % divider;
            int each = (amount - remaining) / divider;

            for (Pair<BlockPos, Map<Direction, ResourceHandler<FluidResource>>> pair : available) {
                int currentlySent = each;
                Map<Direction, ResourceHandler<FluidResource>> acceptors = pair.getB();
                EnumSet<Direction> faces = this.acceptors.get(pair.getA()).faces();

                if (remaining > 0) {
                    currentlySent++;
                    remaining--;
                }

                for (Direction direction : faces) {
                    int previouslySent = totalSent;
                    ResourceHandler<FluidResource> handler = acceptors.get(direction);

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

    public int insert(FluidResource resource, int amount, boolean simulate, Transaction transaction) {
        if (resource.isEmpty() || !resource.matches(this.buffer)) {
            return 0;
        }

        int toExtract = Math.min(getRequest(), amount);

        if (!simulate) {
            if (this.buffer.isEmpty()) {
                this.buffer = resource.toStack(toExtract);
            } else {
                this.buffer.grow(toExtract);
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

            int currentAmount = this.buffer.getAmount();

            if (currentAmount != this.previousAmount) {
                this.needsUpdate = true;
            }

            this.previousAmount = currentAmount;

            if (this.hasTransferred != this.previouslyTransferred || this.needsUpdate) {
                BlockPos pos = ((BlockEntity)this.pipes.iterator().next()).getBlockPos();
                PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), 20.0D, createResourceUpdatePacket(pos, this.buffer));
                this.needsUpdate = false;
            }

            this.previouslyTransferred = this.hasTransferred;

            if (!this.buffer.isEmpty()) {
                try (Transaction tx = Transaction.open(null)) {
                    int i = insert(FluidResource.of(this.buffer), this.buffer.getAmount(), false, tx);

                    if (i > 0) {
                        this.previousAmount = i;
                        tx.commit();
                    }
                }

                if (!this.buffer.isEmpty()) {
                    this.buffer.setAmount(this.previousAmount);

                    if (this.buffer.getAmount() <= 0) {
                        this.buffer = FluidStack.EMPTY;
                    }
                }
            }
        }
    }

    protected NetworkGridPacket createResourceUpdatePacket(BlockPos pos, FluidStack fluidStack) {
        return () -> null;
    }

    public void clientTick() {

    }

    public float getScale() {
        if (this.buffer.isEmpty() || getCapacity() == 0) {
            return 0.0F;
        }

        return Math.min(1.0F, this.buffer.getAmount() / (float) getCapacity());
    }

    public List<Pair<BlockPos, Map<Direction, ResourceHandler<FluidResource>>>> getAcceptors(FluidResource resource, int amount) {
        List<Pair<BlockPos, Map<Direction, ResourceHandler<FluidResource>>>> acceptors = new LinkedList<>();

        if (this.level == null || this.level.isClientSide()) {
            return acceptors;
        }

        if (this.acceptors.isEmpty()) {
            updateAcceptors();
        }

        List<BlockPos> copiedPos = new ArrayList<>(this.acceptors.keySet());

        for (BlockPos pos : copiedPos) {
            EnumSet<Direction> faces = this.acceptors.get(pos).faces();
            BlockEntity blockEntity = this.level.getBlockEntity(pos);

            if (faces == null || faces.isEmpty() || blockEntity == null) continue;

            Map<Direction, ResourceHandler<FluidResource>> handlers = Maps.newHashMap();

            for (Direction direction : faces) {
                ResourceHandler<FluidResource> handler = this.level.getCapability(Capabilities.Fluid.BLOCK, pos, direction);

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

    public void update() {
        if (!this.acceptors.isEmpty()) {
            this.acceptors.clear();
        }

        for (TransmitterBlockEntity transmitter : this.pipes) {
            BlockEntity blockEntity = (BlockEntity) transmitter;

            transmitter.onNetworkUpdate();

            if (!blockEntity.hasLevel()) {
                this.pipes.remove(transmitter);
            }

            transmitter.addNetwork(this);
        }

        updateCapacity();
    }

    public void updateAcceptors() {
        if (!this.acceptors.isEmpty()) {
            this.acceptors.clear();
        }

        for (TransmitterBlockEntity transmitter : this.pipes) {
            BlockEntity blockEntity = (BlockEntity) transmitter;

            if (!blockEntity.hasLevel()) {
                this.pipes.remove(transmitter);
            }

            transmitter.addNetwork(this);
        }
    }

    protected class Snapshot extends SnapshotJournal<FluidStack> {
        private FluidStack stack = FluidStack.EMPTY;

        @Override
        protected FluidStack createSnapshot() {
            return stack;
        }

        @Override
        protected void revertToSnapshot(FluidStack u) {
            this.stack = u;
        }

        @Override
        protected void onRootCommit(FluidStack originalState) {
            super.onRootCommit(originalState);
            FluidNodeNetwork.this.needsUpdate = true;
        }
    }

    public record Packed(ResourceKey<Level> dimension, FluidStack buffer, int capacity, HashSet<BlockPos> networkChildren) {
        public static final Codec<Packed> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(Packed::dimension),
                FluidStack.CODEC.fieldOf("buffer").forGetter(Packed::buffer),
                Codec.INT.fieldOf("capacity").forGetter(Packed::capacity),
                BlockPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList).fieldOf("networkChildren").forGetter(Packed::networkChildren)
        ).apply(instance, Packed::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Packed> STREAM_CODEC = StreamCodec.composite(
                ResourceKey.streamCodec(Registries.DIMENSION), Packed::dimension,
                FluidStack.OPTIONAL_STREAM_CODEC, Packed::buffer,
                ByteBufCodecs.INT, Packed::capacity,
                BlockPos.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)), Packed::networkChildren,
                Packed::new
        );
    }

    protected record Acceptor(ResourceHandler<FluidResource> resourceHandler, EnumSet<Direction> faces) {

    }

    protected static class DelayQueue {
        private final Player player;
        private int delay;

        public DelayQueue(Player player, int delay) {
            this.player = player;
            this.delay = delay;
        }

        public DelayQueue(Player player) {
            this(player, 5);
        }
    }
}
