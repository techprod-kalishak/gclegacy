/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.network.payload.NetworkGridPacket;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class FluidNodeNetwork extends ResourceNodeNetwork<FluidResource, FluidStack> {
    public FluidNodeNetwork(Level level, Collection<FluidNodeNetwork> toMerge) {
        super(level, FluidResource.EMPTY, Capabilities.Fluid.BLOCK, toMerge);

        registerNode(this);
    }

    public static FluidNodeNetwork fromBlockEntity(BlockEntity blockEntity, Direction side) {
        if (blockEntity instanceof TransmitterBlockEntity transmitter) {
            return transmitter.getNetwork(side) instanceof FluidNodeNetwork fluidNodeNetwork ? fluidNodeNetwork : null;
        }

        return null;
    }

    @Override
    public void adoptNetwork(NodeNetwork network) {
        super.adoptNetwork(network);

        if (network instanceof FluidNodeNetwork nodeNetwork) {
            this.acceptors.putAll(nodeNetwork.acceptors);
        }
    }

    @Override
    public NetworkType getType() {
        return NetworkType.FLUID;
    }

    @Override
    protected FluidStack toStack() {
        return this.buffer.toStack(this.amount);
    }

    @Override
    protected Packed save() {
        Packed packed = new Packed(
                this.level.dimension(),
                this.buffer.toStack(this.amount),
                this.capacity,
                new HashSet<>(this.acceptors.keySet())
        );

        unregisterNode(this);

        return packed;
    }

    @Override
    protected NetworkGridPacket createResourceUpdatePacket(BlockPos pos, FluidStack fluidStack) {
        return null;
    }

    public record Packed(ResourceKey<Level> dimension, FluidStack buffer, int capacity, HashSet<BlockPos> networkChildren) implements NodeNetwork.PackedNode {
        public static final MapCodec<Packed> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(Packed::dimension),
                FluidStack.CODEC.fieldOf("buffer").forGetter(Packed::buffer),
                Codec.INT.fieldOf("capacity").forGetter(Packed::capacity),
                BlockPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList).fieldOf("networkChildren").forGetter(Packed::networkChildren)
        ).apply(instance, Packed::new));

        @Override
        public FluidNodeNetwork load(@NonNull ServerLevel level) {
            FluidNodeNetwork nodeNetwork = new FluidNodeNetwork(level, Collections.emptyList());
            nodeNetwork.buffer = FluidResource.of(this.buffer);
            nodeNetwork.amount = this.buffer.getAmount();
            nodeNetwork.capacity = this.capacity;

            this.networkChildren().stream()
                    .map(level::getBlockEntity)
                    .filter(blockEntity -> blockEntity instanceof TransmitterBlockEntity)
                    .forEach(blockEntity -> nodeNetwork.addTransmitter((TransmitterBlockEntity) blockEntity));
            return nodeNetwork;
        }

        @Override
        public MapCodec<FluidNodeNetwork.Packed> codec() {
            return MAP_CODEC;
        }
    }
}
