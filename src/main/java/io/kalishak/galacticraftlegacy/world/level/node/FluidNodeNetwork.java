package io.kalishak.galacticraftlegacy.world.level.node;

import io.kalishak.galacticraftlegacy.network.payload.NetworkGridPacket;
import io.kalishak.galacticraftlegacy.network.payload.UpdateResourceNetworkPayload;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class FluidNodeNetwork extends ResourceNodeNetwork<FluidResource, FluidStack> {
    public FluidNodeNetwork(Collection<ResourceNodeNetwork<FluidResource, FluidStack>> toMerge) {
        super(toMerge, Capabilities.Fluid.BLOCK);
    }

    public FluidNodeNetwork() {
        super(Capabilities.Fluid.BLOCK);
    }

    @Override
    protected FluidResource getEmptyResource() {
        return FluidResource.EMPTY;
    }

    @Override
    protected FluidStack produce(FluidResource resource, int amount, Predicate<BlockEntity> ignored) {
        return FluidStack.EMPTY;
    }

    @Override
    protected FluidStack request(Predicate<BlockEntity> ignored) {
        return FluidStack.EMPTY;
    }

    @Override
    protected FluidStack getEmptyStack() {
        return FluidStack.EMPTY;
    }

    @Override
    protected FluidStack getStack() {
        return FluidStack.EMPTY;
    }

    @Override
    protected FluidStack toStack() {
        return this.resource.toStack(this.amount);
    }

    @Override
    public void onTransmitterAdded(TransmitterBlockEntity<ResourceNodeNetwork<FluidResource, FluidStack>> toMerge) {

    }

    @Override
    public void updateCapacity() {

    }

    @Override
    protected NetworkGridPacket createResourceUpdatePacket(BlockPos pos, FluidResource resource, int amount) {
        return new UpdateResourceNetworkPayload(pos, resource, amount);
    }

    @Override
    public NetworkType getType() {
        return NetworkType.FLUID;
    }

    @Override
    public Set<BlockEntity> getConnections() {
        return Set.of();
    }

    @Override
    public NodeNetwork merge(NodeNetwork other) {
        return this;
    }

    @Override
    public void split(BlockEntity connection) {

    }
}
