package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.network.payload.UpdateStoredEnergyPayload;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.AbstractMachineBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.DelegatingEnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public abstract class AbstractMachineMenu<M extends AbstractMachineBlockEntity> extends RecipeBookMenu {
    protected final ContainerData containerData;
    protected final M machine;
    protected final ResourceHandler<ItemResource> resourceHandler;
    protected final EnergyHandler energyHandler;
    protected final Player player;

    protected AbstractMachineMenu(MenuType<? extends AbstractMachineMenu<M>> menuType, int containerId, Inventory playerInventory, M machine, ContainerData containerData) {
        super(menuType, containerId);
        this.containerData = containerData;
        this.machine = machine;
        this.resourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, machine, null);
        this.energyHandler = new DelegatingEnergyHandler(() -> ResourcefulHelper.getEnergyHandler(machine, null));
        this.player = playerInventory.player;
        addDataSlots(containerData);
    }

    public int getEnergyCapacity() {
        return this.energyHandler.getCapacityAsInt();
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (this.player.containerMenu.containerId == this.containerId && this.player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new UpdateStoredEnergyPayload(this.containerId, AbstractMachineMenu.this.energyHandler.getAmountAsInt()));
        }
    }
}
