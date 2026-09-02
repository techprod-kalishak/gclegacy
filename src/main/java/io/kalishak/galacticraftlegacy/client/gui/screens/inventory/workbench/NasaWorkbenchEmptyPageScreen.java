/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.network.payload.UnlockSchematicPayload;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchEmptyPageMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.Nullable;

public class NasaWorkbenchEmptyPageScreen extends AbstractNasaWorkbenchScreen<NasaWorkbenchEmptyPageMenu> implements NasaWorkbenchScreenPage {
    public static final Identifier BACKGROUND = Constants.texture("gui/container/workbench_page/empty.png");

    public NasaWorkbenchEmptyPageScreen(NasaWorkbenchEmptyPageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 176, BACKGROUND);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(GalacticraftComponents.UNLOCK_SCHEMATIC.asComponent(), _ -> ClientPacketDistributor.sendToServer(new UnlockSchematicPayload(this.menu.containerId)))
                .pos(this.width / 2 - 46, this.height / 2 - 32)
                .size(92, 20)
                .createNarration(_ -> GalacticraftComponents.UNLOCK_SCHEMATIC.asComponent())
                .build()
        );
    }

    @Override
    public @Nullable ResourceKey<SchematicVariant> getPage() {
        return null;
    }

    @Override
    protected boolean activateNextButton() {
        return false;
    }
}
