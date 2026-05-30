/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network;

import io.kalishak.galacticraftlegacy.network.handler.client.ToggleGearInventoryClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.client.UpdateEnergyNodeNetworkClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.client.UpdateFluidNodeNetworkClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.server.ToggleGearInventoryServerHandler;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.network.payload.UpdateEnergyNodeNetworkPayload;
import io.kalishak.galacticraftlegacy.network.payload.UpdateFluidNodeNetworkPayload;
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
        registrar.playToClient(
                UpdateFluidNodeNetworkPayload.TYPE,
                UpdateFluidNodeNetworkPayload.STREAM_CODEC,
                UpdateFluidNodeNetworkClientHandler::handleClient
        );
        registrar.playToClient(
                UpdateEnergyNodeNetworkPayload.TYPE,
                UpdateEnergyNodeNetworkPayload.STREAM_CODEC,
                UpdateEnergyNodeNetworkClientHandler::handleClient
        );
    }
}
