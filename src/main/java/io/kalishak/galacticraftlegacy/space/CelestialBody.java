package io.kalishak.galacticraftlegacy.space;

import net.minecraft.core.Holder;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.function.Supplier;

public abstract class CelestialBody {
    public Set<CelestialBody> getChildBodies() {
        return Set.of();
    }

    public abstract Supplier<CelestialBodyType> getCelestialBodyType();

    public abstract @Nullable Holder<CelestialBody> getParentBody();
}
