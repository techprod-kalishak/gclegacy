package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.AbstractFluidMachineScreen;
import io.kalishak.galacticraftlegacy.network.payload.UpdateStoredFluidPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateStoredFluidClientHandler {
    public static void handleClient(UpdateStoredFluidPayload msg, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Screen screen = Minecraft.getInstance().screen;

            if (screen instanceof AbstractFluidMachineScreen<?, ?> machineScreen && machineScreen.getMenu().containerId == msg.containerId()) {
                machineScreen.updateTankContents(msg.content(), msg.tankIndex());
            }

        }).exceptionally(t -> {
            cxt.disconnect(Component.translatable("galacticraftlegacy.networking_failed", t.getLocalizedMessage()));

            return null;
        });
    }
}
