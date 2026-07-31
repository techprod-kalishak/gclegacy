/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchPageMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;

public class NasaWorkbenchPageScreen extends AbstractNasaWorkbenchScreen<NasaWorkbenchPageMenu> implements NasaWorkbenchScreenPage {
    public NasaWorkbenchPageScreen(NasaWorkbenchPageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, DEFAULT_IMAGE_WIDTH, menu.getCurrentPage().imageHeight(), menu.getCurrentPage().background());
    }

    @Override
    public ResourceKey<SchematicVariant> getPage() {
        return this.menu.getCurrentPage().schematic().getKey();
    }
}
