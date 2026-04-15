package io.kalishak.galacticraftlegacy.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class GearEquipmentOverlays {
    public static final IClientItemExtensions TANK_EXTENSION = new IClientItemExtensions() {
        @Override
        public void renderFirstPersonOverlay(ItemStack stack, EquipmentSlot equipmentSlot, Player player, GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
            IClientItemExtensions.super.renderFirstPersonOverlay(stack, equipmentSlot, player, guiGraphics, deltaTracker);
        }
    };
}
