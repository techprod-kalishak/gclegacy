/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.GearInventoryScreen;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleGearInventoryClientHandler {
    public static void handleClient(ToggleGearInventoryPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Gui gui = mc.gui;

            if (payload.open() && gui.screen() instanceof GearInventoryScreen) {
                mc.player.closeContainer();
                gui.setScreen(new InventoryScreen(mc.player));
            } else {
                gui.setScreen(null);
            }
        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(cxt::disconnect, e));
    }
}
