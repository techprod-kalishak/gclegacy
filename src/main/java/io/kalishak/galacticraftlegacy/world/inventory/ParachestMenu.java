package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.network.payload.UpdateStoredFluidPayload;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.ParachestBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class ParachestMenu extends AbstractContainerMenu {
    private final ResourceHandler<ItemResource> resourceHandler;
    private final ResourceHandler<FluidResource> fluidResourceHandler;
    private final ParachestBlockEntity parachest;
    private final Player player;
    private final int slotCount;
    private final int rows;

    public ParachestMenu(int containerId, Inventory playerInventory, ParachestBlockEntity parachest, int slotCount) {
        super(GalacticraftMenuType.PARACHEST.get(), containerId);
        assert slotCount % 3 != 0;
        this.resourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, parachest, null);
        this.fluidResourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Fluid.BLOCK, FluidResource.EMPTY, parachest, null);
        this.parachest = parachest;
        this.player = playerInventory.player;
        this.slotCount = slotCount;
        this.rows = (this.slotCount - 3) / 9;

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new ResourceHandlerSlot(
                        this.resourceHandler,
                        (index, resource, amount) -> {},
                        j + i * 9,
                        8 + j * 18,
                        18 + j * 18
                ));
            }
        }

        addSlot(new ResourceHandlerSlot(this.resourceHandler, (index, resource, amount) -> {}, resourceHandler.size() - 3, 125, (this.rows == 0 ? 24 : 26) + this.rows * 18));
        addSlot(new ResourceHandlerSlot(this.resourceHandler, (index, resource, amount) -> {}, resourceHandler.size() - 2, 125 + 18, (this.rows == 0 ? 24 : 26) + this.rows * 18));
        addSlot(new ResourceHandlerSlot(this.resourceHandler, (index, resource, amount) -> {}, resourceHandler.size() - 1,  75, (this.rows == 0 ? 24 : 26) + this.rows * 18));

        addStandardInventorySlots(playerInventory, 18, (this.rows == 0 ? 116 : 118));
    }

    public ParachestMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.PARACHEST.get(), playerInventory.player.level(), data), data.readOptional(ByteBufCodecs.VAR_INT).orElse(0));
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (this.player.containerMenu.containerId == this.containerId && this.player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new UpdateStoredFluidPayload(this.containerId, FluidUtil.getStack(this.fluidResourceHandler, 0), 0));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        final int slotsCount = this.slots.size();

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < this.resourceHandler.size()) {
                if (!this.moveItemStackTo(itemstack1, slotsCount - 36, slotsCount, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.resourceHandler.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this.parachest, player);
    }
}
