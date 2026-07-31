/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractNasaWorkbenchMenu extends AbstractContainerMenu {
    protected final ContainerLevelAccess access;
    protected final Player player;
    private final BlockPos blockPos;
    protected final ItemStacksResourceHandler resourceHandler;

    protected AbstractNasaWorkbenchMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, int handlerSize, Level level, BlockPos blockPos) {
        super(menuType, containerId);
        this.access = ContainerLevelAccess.create(level, blockPos);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.resourceHandler = new ItemStacksResourceHandler(handlerSize);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
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
