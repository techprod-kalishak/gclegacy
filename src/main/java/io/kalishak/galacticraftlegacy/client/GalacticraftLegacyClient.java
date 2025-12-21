package io.kalishak.galacticraftlegacy.client;

import io.kalishak.galacticraftlegacy.GalacticraftLegacy;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = GalacticraftLegacy.MODID, dist = Dist.CLIENT)
public class GalacticraftLegacyClient {
    public GalacticraftLegacyClient(IEventBus bus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
