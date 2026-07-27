/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class NasaWorkbenchScreen extends AbstractNasaWorkbenchScreen<NasaWorkbenchMenu> {
    public static final Identifier BACKGROUND = Constants.texture("gui/container/workbench_page/default.png");

    public NasaWorkbenchScreen(NasaWorkbenchMenu menu, Inventory inventory, Component ignored) {
        super(menu, inventory, NasaWorkbenchScreenPage.createTitle(SchematicVariants.TIER_1_ROCKET), BACKGROUND);
    }

    @Override
    public int getPageIndex() {
        return 0;
    }

    @Override
    protected boolean activateBackButton() {
        return false;
    }

    @Override
    protected boolean activateNextButton() {
        return true;
    }
}
