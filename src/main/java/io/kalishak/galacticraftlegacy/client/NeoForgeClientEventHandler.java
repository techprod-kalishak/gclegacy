/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.GearInventoryScreen;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.network.payload.ToggleSensorGlassesPayload;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

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
    public void onSoundPlayed(PlaySoundEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;

        stopSound(player, level, event::setSound);
    }

    private static void stopSound(@Nullable Player player, @Nullable Level level, Consumer<@Nullable SoundInstance> setter) {
        if (player != null && level != null) {
            Holder<DimensionType> dimensionTypeHolder = level.dimensionTypeRegistration();

            if (dimensionTypeHolder.is(GalacticraftTags.DimensionTypes.NEEDS_FREQUENCY_MODULE)) {
                ItemStack telemetryUnit = AttachmentHelper.getGearInventory(player).getGearEquipment().get(GearEquipmentSlot.FREQUENCY_MODULE);

                if (telemetryUnit.isEmpty() || !telemetryUnit.is(GalacticraftItems.FREQUENCY_MODULE)) {
                    setter.accept(null);
                }
            }
        }
    }

    @SubscribeEvent
    public void setupScreens(ScreenEvent.Init.Post event) {
        GearInventoryScreen.setupScreen(event);
    }
}
