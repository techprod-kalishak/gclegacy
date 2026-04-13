package io.kalishak.galacticraftlegacy.client.gui;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class SensorGlassesOverlay implements GuiLayer {
    private static final Identifier GUI = Constants.id("gui/sensor_glasses.png");
    private int zoom = 0;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        ItemStack sensorGlasses = player.getItemBySlot(EquipmentSlot.HEAD);

        if (sensorGlasses.is(GalacticraftItems.SENSOR_GLASSES)) {
            this.zoom++;

            float angle = (float) Math.sin(this.zoom / 80.0F) * 0.1F + 0.1F;
            int width = guiGraphics.guiWidth();
            int height = guiGraphics.guiHeight();

            guiGraphics.pose().pushMatrix();

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI, 0, 0, 0.0F, 0.0F, width, height, 512, 256);
            guiGraphics.pose().rotate(angle);
            guiGraphics.pose().popMatrix();
        }
    }
}
