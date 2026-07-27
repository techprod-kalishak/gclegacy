/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.server;

import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.network.payload.MoveVehiclePagePayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.Nullable;

public class MoveVehiclePageServerHandler {
    public static void handleServer(MoveVehiclePagePayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Player targetPlayer = cxt.player();
            int pageIndex = payload.currentPage();
            int nextPageIndex = payload.action().apply(pageIndex);

            if (pageIndex != nextPageIndex) {
                if (targetPlayer.containerMenu.containerId == payload.containerId() && targetPlayer.containerMenu instanceof AbstractNasaWorkbenchMenu nasaWorkbenchMenu) {
                    pickMenu(targetPlayer, pageIndex, nextPageIndex, payload.action(), payload.blockPos());
                }
            }

        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(cxt::disconnect, e));
    }

    private static void pickMenu(Player serverPlayer, int pageIndex, int nextPageIndex, MoveVehiclePagePayload.Action action, BlockPos blockPos) {
        if (nextPageIndex == 0) {
            serverPlayer.openMenu(
                    new SimpleMenu((containerId, inventory, player) -> new NasaWorkbenchMenu(containerId, inventory, player.level(), blockPos)),
                    blockPos
            );
        }

        Holder<VehicleCraftingPage> page = Schematics.getPage(serverPlayer, pageIndex, action.apply(pageIndex));

        if (page == null) {
            serverPlayer.openMenu(
                    new SimpleMenu((containerId, inventory, player) -> new NasaWorkbenchEmptyPageMenu(containerId, inventory, player.level(), player.blockPosition())),
                    blockPos
            );
        } else {
            serverPlayer.openMenu(
                    new SimpleMenu((containerId, inventory, player) -> new NasaWorkbenchPageMenu(containerId, inventory, player.level(), player.blockPosition(), page.value())),
                    buf -> {
                        buf.writeBlockPos(blockPos);
                        VehicleCraftingPage.STREAM_CODEC.encode(buf, page);
                    }
            );
        }
    }

    private record SimpleMenu(MenuConstructor constructor) implements MenuProvider {
        @Override
        public Component getDisplayName() {
            return Component.empty();
        }

        @Override
        public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
            return this.constructor.createMenu(containerId, inventory, player);
        }
    }
}
