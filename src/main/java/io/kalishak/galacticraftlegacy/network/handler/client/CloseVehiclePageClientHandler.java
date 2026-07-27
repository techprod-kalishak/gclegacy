/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.network.handler.CloseVehiclePagePayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CloseVehiclePageClientHandler {
    public static void handleClient(CloseVehiclePagePayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Player player = cxt.player();

            if (player.containerMenu.containerId == payload.containerId()) {

            }
        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(cxt::disconnect, e));
    }
}
