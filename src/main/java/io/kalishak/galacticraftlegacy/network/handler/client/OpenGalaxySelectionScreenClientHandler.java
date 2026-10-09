package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.client.gui.screens.galaxy.CelestialSelectionScreen;
import io.kalishak.galacticraftlegacy.network.payload.OpenGalaxySelectionScreenPayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenGalaxySelectionScreenClientHandler {
    public static void handle(OpenGalaxySelectionScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();

            if (player instanceof LocalPlayer) {
                IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();

                if (server != null) {
                    Minecraft.getInstance().setScreenAndShow(CelestialSelectionScreen.create(server.registryAccess(), server.getDataStorage(), !payload.canTransit(), true));
                }
            }

        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(context::disconnect, e));
    }
}
