package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class NasaWorkbenchPageMenu extends AbstractNasaWorkbenchMenu {
    private final VehicleCraftingPage currentPage;

    public NasaWorkbenchPageMenu(int containerId, Inventory inventory, Level level, BlockPos blockPos, VehicleCraftingPage page) {
        super(GalacticraftMenuType.NASA_WORKBENCH_PAGE.get(), containerId, inventory, page.getInputSlotSize() + 1, level, blockPos);
        this.currentPage = page;
        VehicleCraftingPage.populateContainer(page.vehicleRecipe().value(), this::addSlot, this.resourceHandler, this.resourceHandler::set, level, blockPos);
        addStandardInventorySlots(inventory, 8, page.inventoryHeight());
    }

    public NasaWorkbenchPageMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(containerId, inventory, inventory.player.level(), data.readBlockPos(), VehicleCraftingPage.STREAM_CODEC.decode(data).value());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    public VehicleCraftingPage getCurrentPage() {
        return this.currentPage;
    }
}
