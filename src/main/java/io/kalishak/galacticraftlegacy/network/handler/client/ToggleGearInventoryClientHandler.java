/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.GearInventoryScreen;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleGearInventoryClientHandler {
    public static void handleClient(ToggleGearInventoryPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Screen screen = mc.screen;

            if (payload.open() && screen instanceof GearInventoryScreen) {
                mc.player.closeContainer();
                mc.setScreen(new InventoryScreen(mc.player));
            } else {
                mc.setScreen(null);
            }
        }).exceptionally(e -> {
            cxt.disconnect(Component.translatable("galacticraftlegacy.networking_failed"));
            return null;
        });
    }
}
