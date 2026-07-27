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
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchEmptyPageMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class NasaWorkbenchEmptyPageScreen extends AbstractNasaWorkbenchScreen<NasaWorkbenchEmptyPageMenu> implements NasaWorkbenchScreenPage {
    public static final Identifier BACKGROUND = Constants.texture("gui/container/workbench_page/empty.png");

    public NasaWorkbenchEmptyPageScreen(NasaWorkbenchEmptyPageMenu menu, Inventory inventory, Component ignored) {
        super(menu, inventory, GalacticraftComponents.NEW_SCHEMATIC, BACKGROUND);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(GalacticraftComponents.UNLOCK_SCHEMATIC, _ -> ClientPacketDistributor.sendToServer(new UnlockSchematicPayload(this.menu.containerId, this.menu.getOwnerUUID())))
                .pos(this.width / 2 - 46, this.height / 2 - 52)
                .size(92, 20)
                .createNarration(_ -> (MutableComponent) GalacticraftComponents.UNLOCK_SCHEMATIC)
                .build()
        );
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
        super.extractLabels(graphics, xm, ym);
    }

    @Override
    public int getPageIndex() {
        return -1;
    }

    @Override
    protected boolean activateBackButton() {
        return true;
    }

    @Override
    protected boolean activateNextButton() {
        return false;
    }
}
