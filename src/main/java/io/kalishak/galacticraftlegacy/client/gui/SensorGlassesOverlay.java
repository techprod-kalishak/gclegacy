/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui;

import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class SensorGlassesOverlay implements GuiLayer {
    private static final Identifier GUI = Constants.id("textures/gui/sensor_glasses.png");
    private int zoom = 0;

    @Override
    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        assert player != null;

        PlayerSpaceData spaceData = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);

        if (spaceData.isSensorGlassesActivated()) {
            ItemStack sensorGlasses = player.getItemBySlot(EquipmentSlot.HEAD);

            if (sensorGlasses.is(GalacticraftItems.SENSOR_GLASSES)) {
                this.zoom++;

                float angle = (float) Math.sin(this.zoom / 80.0F) * 0.1F + 0.1F;
                int width = graphics.guiWidth();
                int height = graphics.guiHeight();

                graphics.pose().pushMatrix();
                graphics.pose().rotate(angle);
                graphics.blit(RenderPipelines.GUI_TEXTURED, GUI, 0, 0, 0.0F, 0.0F, width, height, 512, 256);
                graphics.pose().popMatrix();
            }
        }
    }
}
