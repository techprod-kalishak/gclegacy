/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.GearInventoryScreen;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.network.payload.ToggleSensorGlassesPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;

public class NeoForgeClientEventHandler {
    NeoForgeClientEventHandler() {}

    @SubscribeEvent
    public void onKeyPressed(InputEvent.Key event) {
        if (Minecraft.getInstance().gui.screen() == null) {
            InputConstants.Key key = InputConstants.getKey(event.getKeyEvent());

            if (GalacticraftKeys.OPEN_GEAR_KEY.isActiveAndMatches(key) && event.getAction() == GLFW.GLFW_RELEASE) {
                ClientPacketDistributor.sendToServer(new ToggleGearInventoryPayload(!Minecraft.getInstance().player.hasContainerOpen()));
            }

            if (GalacticraftKeys.ACTIVATE_SENSOR_GLASSES.isActiveAndMatches(key) && event.getAction() == GLFW.GLFW_RELEASE) {
                ClientPacketDistributor.sendToServer(new ToggleSensorGlassesPayload());
            }
        }
    }

    @SubscribeEvent
    public void setupScreens(ScreenEvent.Init.Post event) {
        GearInventoryScreen.setupScreen(event);
    }
}
