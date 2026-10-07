package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.client.gui.screens.galaxy.CelestialSelectionScreen;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBody;
import io.kalishak.galacticraftlegacy.network.payload.OpenGalaxySelectionScreenPayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public class OpenGalaxySelectionScreenClientHandler {
    public static void handle(OpenGalaxySelectionScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();

            if (player instanceof LocalPlayer localPlayer) {
                List<CelestialBody> registered = localPlayer.registryAccess().lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL_BODY).stream().toList();
                Minecraft.getInstance().setScreenAndShow(new CelestialSelectionScreen(localPlayer.registryAccess(), !payload.canTransit(), registered, true));

            }

        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(context::disconnect, e));
    }
}
