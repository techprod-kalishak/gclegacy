package io.kalishak.galacticraftlegacy.registry;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.space.CelestialBody;
import io.kalishak.galacticraftlegacy.space.CelestialBodyType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class GalacticraftRegistries {
    public static final Registry<CelestialBody> CELESTIAL_BODY = new RegistryBuilder<>(Keys.CELESTIAL_BODY)
            .defaultKey(Constants.id("sol"))
            .create();

    @SubscribeEvent
    public static void newRegistries(NewRegistryEvent event) {
        event.register(CELESTIAL_BODY);
    }

    @SubscribeEvent
    public static void newDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Keys.SCHEMATIC, SchematicVariant.DIRECT_CODEC, SchematicVariant.DIRECT_CODEC, builder -> builder.defaultKey(SchematicVariants.TIER_2_ROCKET));
        event.dataPackRegistry(Keys.CELESTIAL_BODY_LEVEL_DATA, CelestialBodyLevelData.DIRECT_CODEC, CelestialBodyLevelData.DIRECT_CODEC, builder -> builder.defaultKey(CelestialBodyLevelDataEntries.OVERWORLD));
    }

    public static class Keys {
        public static final ResourceKey<? extends Registry<CelestialBody>> CELESTIAL_BODY = ResourceKey.createRegistryKey(Constants.id("celestial_body"));
        public static final ResourceKey<? extends Registry<CelestialBodyType>> CELESTIAL_BODY_TYPE = ResourceKey.createRegistryKey(Constants.id("celestial_body_type"));
        public static final ResourceKey<Registry<CelestialBodyLevelData>> CELESTIAL_BODY_LEVEL_DATA = ResourceKey.createRegistryKey(Constants.id("celestial_body_level_data"));
        public static final ResourceKey<Registry<SchematicVariant>> SCHEMATIC = ResourceKey.createRegistryKey(Constants.id("schematic"));
    }
}
