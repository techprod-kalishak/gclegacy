package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.AbstractMachineScreen;
import io.kalishak.galacticraftlegacy.network.payload.UpdateStoredEnergyPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateStoredEnergyClientHandler {
    public static void handleClient(UpdateStoredEnergyPayload msg, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Screen screen = Minecraft.getInstance().screen;

            if (screen instanceof AbstractMachineScreen<?, ?> machineScreen && msg.containerId() == machineScreen.getMenu().containerId) {
                machineScreen.updateEnergy(msg.newAmount());
            }

        }).exceptionally(e -> {
            cxt.disconnect(Component.translatable("galacticraftlegacy.networking_failed", e.getLocalizedMessage()));
            return null;
        });
    }
}
