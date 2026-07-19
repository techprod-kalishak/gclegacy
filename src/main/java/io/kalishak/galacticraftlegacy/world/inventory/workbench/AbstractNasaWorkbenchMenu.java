package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

public abstract class AbstractNasaWorkbenchMenu extends AbstractContainerMenu {
    protected final ContainerLevelAccess access;
    protected final Player player;
    protected final ItemStacksResourceHandler resourceHandler;

    protected AbstractNasaWorkbenchMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, int handlerSize, Level level, BlockPos blockPos) {
        super(menuType, containerId);
        this.access = ContainerLevelAccess.create(level, blockPos);
        this.player = inventory.player;
        this.resourceHandler = new ItemStacksResourceHandler(handlerSize);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        VehicleCraftingMenuProvider.removed(this.access, this.resourceHandler, player);
    }

    @Override
    public boolean stillValid(Player player) {
        return VehicleCraftingMenuProvider.stillValid(this.access, player);
    }
}
