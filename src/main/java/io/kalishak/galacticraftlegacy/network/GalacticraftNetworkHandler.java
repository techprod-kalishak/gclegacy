/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network;

import io.kalishak.galacticraftlegacy.network.handler.CloseVehiclePagePayload;
import io.kalishak.galacticraftlegacy.network.handler.client.CloseVehiclePageClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.client.ToggleGearInventoryClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.client.UpdateEnergyNodeNetworkClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.client.UpdateFluidNodeNetworkClientHandler;
import io.kalishak.galacticraftlegacy.network.handler.server.MoveVehiclePageServerHandler;
import io.kalishak.galacticraftlegacy.network.handler.server.ToggleGearInventoryServerHandler;
import io.kalishak.galacticraftlegacy.network.handler.server.ToggleSensorGlassesServerHandler;
import io.kalishak.galacticraftlegacy.network.handler.server.UnlockSchematicServerHandler;
import io.kalishak.galacticraftlegacy.network.payload.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
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

        registrar.playToServer(
                ToggleSensorGlassesPayload.TYPE,
                ToggleSensorGlassesPayload.STREAM_CODEC,
                ToggleSensorGlassesServerHandler::handleServer
        );
        registrar.playToServer(
                MoveVehiclePagePayload.TYPE,
                MoveVehiclePagePayload.STREAM_CODEC,
                MoveVehiclePageServerHandler::handleServer
        );
        registrar.playToServer(
                UnlockSchematicPayload.TYPE,
                UnlockSchematicPayload.STREAM_CODEC,
                UnlockSchematicServerHandler::handleServer
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
        registrar.playToClient(
                CloseVehiclePagePayload.TYPE,
                CloseVehiclePagePayload.STREAM_CODEC,
                CloseVehiclePageClientHandler::handleClient
        );
    }
}
