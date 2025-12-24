package io.kalishak.galacticraftlegacy.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.CoalGeneratorScreen;
import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.GearInventoryScreen;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

@Mod(value = Galacticraft.MODID, dist = Dist.CLIENT)
public class GalacticraftClient {
    public GalacticraftClient(IEventBus bus, ModContainer container) {
        bus.addListener(this::registerScreens);
        bus.addListener(GalacticraftKeys::registerKeyMappings);

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        NeoForge.EVENT_BUS.addListener(this::onKeyPressed);
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GalacticraftMenuType.GEAR.get(), GearInventoryScreen::new);
        event.register(GalacticraftMenuType.COAL_GENERATOR.get(), CoalGeneratorScreen::new);
    }

    private void onKeyPressed(InputEvent.Key event) {
        if (Minecraft.getInstance().screen == null) {
            InputConstants.Key key = InputConstants.getKey(event.getKeyEvent());

            if (GalacticraftKeys.OPEN_GEAR_KEY.isActiveAndMatches(key) && event.getAction() == GLFW.GLFW_RELEASE) {
                ClientPacketDistributor.sendToServer(new ToggleGearInventoryPayload(!Minecraft.getInstance().player.hasContainerOpen()));
            }
        }
    }
}
