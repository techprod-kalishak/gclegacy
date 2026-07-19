package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.inventory.slot.ConditionalHandlerSlot;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NasaWorkbenchEmptyPageMenu extends AbstractNasaWorkbenchMenu {
    public NasaWorkbenchEmptyPageMenu(int containerId, Inventory inventory, int handlerSize, Level level, BlockPos blockPos) {
        super(GalacticraftMenuType.NASA_WORKBENCH_EMPTY_PAGE.get(), containerId, inventory, handlerSize, level, blockPos);

        addSlot(new ConditionalHandlerSlot(
                this.resourceHandler,
                itemStack -> itemStack.has(GalacticraftDataComponents.SCHEMATIC),
                0,
                80,
                1
        ));
        addStandardInventorySlots(inventory, 18, 111);
    }

    public NasaWorkbenchEmptyPageMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(containerId, inventory, 1, inventory.player.level(), data.readBlockPos());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }
}
