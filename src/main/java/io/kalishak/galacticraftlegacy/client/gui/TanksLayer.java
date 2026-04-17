/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui;

import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.config.values.OxygenTankPosition;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class TanksLayer extends GearLayer {
    private static final Identifier EMPTY_TANK_LOCATION = Constants.id("hud/empty_tank");
    private static final Identifier FILLED_TANK_LOCATION = Constants.id("hud/filled_tank");

    @Override
    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (isVisible(Minecraft.getInstance().level, Minecraft.getInstance().player)) {
            ItemStack tank = getStackFromSlot(GearEquipmentSlot.TANK);
            ItemStack additionalTank = getStackFromSlot(GearEquipmentSlot.ADDITIONAL_TANK);
            OxygenTankPosition pos = ClientConfig.OXYGEN_TANKS_POSITION.get();

            int width = graphics.guiWidth();
            int height = graphics.guiHeight();
            int leftX = 10;
            int rightX = 30;
            int topY = height - 57;

            if (pos.onRight()) {
                leftX = width - 59;
                rightX = width - 39;
            }

            if (pos.onTop()) {
                topY = 10;
            }

            renderVisibleTank(graphics, leftX, topY, tank);
            renderVisibleTank(graphics, rightX, topY, additionalTank);
        }
    }

    private void renderVisibleTank(GuiGraphicsExtractor graphics, int x, int y, ItemStack tank) {
        if (!tank.isEmpty()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EMPTY_TANK_LOCATION, 19, 47, 0, 0, x, y, 19, 47);

            ResourceHandler<FluidResource> additionalTankFluid = tank.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(tank));

            if (additionalTankFluid != null) {
                float fill = (float) additionalTankFluid.getAmountAsInt(0) / (float) additionalTankFluid.getCapacityAsInt(0, additionalTankFluid.getResource(0));

                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FILLED_TANK_LOCATION, 17, 45, 0, 0, x + 1, y + 1, 17, 45 - (int) Math.floor(fill));
            }
        }
    }

    private boolean isVisible(Level level, Player player) {
        if (level == null || player == null) {
            return false;
        }

        if (player.hasData(GalacticraftAttachments.PLAYER_SPACE_DATA)) {
            CelestialBodyLevelData data = level.getData(GalacticraftAttachments.CELESTIAL_BODY).value();

            return data.atmosphereInfo().isBreathable();
        }

        return false;
    }
}
