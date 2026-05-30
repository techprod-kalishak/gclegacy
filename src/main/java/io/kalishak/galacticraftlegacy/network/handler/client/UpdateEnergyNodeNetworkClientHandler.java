/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.network.payload.UpdateEnergyNodeNetworkPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateEnergyNodeNetworkClientHandler {
    public static void handleClient(UpdateEnergyNodeNetworkPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {

        }).exceptionally(e -> Constants.networkFailureMessage(cxt::disconnect, e));
    }
}
