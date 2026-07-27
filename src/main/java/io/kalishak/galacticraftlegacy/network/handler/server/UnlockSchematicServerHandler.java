/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.handler.server;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.network.payload.UnlockSchematicPayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchEmptyPageMenu;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UnlockSchematicServerHandler {
    public static void handleServer(UnlockSchematicPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Player player = cxt.player();
            AbstractContainerMenu menu = player.containerMenu;

            if (payload.playerId().equals(player.getUUID())) {
                if (menu.containerId == payload.containerId() && menu instanceof NasaWorkbenchEmptyPageMenu pageMenu) {
                    Schematics schematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();
                    ItemStack inSlot = pageMenu.getSlot(0).getItem();
                    Holder<SchematicVariant> schematicVariant = inSlot.get(GalacticraftDataComponents.SCHEMATIC);

                    if (schematicVariant != null && !schematics.isUnlocked(schematicVariant.getKey())) {
                        schematics.unlock(schematicVariant.getKey());
                        pageMenu.onSchematicUnlocked();
                    }
                }
            }

        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(cxt::disconnect, e));
    }
}
