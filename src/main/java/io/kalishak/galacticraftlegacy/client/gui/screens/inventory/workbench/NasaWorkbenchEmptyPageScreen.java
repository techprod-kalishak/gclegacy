package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchEmptyPageMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class NasaWorkbenchEmptyPageScreen extends AbstractNasaWorkbenchScreen<NasaWorkbenchEmptyPageMenu> {
    public static final Identifier BACKGROUND = Constants.texture("gui/container/workbench_page/empty.png");

    public NasaWorkbenchEmptyPageScreen(NasaWorkbenchEmptyPageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }
}
