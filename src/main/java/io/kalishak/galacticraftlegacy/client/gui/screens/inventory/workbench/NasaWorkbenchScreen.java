package io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class NasaWorkbenchScreen extends AbstractNasaWorkbenchScreen<NasaWorkbenchMenu> {
    public static final Identifier BACKGROUND = Constants.texture("gui/container/workbench_page/default.png");

    public NasaWorkbenchScreen(NasaWorkbenchMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }
}
