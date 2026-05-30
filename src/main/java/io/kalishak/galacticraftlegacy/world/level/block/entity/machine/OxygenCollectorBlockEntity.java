/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.NamedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.VoidingResourceHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.energy.VoidingEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class OxygenCollectorBlockEntity extends NamedBlockEntity {
    private @NonNull FluidStack fluid = FluidStack.EMPTY;
    private final SingleTankResourceHandler fluidResourceHandler = new SingleTankResourceHandler() {
        @Override
        public FluidStack getFluidStack() {
            return OxygenCollectorBlockEntity.this.fluid;
        }

        @Override
        public void setFluidStack(FluidStack stack) {
            OxygenCollectorBlockEntity.this.fluid = stack;
        }

        @Override
        public int getCapacity() {
            return 8000;
        }
    };
    private final SimpleEnergyHandler energyHandler = new SimpleEnergyHandler(25000, 250) {
        @Override
        protected void onEnergyChanged(int previousAmount) {
            if (!OxygenCollectorBlockEntity.this.isRemoved()) {
                OxygenCollectorBlockEntity.this.setData(GalacticraftAttachments.SYNC_ENERGY_STORAGE, new SyncedEnergyHandler(previousAmount));
            }
        }
    };
    public boolean active;
    public static final int OUTPUT_PER_TICK = 100;
    public static float OXYGEN_PER_PLANT = 0.75F;
    public float lastOxygenCollected;
    private boolean noAtmosphericOxygen;
    private boolean isInitialised;
    private boolean producedLastTick;

    public OxygenCollectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(), pos, blockState);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(),
                (blockEntity, context) -> {
                    if (context == null || context == Direction.DOWN) {
                        return blockEntity.fluidResourceHandler;
                    }

                    return new VoidingResourceHandler<>(FluidResource.EMPTY);
                }
        );
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(),
                (blockEntity, context) -> {
                    if (context == null || context == Direction.EAST) {
                        return blockEntity.energyHandler;
                    }

                    return VoidingEnergyHandler.INSTANCE;
                }
        );
    }

    public static void serverTick(ServerLevel level, BlockPos worldPosition, BlockState blockState, OxygenCollectorBlockEntity blockEntity) {

    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.galacticraftlegacy.oxygen_collector");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.level != null && !this.level.isClientSide()) {
            setData(GalacticraftAttachments.SYNC_ENERGY_STORAGE, new SyncedEnergyHandler(this.energyHandler.getAmountAsInt()));
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.fluidResourceHandler.deserialize(input);
        this.energyHandler.deserialize(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.fluidResourceHandler.serialize(output);
        this.energyHandler.serialize(output);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        this.fluid = componentGetter.getOrDefault(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY).copy();
        this.energyHandler.set(componentGetter.getOrDefault(GalacticraftDataComponents.STORED_ENERGY, 0));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(this.fluid));
        components.set(GalacticraftDataComponents.STORED_ENERGY, this.energyHandler.getAmountAsInt());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
