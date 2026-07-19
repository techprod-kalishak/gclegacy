package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchPageMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NasaWorkbenchPageScreen extends AbstractNasaWorkbenchScreen<NasaWorkbenchPageMenu> {
    public NasaWorkbenchPageScreen(NasaWorkbenchPageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, menu.getCurrentPage().background());
    }
}
