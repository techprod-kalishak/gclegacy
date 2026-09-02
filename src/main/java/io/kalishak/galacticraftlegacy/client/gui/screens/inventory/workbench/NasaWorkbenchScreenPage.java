/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
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
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface NasaWorkbenchScreenPage {
    @Nullable ResourceKey<SchematicVariant> getPage();

    AbstractNasaWorkbenchMenu getMenu();

    default Optional<ResourceKey<SchematicVariant>> getOptionalPage() {
        return Optional.ofNullable(getPage());
    }

    default Button createNextButton(int width, int height) {
        return Button.builder(GalacticraftComponents.NEXT_PAGE.asComponent(), _ -> ClientPacketDistributor.sendToServer(new MoveVehiclePagePayload(getMenu().containerId, getOptionalPage(), Schematics.Picker.NEXT, getMenu().getBlockPos())))
                .pos(width / 2 - 130, height / 2 - 110)
                .size(40, 20)
                .createNarration(_ -> GalacticraftComponents.NEXT_PAGE.asComponent())
                .build();
    }

    default Button createPreviousButton(int width, int height) {
        return Button.builder(GalacticraftComponents.PREVIOUS_PAGE.asComponent(), _ -> ClientPacketDistributor.sendToServer(new MoveVehiclePagePayload(getMenu().containerId, getOptionalPage(), Schematics.Picker.PREVIOUS, getMenu().getBlockPos())))
                .pos(width / 2 - 130, height / 2 - 85)
                .size(40, 20)
                .createNarration(_ -> GalacticraftComponents.PREVIOUS_PAGE.asComponent())
                .build();
    }

    static Component createTitle(ResourceKey<SchematicVariant> schematicVariantKey) {
        return Component.translatable(Constants.translatable(schematicVariantKey, "title"));
    }
}
