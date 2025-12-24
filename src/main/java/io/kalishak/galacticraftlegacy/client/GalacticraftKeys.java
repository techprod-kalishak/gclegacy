package io.kalishak.galacticraftlegacy.client;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class GalacticraftKeys {
    public static final KeyMapping.Category GC_CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Galacticraft.MODID, "galacticraft"));
    public static final KeyMapping OPEN_GEAR_KEY = new KeyMapping(
            "galacticraftlegacy.key.toggle_gear_inventory",
            71,
            GC_CATEGORY
    );

    static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.registerCategory(GC_CATEGORY);
        event.register(OPEN_GEAR_KEY);
    }
}
