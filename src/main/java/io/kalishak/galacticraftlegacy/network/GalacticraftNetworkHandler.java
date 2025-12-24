package io.kalishak.galacticraftlegacy.network;

import io.kalishak.galacticraftlegacy.network.handler.client.UpdateStoredEnergyClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.server.ToggleGearInventoryServerHandler;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.network.payload.UpdateStoredEnergyPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class GalacticraftNetworkHandler {
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                ToggleGearInventoryPayload.TYPE,
                ToggleGearInventoryPayload.STREAM_CODEC,
                ToggleGearInventoryServerHandler::handle
        );
        registrar.playToClient(
                UpdateStoredEnergyPayload.TYPE,
                UpdateStoredEnergyPayload.STREAM_CODEC,
                UpdateStoredEnergyClientHandler::handle
        );
    }
}
