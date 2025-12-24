package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.network.payload.UpdateStoredEnergyPayload;
import io.kalishak.galacticraftlegacy.world.inventory.WorldlyEnergyHandler;
import io.kalishak.galacticraftlegacy.world.inventory.WorldlyResourceHandler;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftDataComponents;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public abstract class AbstractMachineBlockEntity extends NamedBlockEntity {
    protected final NonNullList<ItemStack> items = NonNullList.withSize(size(), ItemStack.EMPTY);
    protected final ItemStacksResourceHandler innerResourceHandler = new ItemStacksResourceHandler(this.items);
    protected final SimpleEnergyHandler energyHandler = new SimpleEnergyHandler(energyCapacity(), maxTransferRate()) {
        @Override
        protected void onEnergyChanged(int previousAmount) {
            AbstractMachineBlockEntity.this.notifyEnergyChange(previousAmount);
        }
    };

    protected AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected static <R extends Resource, M extends AbstractMachineBlockEntity> void registerResourceHandler(BlockCapability<ResourceHandler<R>, @Nullable Direction> capability, BlockEntityType<M> type, @Nullable Direction direction, Function<M, WorldlyResourceHandler<R>> sidedHandler, Function<M, ResourceHandler<R>> defaultHandler, RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                capability,
                type,
                (machine, cxt) ->
                        direction != null ? sidedHandler.apply(machine) : defaultHandler.apply(machine)
        );
    }

    protected static <M extends AbstractMachineBlockEntity> void registerEnergyHandler(BlockEntityType<M> type, @Nullable Direction side, Function<M, WorldlyEnergyHandler> sidedHandler, RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                type,
                (machine, cxt) -> side != null ? sidedHandler.apply(machine) : machine.energyHandler
        );
    }

    protected static <M extends AbstractMachineBlockEntity> void energyTransferTick(ServerLevel level, BlockPos pos, BlockState state, M machine) {
        ItemResource battery = machine.innerResourceHandler.getResource(machine.getBatterySlotIndex());

        if (!battery.isEmpty()) {
            EnergyHandler itemCapacitor = battery.toStack().getCapability(Capabilities.Energy.ITEM, ItemAccess.forHandlerIndex(machine.innerResourceHandler, machine.getBatterySlotIndex()));

            if (itemCapacitor != null && itemCapacitor.getAmountAsInt() > 0) {
                try (Transaction tx = Transaction.open(null)) {
                    if (EnergyHandlerUtil.move(itemCapacitor, machine.energyHandler, machine.maxTransferRate(), tx) > 0) {
                        tx.commit();
                    }
                }
            }
        }
    }

    protected abstract int size();

    protected abstract int getBatterySlotIndex();

    protected int energyCapacity() {
        return 25000;
    }

    protected int maxTransferRate() {
        return 25;
    }

    public void set(int index, ItemResource resource, int amount) {
        this.innerResourceHandler.set(index, resource, amount);
    }

    protected void notifyEnergyChange(int previousAmount) {
        setChanged();
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
}
