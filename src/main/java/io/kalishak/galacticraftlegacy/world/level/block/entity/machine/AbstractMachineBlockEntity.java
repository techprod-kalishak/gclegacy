/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.block.entity.NamedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.energy.VoidingEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class AbstractMachineBlockEntity extends NamedBlockEntity {
    public static final int BASIC_MACHINE_ENERGY_CAPACITY = 25000;
    public static final int BASIC_MACHINE_MAX_TRANSFER_RATE = 500;
    public static final int ADVANCED_MACHINE_ENERGY_CAPACITY = 50000;
    public static final int ADVANCED_MACHINE_MAX_TRANSFER_RATE = 750;
    public static final int MACHINE_ENERGY_LEAK = 5;
    protected final NonNullList<ItemStack> items = NonNullList.withSize(size(), ItemStack.EMPTY);
    protected final ItemStacksResourceHandler innerResourceHandler = new ItemStacksResourceHandler(this.items);
    protected final SimpleEnergyHandler energyHandler = new SimpleEnergyHandler(energyCapacity(), maxTransferRate()) {
        @Override
        protected void onEnergyChanged(int previousAmount) {
            if (!AbstractMachineBlockEntity.this.isRemoved()) {
                AbstractMachineBlockEntity.this.setData(GalacticraftAttachments.SYNC_ENERGY_STORAGE, new SyncedEnergyHandler(previousAmount));
            }
        }
    };

    protected AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected static <M extends AbstractMachineBlockEntity> void registerSingleEnergyInputEnergyHandler(@NonNull Direction direction, BlockEntityType<M> type, RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                type,
                (machine, cxt) -> {
                    if (cxt == null || cxt == direction) {
                        return machine.energyHandler;
                    }

                    return VoidingEnergyHandler.INSTANCE;
                }
        );
    }

    protected static <M extends AbstractMachineBlockEntity> boolean energyTransferTick(M machine, boolean isOperating, boolean enableLeak, int energyBasePerOperation, @Nullable Transaction tx) {
        ItemResource battery = machine.innerResourceHandler.getResource(machine.getBatterySlotIndex());
        boolean doCommit = false;

        if (!battery.isEmpty()) {
            EnergyHandler itemCapacitor = battery.toStack().getCapability(Capabilities.Energy.ITEM, ItemAccess.forHandlerIndex(machine.innerResourceHandler, machine.getBatterySlotIndex()));

            if (itemCapacitor != null && itemCapacitor.getAmountAsInt() > 0) {
                int toMove = Math.min(machine.energyHandler.getCapacityAsInt() - machine.energyHandler.getAmountAsInt(), Math.min(itemCapacitor.getAmountAsInt(), machine.maxTransferRate()));

                try (Transaction childTx = Transaction.open(tx)) {
                    if (EnergyHandlerUtil.move(itemCapacitor, machine.energyHandler, toMove, childTx) > 0) {
                        childTx.commit();
                        doCommit = true;
                    }
                }
            }
        }

        int toExtract = 0;

        if (isOperating) {
            toExtract = energyBasePerOperation;
        } else if (enableLeak) {
            toExtract = MACHINE_ENERGY_LEAK;
        }

        if (toExtract > 0) {
            try (Transaction childTx = Transaction.open(tx)) {
                if (machine.energyHandler.extract(toExtract, childTx) > 0) {
                    childTx.commit();
                    doCommit = true;
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
        return machine.items.subList(ingredientSlotStart, ingredientSlotEnd).stream().noneMatch(ItemStack::isEmpty);
    }

    protected static boolean hasRequiredIngredients(ResourceHandler<ItemResource> resourceHandler, int ingredientSlotStart, int ingredientSlotEnd) {
        for (int i = ingredientSlotStart; i < ingredientSlotEnd; i++) {
            if (resourceHandler.getResource(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    protected abstract int size();

    protected abstract int getBatterySlotIndex();

    protected int energyCapacity() {
        return BASIC_MACHINE_ENERGY_CAPACITY;
    }

    protected int maxTransferRate() {
        return BASIC_MACHINE_MAX_TRANSFER_RATE / 10;
    }

    protected boolean hasEnergyToOperate() {
        return this.energyHandler.getAmountAsInt() > maxTransferRate();
    }

    public void set(int index, ItemResource resource, int amount) {
        this.innerResourceHandler.set(index, resource, amount);
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
        this.innerResourceHandler.deserialize(input);
        this.energyHandler.deserialize(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.innerResourceHandler.serialize(output);
        this.energyHandler.serialize(output);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        componentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.items);
        this.energyHandler.set(componentGetter.getOrDefault(GalacticraftDataComponents.STORED_ENERGY, 0));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
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

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        if (this.level != null) {
            Containers.dropContents(this.level, pos, this.items);
        }
    }
}
