/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.TankWrapper;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.WorldlyTankWrapper;
import io.kalishak.galacticraftlegacy.transfer.node.NodeNetwork;
import io.kalishak.galacticraftlegacy.world.inventory.container.Tank;
import io.kalishak.galacticraftlegacy.world.inventory.container.WorldlyStorage;
import io.kalishak.galacticraftlegacy.world.inventory.container.WorldlyTank;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.block.entity.BaseItemStorageBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.*;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.SequencedCollection;

public abstract class AbstractMachineBlockEntity extends BaseItemStorageBlockEntity implements TransmitterBlockEntity {
    public static final int BASIC_MACHINE_ENERGY_CAPACITY = 25000;
    public static final int BASIC_MACHINE_MAX_TRANSFER_RATE = 500;
    public static final int ADVANCED_MACHINE_ENERGY_CAPACITY = 50000;
    public static final int ADVANCED_MACHINE_MAX_TRANSFER_RATE = 750;
    public static final int MACHINE_ENERGY_LEAK = 5;
    protected final SimpleEnergyHandler capacitor = new SimpleEnergyHandler(getMaxEnergy(), getMaxEnergyTransferRate()) {
        @Override
        protected void onEnergyChanged(int previousAmount) {
            if (!AbstractMachineBlockEntity.this.isRemoved()) {
                AbstractMachineBlockEntity.this.setData(GalacticraftAttachments.SYNC_ENERGY_STORAGE, new SyncedEnergyHandler(previousAmount));
            }
        }
    };
    protected MachineStatus machineStatus;

    protected AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected static <M extends AbstractMachineBlockEntity> void registerItemCapability(BlockEntityType<M> type, RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                type,
                (entity, side) -> {
                    if (side == null) {
                        return entity.items;
                    }

                    if (entity instanceof WorldlyStorage worldlyStorage) {
                        SequencedCollection<ResourceHandler<ItemResource>> collection = new ArrayList<>();
                        int[] slotsForFace = worldlyStorage.getSlotsForFace(side);

                        for (int i : slotsForFace) {
                            collection.add(RangedResourceHandler.ofSingleIndex(() -> entity.items, i));
                        }

                        return new CombinedResourceHandler<>(collection);
                    }

                    return entity.items;
                }
        );
    }

    protected static <M extends AbstractMachineBlockEntity> void registerFluidCapability(BlockEntityType<M> type, RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                type,
                (blockEntity, context) -> {
                    if (blockEntity instanceof WorldlyTank worldlyTank) {
                        return new WorldlyTankWrapper(worldlyTank, context);
                    } else if (blockEntity instanceof Tank tank) {
                        return new TankWrapper(tank);
                    }

                    return null;
                }
        );
    }

    protected static <M extends AbstractMachineBlockEntity> void registerEnergyCapability(BlockEntityType<M> type, RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                type,
                (machine, _) -> machine.capacitor
        );
    }

    protected static <M extends AbstractMachineBlockEntity> boolean extractBattery(M machine, boolean enableLeak, int energyBasePerOperation, @Nullable Transaction tx) {
        ItemStack battery = machine.getItem(machine.getBatterySlotIndex());
        boolean doCommit = false;

        if (!battery.isEmpty()) {
            EnergyHandler itemCapacitor = battery.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(battery));

            if (itemCapacitor != null && itemCapacitor.getAmountAsInt() > 0) {
                int toMove = Math.min(itemCapacitor.getAmountAsInt(), machine.getMaxEnergyTransferRate());

                try (Transaction childTx = Transaction.open(tx)) {
                    if (EnergyHandlerUtil.move(itemCapacitor, machine.capacitor, toMove, childTx) > 0) {
                        childTx.commit();
                        doCommit = true;
                    }
                }
            }
        }

        return doCommit;
    }

    /**
     * Checks whether all ingredients can be considered a recipe
     * @param machine producer
     * @param ingredientSlotStart inclusive slot index
     * @param ingredientSlotEnd exclusive slot index
     * @return true if all the slots are filled
     */
    protected static boolean hasRequiredIngredients(AbstractMachineBlockEntity machine, int ingredientSlotStart, int ingredientSlotEnd) {
        return machine.items.copyToList().subList(ingredientSlotStart, ingredientSlotEnd).stream().noneMatch(ItemStack::isEmpty);
    }

    protected static boolean hasRequiredIngredients(NonNullList<ItemStack> items, int ingredientSlotStart, int ingredientSlotEnd) {
        for (int i = ingredientSlotStart; i < ingredientSlotEnd; i++) {
            if (items.get(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    protected static void extractIngredients(NonNullList<ItemStack> items, int startSlot, int endSlot) {
        for (int i = startSlot; i <= endSlot; i++) {
            ItemStack stack = items.get(i).copy();
            stack.shrink(1);
            items.set(i, stack);
        }
    }

    protected static NodeNetwork getNetwork(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));

            if (blockEntity instanceof TransmitterBlockEntity t) {
                return t.getNetwork(direction);
            }
        }

        return null;
    }

    protected void setMachineStatus(MachineStatus machineStatus) {
        this.machineStatus = machineStatus;
        setData(GalacticraftAttachments.MACHINE_STATUS, machineStatus);
    }

    protected MachineStatus getMachineStatus() {
        return this.machineStatus;
    }

    protected abstract int getBatterySlotIndex();

    protected int[] getBatterySlots() {
        return new int[] { getBatterySlotIndex() };
    }

    protected int getMaxEnergy() {
        return BASIC_MACHINE_ENERGY_CAPACITY;
    }

    protected int getMaxEnergyTransferRate() {
        return BASIC_MACHINE_MAX_TRANSFER_RATE;
    }

    protected boolean hasEnergyToOperate() {
        return this.capacitor.getAmountAsInt() > getMaxEnergyTransferRate();
    }

    protected void onItemChange(int slot, ItemStack previousStack) {
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.level != null && !this.level.isClientSide()) {
            setData(GalacticraftAttachments.SYNC_ENERGY_STORAGE, new SyncedEnergyHandler(this.capacitor.getAmountAsInt()));
        }
    }

    @Override
    public void updateNeighbouringTransmitters(Level level, BlockPos pos) {

    }

    @Override
    public boolean hasNetwork() {
        NodeNetwork network = getNetwork(this.level, this.worldPosition);
        return network != null;
    }

    @Override
    public void addNetwork(NodeNetwork network) {
        network.addTransmitter(this);
    }

    @Override
    public void updateNetwork() {

    }

    @Override
    public void onNetworkUpdate() {

    }

    @Override
    public NodeNetwork getNetwork(@Nullable Direction side) {
        return getNetwork(this.level, this.worldPosition);
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType networkType) {
        return switch (networkType) {
            case POWER -> direction == Direction.EAST;
            case FLUID -> this instanceof AbstractFluidTankMachineBlockEntity && direction == Direction.DOWN;
            default -> false;
        };
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.capacitor.deserialize(input);
        MachineStatus.deserialize(input, this::setMachineStatus);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.capacitor.serialize(output);
        MachineStatus.serialize(output, getMachineStatus());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.capacitor.set(components.getOrDefault(GalacticraftDataComponents.STORED_ENERGY, 0));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(GalacticraftDataComponents.STORED_ENERGY, this.capacitor.getAmountAsInt());
    }

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
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
