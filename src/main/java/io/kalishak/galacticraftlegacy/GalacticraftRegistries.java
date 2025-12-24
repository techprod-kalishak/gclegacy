package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.space.CelestialBodyType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class GalacticraftRegistries {
    public static class Keys {
        public static final ResourceKey<? extends Registry<CelestialBodyType>> CELESTIAL_BODY_TYPE = ResourceKey.createRegistryKey(Galacticraft.id("celestial_body_type"));
    }
}
