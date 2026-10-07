package io.kalishak.galacticraftlegacy.galaxies.celestial;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public class SolarSystem extends CelestialBody {
    protected final ResourceKey<CelestialBody> starKey;

    public SolarSystem(Builder builder, ResourceKey<CelestialBody> starKey, ResourceKey<CelestialBody> key) {
        super(builder, key);
        this.starKey = starKey;
    }

    public @Nullable Star getStar(HolderGetter<CelestialBody> celestialBodies) {
        return (Star) celestialBodies.get(this.starKey)
                .map(Holder.Reference::value)
                .filter(c -> c instanceof Star)
                .orElseThrow(() -> new IllegalStateException("Could not find any star of name: " + this.starKey));
    }
}
