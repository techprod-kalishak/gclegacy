/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.transfer.node.FluidNodeNetwork;
import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.ResourceTransmitter;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class ColoredPipeBlockEntity extends AbstractConnectableBlockEntity implements ResourceTransmitter {
    private ConnectionState connectionState = ConnectionState.DEFAULT;
    private FluidStack stack = FluidStack.EMPTY;
    private DyeColor color = DyeColor.WHITE;
    private final SingleTankResourceHandler fluidHandler = new SingleTankResourceHandler() {
        @Override
        public FluidStack getFluidStack() {
            return ColoredPipeBlockEntity.this.stack;
        }

        @Override
        public void setFluidStack(FluidStack stack) {
            ColoredPipeBlockEntity.this.stack = stack;
        }

        @Override
        protected int getCapacity(FluidResource resource) {
            return ColoredPipeBlockEntity.this.getCapacity();
        }

        @Override
        protected void notifyChange() {
            ColoredPipeBlockEntity.this.setData(GalacticraftAttachments.SYNC_FLUID_STACK, getFluidStack());
        }
    };

    public ColoredPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.COLORED_PIPE.get(), pos, blockState);
    }

    public ColoredPipeBlockEntity(BlockPos pos, BlockState blockState, DyeColor color) {
        this(pos, blockState);
        this.color = color;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GalacticraftBlockEntityType.COLORED_PIPE.get(), (blockEntity, cxt) -> blockEntity.fluidHandler);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, ColoredPipeBlockEntity coloredPipe) {
        if (!coloredPipe.isValid) {
            //todo update transmitters
            coloredPipe.isValid = true;
        }

        if (coloredPipe.connectionState == ConnectionState.PULL) {
            for (Direction direction : Direction.values()) {
                BlockEntity neighbour = level.getBlockEntity(pos.relative(direction));

                if (neighbour instanceof ColoredPipeBlockEntity neighbourBlockEntity) {
                    ResourceHandler<FluidResource> handler = level.getCapability(Capabilities.Fluid.BLOCK, neighbourBlockEntity.getBlockPos(), direction.getOpposite());

                    if (handler != null) {
                        try (Transaction childTx = Transaction.open(null)) {
                            if (ResourceHandlerUtil.move(handler, coloredPipe.fluidHandler, resource -> !resource.isEmpty(), coloredPipe.getCapacity(), childTx) > 0) {
                                childTx.commit();
                            }
                        }
                    }
                }
            }
        }
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    @Override
    public FluidResource getResource() {
        return FluidResource.of(this.stack);
    }

    @Override
    public int getAmount() {
        return this.stack.getAmount();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("ConnectionState", ConnectionState.CODEC, this.connectionState);
        output.store("Color", DyeColor.CODEC, this.color);
        this.fluidHandler.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        input.read("ConnectionState", ConnectionState.CODEC).ifPresentOrElse(this::setConnectionState, () -> setConnectionState(ConnectionState.DEFAULT));
        input.read("Color", DyeColor.CODEC).ifPresentOrElse(this::setColor, () -> setColor(DyeColor.WHITE));
        this.fluidHandler.deserialize(input);
    }

    protected Set<BlockEntity> getSurroundingBlockEntities() {
        if (getNetwork().getType() == NetworkType.FLUID && this.surroundingBlockEntities == null) {
            this.surroundingBlockEntities = OxygenHelper.getFluidConnections(this, this.level);
        }

        return this.surroundingBlockEntities;
    }

    @Override
    protected void resetNetwork() {
    }

    @Override
    public int getCapacity() {
        return 8000;
    }

    @Override
    public void updateNeighbouringTransmitters(Level level, BlockPos pos) {

    }

    @Override
    public void addNetwork(FluidNodeNetwork network) {

    }

    @Override
    public boolean canConnect(Direction direction, NetworkType networkType) {
        if (this.level != null) {
            BlockEntity neighbour = this.level.getBlockEntity(getBlockPos().relative(direction));

            if (neighbour instanceof ColoredPipeBlockEntity coloredPipe) {
                return coloredPipe.color == this.color;
            }
        }

        return super.canConnect(direction, networkType);
    }

    public enum ConnectionState implements SerializableEnum {
        DEFAULT(0, "default"),
        PULL(1, "pull");

        public static final Codec<ConnectionState> CODEC = SerializableEnum.codec(ConnectionState.class);
        public static final StreamCodec<ByteBuf, ConnectionState> STREAM_CODEC = SerializableEnum.streamCodec(ConnectionState.class);
        private final String name;
        private final int id;

        ConnectionState(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.id;
        }
    }
}
