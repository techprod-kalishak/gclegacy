/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.TankWrapper;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.WorldlyTankWrapper;
import io.kalishak.galacticraftlegacy.transfer.node.FluidNodeNetwork;
import io.kalishak.galacticraftlegacy.transfer.node.object.OxygenConsumer;
import io.kalishak.galacticraftlegacy.world.inventory.Tank;
import io.kalishak.galacticraftlegacy.world.inventory.WorldlyTank;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public abstract class AbstractOxygenBlockEntity extends AbstractMachineBlockEntity implements Tank {
    protected final int oxygenPerTick;
    protected FluidStack oxygenTank = FluidStack.EMPTY;
    protected final SingleTankResourceHandler oxygenHandler;
    protected int lastOxygenAmount;

    protected AbstractOxygenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int oxygenPerTick, int capacity) {
        super(type, pos, blockState);
        this.oxygenPerTick = oxygenPerTick;
        this.oxygenHandler = new SingleTankResourceHandler(capacity) {
            @Override
            protected void notifyChange() {
                if (!AbstractOxygenBlockEntity.this.isRemoved()) {
                    AbstractOxygenBlockEntity.this.setData(GalacticraftAttachments.SYNC_FLUID_STACK, AbstractOxygenBlockEntity.this.oxygenHandler.getFluidStack());
                }
            }
        };
    }

    public static <M extends AbstractOxygenBlockEntity> void registerEnergyFluidItemCapability(BlockEntityType<M> blockEntity, RegisterCapabilitiesEvent event) {
        registerItemCapability(blockEntity, event);
        registerEnergyCapability(blockEntity, event);
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                blockEntity,
                (machine, context) -> {
                    if (machine instanceof WorldlyTank worldlyTank) {
                        return new WorldlyTankWrapper(worldlyTank, context);
                    }

                    return new TankWrapper(machine);
                }
        );
    }

    protected static void oxygenServerTick(ServerLevel level, BlockPos worldPosition, BlockState blockState, AbstractOxygenBlockEntity entity, @Nullable Transaction tx) {
        if (entity.isOxygenConsumer()) {
            try (Transaction childTx = Transaction.open(tx)) {
                int snapshot = entity.oxygenHandler.getAmount();

                if (entity.oxygenHandler.extract(0, FluidResource.of(GalacticraftFluids.OXYGEN), entity.oxygenPerTick, childTx) > 0) {
                    entity.lastOxygenAmount = snapshot;
                    childTx.commit();
                }
            }
        }
    }

    protected static boolean produce(ServerLevel level, BlockPos worldPosition, AbstractOxygenBlockEntity entity, Direction side, @Nullable Transaction tx) {
        if (!entity.isOxygenConsumer()) {
            BlockEntity targetEntity = level.getBlockEntity(worldPosition.relative(side));
            FluidNodeNetwork network = FluidNodeNetwork.fromBlockEntity(targetEntity, side);

            if (network != null) {
                int requestedAmount = network.getRequest();

                if (requestedAmount > 0) {
                    try (Transaction childTx = Transaction.open(tx)) {
                        int moveAmount = Math.min(entity.oxygenPerTick, requestedAmount);
                        int inserted = network.insert(FluidResource.of(GalacticraftFluids.OXYGEN), moveAmount, false, childTx);

                        if (inserted > 0) {
                            int extractedFrom = entity.oxygenHandler.extract(FluidResource.of(GalacticraftFluids.OXYGEN), moveAmount, childTx);

                            if (inserted == extractedFrom) {
                                childTx.commit();
                                return true;
                            }
                        }
                    }
                }
            } else if (targetEntity instanceof OxygenConsumer oxygenConsumer) {
                int requestedAmount = oxygenConsumer.getRequest(side.getOpposite());
                ResourceHandler<FluidResource> targetResourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Fluid.BLOCK, FluidResource.EMPTY, targetEntity, side.getOpposite());

                if (requestedAmount > 0) {
                    try (Transaction childTx = Transaction.open(tx)) {
                        int moveAmount = Math.min(entity.oxygenPerTick, requestedAmount);
                        int inserted = targetResourceHandler.insert(FluidResource.of(GalacticraftFluids.OXYGEN), moveAmount, childTx);

                        if (inserted > 0) {
                            int extractedFrom = entity.oxygenHandler.extract(FluidResource.of(GalacticraftFluids.OXYGEN), moveAmount, childTx);

                            if (inserted == extractedFrom) {
                                childTx.commit();
                                return true;
                            }
                        }
                    }
                }

            }
        }

        return false;
    }

    public int getScaledOxygenLevel(int scale) {
        return (int) Math.floor(this.oxygenHandler.getAmount() * (float) scale / (this.oxygenHandler.getCapacity() - this.oxygenPerTick));
    }

    public abstract boolean isOxygenConsumer();

    public int getClampedOxygenLevel(int scale) {
        return Math.clamp((int) Math.floor((double) this.oxygenHandler.getAmount() / this.oxygenHandler.getCapacity() * scale), 0, scale);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("OxygenTank", FluidStack.OPTIONAL_CODEC, this.oxygenTank);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.oxygenTank = input.read("OxygenTank", FluidStack.OPTIONAL_CODEC).orElse(FluidStack.EMPTY);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.oxygenTank = components.getOrDefault(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY).copy();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(this.oxygenTank));
    }

    @Override
    public FluidStack getFluid(int index) {
        return null;
    }

    @Override
    public FluidStack removeFluid(int index, int amount) {
        FluidStack stack = this.oxygenTank.copy();
        onTankChange(index, stack);
        stack.shrink(amount);

        this.oxygenTank = stack.isEmpty() ? FluidStack.EMPTY : stack;

        return stack;
    }

    @Override
    public FluidStack removeNoUpdate(int index) {
        FluidStack removed = this.oxygenTank.copy();

        this.oxygenTank = FluidStack.EMPTY;
        return removed;
    }

    @Override
    public void setFluid(int index, FluidStack fluidStack) {
        this.oxygenTank = fluidStack.copy();
    }

    @Override
    public void onTankChange(int index, FluidStack previousContents) {

    }
}
