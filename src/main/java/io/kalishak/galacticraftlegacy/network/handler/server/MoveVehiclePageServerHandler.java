/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.server;

import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench.NasaWorkbenchScreenPage;
import io.kalishak.galacticraftlegacy.network.payload.MoveVehiclePagePayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.Nullable;

public class MoveVehiclePageServerHandler {
    public static void handleServer(MoveVehiclePagePayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Player targetPlayer = cxt.player();

            if (targetPlayer.containerMenu.containerId == payload.containerId()) {
                switch (targetPlayer.containerMenu) {
                    case NasaWorkbenchEmptyPageMenu _ -> openExactPage(targetPlayer, payload.blockPos(), Schematics.getLastPage(targetPlayer));
                    case NasaWorkbenchMenu _ -> pickMenu(targetPlayer, SchematicVariants.TIER_1_ROCKET, payload.action(), payload.blockPos());

                    default -> payload.currentSchematic().ifPresentOrElse(
                            key -> pickMenu(targetPlayer, key, payload.action(), payload.blockPos()),
                            () -> openExactPage(targetPlayer, payload.blockPos(), Schematics.getLastPage(targetPlayer))
                    );
                }
            }
        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(cxt::disconnect, e));
    }

    private static void pickMenu(Player serverPlayer, ResourceKey<SchematicVariant> currentSchematic, Schematics.Picker picker, BlockPos blockPos) {
        if (currentSchematic.equals(SchematicVariants.TIER_1_ROCKET)) {
            openMainPage(serverPlayer, blockPos);
        }

        Holder<VehicleCraftingPage> page = Schematics.getPage(serverPlayer, currentSchematic, picker);

        if (page == null) {
            openEmptyPage(serverPlayer, blockPos);
        } else {
            openExactPage(serverPlayer, blockPos, page);
        }
    }

    private static void openExactPage(Player targetPlayer, BlockPos blockPos, Holder<VehicleCraftingPage> page) {
        if (page == null) {
            openMainPage(targetPlayer, blockPos);
        } else {
            targetPlayer.openMenu(
                    new NasaWorkbenchMenuSupplier(
                            (containerId, inventory, level, pos) -> new NasaWorkbenchPageMenu(containerId, inventory, level, pos, page.value()),
                            blockPos,
                            NasaWorkbenchScreenPage.createTitle(page.value().schematic().getKey())
                    ), buf -> {
                        buf.writeBlockPos(blockPos);
                        VehicleCraftingPage.STREAM_CODEC.encode(buf, page);
                    }
            );
        }
    }

    private static void openEmptyPage(Player targetPlayer, BlockPos blockPos) {
        targetPlayer.openMenu(
                new NasaWorkbenchMenuSupplier(
                        NasaWorkbenchEmptyPageMenu::new,
                        blockPos,
                        GalacticraftComponents.NEW_SCHEMATIC
                ), blockPos
        );
    }

    private static void openMainPage(Player player, BlockPos blockPos) {
        player.openMenu(
                new NasaWorkbenchMenuSupplier(
                        NasaWorkbenchMenu::new,
                        blockPos,
                        NasaWorkbenchScreenPage.createTitle(SchematicVariants.TIER_1_ROCKET)
                ), blockPos
        );
    }

    private record NasaWorkbenchMenuSupplier(MenuConstructor constructor, BlockPos blockPos, Component title) implements MenuProvider {
        @Override
        public Component getDisplayName() {
            return this.title;
        }

        @Override
        public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
            return this.constructor.createMenu(containerId, inventory, player.level(), this.blockPos);
        }
    }

    @FunctionalInterface
    private interface MenuConstructor {
        AbstractNasaWorkbenchMenu createMenu(int containerId, Inventory inventory, Level level, BlockPos blockPos);
    }
}
