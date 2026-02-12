package io.kalishak.galacticraftlegacy.client.gui;

import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.GuiLayer;

public abstract class GearLayer implements GuiLayer {
    public GearLayer() {

    }

    public ItemStack getStackFromSlot(GearEquipmentSlot gearEquipmentSlot) {
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            GearInventoryProvider gearInventoryProvider = AttachmentHelper.getGearInventory(player);

            return gearInventoryProvider.getStackBySlot(gearEquipmentSlot);
        }

        return ItemStack.EMPTY;
    }
}
