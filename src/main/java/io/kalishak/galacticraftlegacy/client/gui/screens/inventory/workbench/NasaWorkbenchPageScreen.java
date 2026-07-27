/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchPageMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NasaWorkbenchPageScreen extends AbstractNasaWorkbenchScreen<NasaWorkbenchPageMenu> implements NasaWorkbenchScreenPage {
    public NasaWorkbenchPageScreen(NasaWorkbenchPageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, menu.getCurrentPage().background());
    }


    @Override
    public int getPageIndex() {
        return Schematics.getPageNumber(this.menu.getCurrentPage());
    }

    @Override
    protected boolean activateBackButton() {
        return true;
    }

    @Override
    protected boolean activateNextButton() {
        assert this.minecraft.player != null;
        return this.minecraft.player.registryAccess().lookupOrThrow(GalacticraftRegistries.Keys.SCHEMATIC).size() < getPageIndex();
    }
}
