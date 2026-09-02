/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.server;

import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.inventory.GearInventoryMenu;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleGearInventoryServerHandler {
    public static void handleServer(ToggleGearInventoryPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Player player = cxt.player();

            if (payload.open()) {
                player.openMenu(new SimpleMenuProvider(GearInventoryMenu::new, GalacticraftComponents.INVENTORY_TAB.asComponent()));
            }  else {
                cxt.reply(new ToggleGearInventoryPayload(true));
            }

        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(cxt::disconnect, e));
    }
}
