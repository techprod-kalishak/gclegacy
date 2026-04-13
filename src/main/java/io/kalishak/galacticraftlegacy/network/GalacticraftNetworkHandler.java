package io.kalishak.galacticraftlegacy.network;

import io.kalishak.galacticraftlegacy.network.handler.client.ToggleGearInventoryClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.server.ToggleGearInventoryServerHandler;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class GalacticraftNetworkHandler {
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playBidirectional(
                ToggleGearInventoryPayload.TYPE,
                ToggleGearInventoryPayload.STREAM_CODEC,
                ToggleGearInventoryServerHandler::handleServer,
                ToggleGearInventoryClientHandler::handleClient
        );
    }
}
