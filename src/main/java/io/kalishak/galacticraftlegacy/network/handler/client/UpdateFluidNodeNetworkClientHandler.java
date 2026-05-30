/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.client;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.network.payload.UpdateFluidNodeNetworkPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateFluidNodeNetworkClientHandler {
    public static void handleClient(UpdateFluidNodeNetworkPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {

        }).exceptionally(e -> Constants.networkFailureMessage(cxt::disconnect, e));
    }
}
