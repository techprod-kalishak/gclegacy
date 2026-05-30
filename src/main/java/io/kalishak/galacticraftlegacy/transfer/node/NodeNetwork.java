/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.network.payload.AddPipePayload;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Function;

public abstract class NodeNetwork {
    private static final Map<ResourceKey<Level>, List<NodeNetwork>> NODE_IN_LEVEL = new HashMap<>();
    protected final Set<TransmitterBlockEntity> pipes = Sets.newHashSet();
    protected final Set<TransmitterBlockEntity> newPipes = Sets.newHashSet();
    protected final Set<DelayQueue> updates = Sets.newHashSet();
    protected final Level level;
    protected boolean hasTransferred;
    protected boolean previouslyTransferred;
    protected int transferDelay;
    protected int capacity;
    protected boolean needsUpdate;
    protected int updateDelay;
    protected boolean firstUpdate = true;

    protected NodeNetwork(Level level) {
        this.level = level;

        registerNode(this);
    }

    public void adoptNetwork(NodeNetwork network) {
        for (TransmitterBlockEntity toMerge : network.pipes) {
            toMerge.addNetwork(this);
            this.pipes.add(toMerge);
            this.newPipes.add(toMerge);
        }

        this.updateDelay = this.firstUpdate ? 3 : 1;
    }

    public abstract void updateCapacity();

    public abstract NodeNetwork merge(NodeNetwork other);

    public int getCapacity() {
        return this.capacity;
    }

    public void update() {
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

    protected void serverTick() {
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

            transferTick(serverLevel);
        }
    }

    protected abstract void transferTick(ServerLevel serverLevel);

    protected void clientTick() {

    }

    private void tick() {
        if (this.level.isClientSide()) {
            clientTick();
        } else {
            serverTick();
        }
    }

    public abstract NetworkType getType();

    protected abstract NodeNetwork.PackedNode save();

    public void addUpdater(Player player) {
        this.updates.add(new DelayQueue(player));
    }

    protected BlockPos anyPos() {
        return ((BlockEntity) this.pipes.iterator().next()).getBlockPos();
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

    public static void levelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        List<NodeNetwork> networks = NODE_IN_LEVEL.get(level.dimension());

        if (networks != null && !networks.isEmpty()) {
            for (NodeNetwork network : networks) {
                network.tick();
            }
        }
    }

    public static void onServerLoad(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        NetworkNodesSaveData.get(server).forEach(packed -> {
            ServerLevel level = server.getLevel(packed.dimension());

            if (level != null) {
                registerNode(packed.load(level));
            }
        });
    }

    public static void onServerStop(ServerStoppingEvent event) {
        MinecraftServer server = event.getServer();
        NetworkNodesSaveData saveData = server.getDataStorage().computeIfAbsent(NetworkNodesSaveData.SAVE_DATA_ID);
        NODE_IN_LEVEL.forEach((dimension, networks) -> {
            ServerLevel serverLevel = server.getLevel(dimension);
            if (serverLevel != null) {
                saveData.updateNetworks(networks.stream().map(NodeNetwork::save).toList());
            }
        });
    }

    protected static void registerNode(NodeNetwork node) {
        ResourceKey<Level> dimension = node.level.dimension();
        var networksInLevel = NODE_IN_LEVEL.computeIfAbsent(dimension, _ -> Lists.newArrayList());

        networksInLevel.add(node);
        NODE_IN_LEVEL.put(dimension, networksInLevel);
    }

    protected static void unregisterNode(NodeNetwork node) {
        ResourceKey<Level> dimension = node.level.dimension();
        var networksInLevel = NODE_IN_LEVEL.get(dimension);

        if (networksInLevel != null) {
            networksInLevel.remove(node);
            NODE_IN_LEVEL.put(dimension, networksInLevel);
        }
    }

    protected static class DelayQueue {
        protected final Player player;
        protected int delay;

        public DelayQueue(Player player, int delay) {
            this.player = player;
            this.delay = delay;
        }

        public DelayQueue(Player player) {
            this(player, 5);
        }
    }

    public interface PackedNode {
        Codec<Either<FluidNodeNetwork.Packed, PackedNode>> DEFAULT_OR_DISPATCH_CODEC = Codec.either(
                FluidNodeNetwork.Packed.MAP_CODEC.codec(),
                Codec.lazyInitialized(GalacticraftRegistries.PACKED_NODE_TYPE::byNameCodec)
                        .dispatch(PackedNode::codec, Function.identity())
        );
        Codec<PackedNode> CODEC = DEFAULT_OR_DISPATCH_CODEC.xmap(
                either -> either.map(Function.identity(), Function.identity()),
                f -> f instanceof FluidNodeNetwork.Packed fluidPack ? Either.left(fluidPack) : Either.right(f)
        );

        ResourceKey<Level> dimension();
        int capacity();
        HashSet<BlockPos> networkChildren();

        NodeNetwork load(@NonNull ServerLevel level);

        MapCodec<? extends PackedNode> codec();
    }
}
