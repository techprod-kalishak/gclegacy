package io.kalishak.galacticraftlegacy.network.handler.server;

import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.world.inventory.GearInventoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleGearInventoryServerHandler {
    public static void handle(ToggleGearInventoryPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Player player = cxt.player();

            if (!player.hasContainerOpen() && payload.open()) {
                cxt.player().openMenu(new SimpleMenuProvider(GearInventoryMenu::new, Component.translatable("container.inventory")));
            }  else if (!payload.open()) {
                cxt.player().closeContainer();
            }

        }).exceptionally(e -> {
            cxt.disconnect(Component.translatable("galacticraftlegacy.networking_failed", e.getLocalizedMessage()));
            return null;
        });
    }
}
