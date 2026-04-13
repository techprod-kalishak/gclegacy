package io.kalishak.galacticraftlegacy.galaxies;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class SolarSystem extends CelestialBody {
    protected final Vec3 mapPosition;
    protected final Supplier<CelestialObject> mainStar;
    protected final Identifier galaxyId;

    public SolarSystem(Holder<CelestialBodyType> celestialBodyType, Vec3 mapPosition, Supplier<CelestialObject> mainStar, Identifier galaxyId, Properties properties) {
        super(celestialBodyType, properties);
        this.mapPosition = scale(mapPosition);
        this.mainStar = mainStar;
        this.galaxyId = galaxyId;
    }

    public Vec3 getMapPosition() {
        return this.mapPosition;
    }

    private static Vec3 scale(Vec3 mapPosition) {
        return mapPosition.scale(500.0D);
    }

    public CelestialObject getMainStar() {
        return this.mainStar.get();
    }

    public String getGalaxyDescriptionId() {
        return this.galaxyId.toLanguageKey("galaxy.");
    }
}
