package io.kalishak.galacticraftlegacy.registry;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.galaxies.CelestialBodyType;
import io.kalishak.galacticraftlegacy.galaxies.CelestialObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class GalacticraftRegistries {
    public static final Registry<CelestialObject> CELESTIAL_OBJECT = new RegistryBuilder<>(Keys.CELESTIAL_OBJECT)
            .defaultKey(Constants.id("sol"))
            .create();

    @SubscribeEvent
    public static void newRegistries(NewRegistryEvent event) {
        event.register(CELESTIAL_OBJECT);
    }

    @SubscribeEvent
    public static void newDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Keys.CHECKLIST, ChecklistEntry.DIRECT_CODEC, ChecklistEntry.DIRECT_CODEC, builder -> builder.sync(true).defaultKey(Checklist.EQUIP_OXYGEN_SUIT));
        event.dataPackRegistry(Keys.SCHEMATIC, SchematicVariant.DIRECT_CODEC, SchematicVariant.DIRECT_CODEC, builder -> builder.sync(true).defaultKey(SchematicVariants.TIER_2_ROCKET));
        event.dataPackRegistry(Keys.CELESTIAL_BODY_LEVEL_DATA, CelestialBodyLevelData.DIRECT_CODEC, CelestialBodyLevelData.DIRECT_CODEC, builder -> builder.sync(true).defaultKey(CelestialBodyLevelDataEntries.OVERWORLD));
    }

    public static class Keys {
        public static final ResourceKey<? extends Registry<CelestialObject>> CELESTIAL_OBJECT = ResourceKey.createRegistryKey(Constants.id("celestial_body"));
        public static final ResourceKey<? extends Registry<CelestialBodyType>> CELESTIAL_BODY_TYPE = ResourceKey.createRegistryKey(Constants.id("celestial_body_type"));
        public static final ResourceKey<Registry<ChecklistEntry>> CHECKLIST = ResourceKey.createRegistryKey(Constants.id("checklist"));
        public static final ResourceKey<Registry<CelestialBodyLevelData>> CELESTIAL_BODY_LEVEL_DATA = ResourceKey.createRegistryKey(Constants.id("celestial_body_level_data"));
        public static final ResourceKey<Registry<SchematicVariant>> SCHEMATIC = ResourceKey.createRegistryKey(Constants.id("schematic"));
    }
}
