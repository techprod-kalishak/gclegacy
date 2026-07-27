/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.network.payload.MoveVehiclePagePayload;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.AbstractNasaWorkbenchMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public interface NasaWorkbenchScreenPage {
    int getPageIndex();

    AbstractNasaWorkbenchMenu getMenu();

    default Button createNextButton(int width, int height) {
        return Button.builder(GalacticraftComponents.NEXT_PAGE, _ -> ClientPacketDistributor.sendToServer(new MoveVehiclePagePayload(getMenu().containerId, getPageIndex(), MoveVehiclePagePayload.Action.NEXT, getMenu().getBlockPos())))
                .pos(width / 2 - 130, height / 2 - 110)
                .size(40, 20)
                .createNarration(_ -> (MutableComponent) GalacticraftComponents.NEXT_PAGE)
                .build();
    }

    default Button createPreviousButton(int width, int height) {
        return Button.builder(GalacticraftComponents.PREVIOUS_PAGE, _ -> ClientPacketDistributor.sendToServer(new MoveVehiclePagePayload(getMenu().containerId, getPageIndex(), MoveVehiclePagePayload.Action.PREVIOUS, getMenu().getBlockPos())))
                .pos(width / 2 - 130, height / 2 - 85)
                .size(40, 20)
                .createNarration(_ -> (MutableComponent) GalacticraftComponents.PREVIOUS_PAGE)
                .build();
    }

    static Component createTitle(ResourceKey<SchematicVariant> schematicVariantKey) {
        return Component.translatable(Constants.translatable(schematicVariantKey, "title"));
    }
}
