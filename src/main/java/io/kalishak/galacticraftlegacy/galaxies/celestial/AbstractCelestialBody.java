package io.kalishak.galacticraftlegacy.galaxies.celestial;

import io.kalishak.galacticraftlegacy.galaxies.ScalableDistance;
import io.kalishak.galacticraftlegacy.galaxies.component.CelestialBodyIcon;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractCelestialBody implements Celestial, Comparable<Celestial> {
    protected final @Nullable Holder<Celestial> parent;
    protected final Vec3 position;
    protected final Component description;
    protected final float relativeSize;
    protected final float relativeOrbitTime;
    protected final float phaseShift;
    protected final ScalableDistance relativeDistanceFromCenter;
    protected final @Nullable Holder<DimensionType> targetDimension;
    protected final FeatureTier requiredTier;
    protected final AtmosphereInfo atmosphereInfo;
    protected final Vec3 ringColors;
    protected final HolderSet<ChecklistEntry> checklist;
    protected final CelestialBodyIcon icon;
    protected final @Nullable Holder<SpaceStationRecipe> recipe;

    protected AbstractCelestialBody(@Nullable Holder<Celestial> parent, Vec3 position, Component description, float relativeSize, float relativeOrbitTime, float phaseShift, ScalableDistance relativeDistanceFromCenter,
                                    @Nullable Holder<DimensionType> targetDimension, FeatureTier requiredTier, AtmosphereInfo atmosphereInfo, Vec3 ringColors, HolderSet<ChecklistEntry> checklist, CelestialBodyIcon icon,
                                    @Nullable Holder<SpaceStationRecipe> recipe) {
        this.parent = parent;
        this.position = position;
        this.description = description;
        this.relativeSize = relativeSize;
        this.relativeOrbitTime = relativeOrbitTime;
        this.phaseShift = phaseShift;
        this.relativeDistanceFromCenter = relativeDistanceFromCenter;
        this.targetDimension = targetDimension;
        this.requiredTier = requiredTier;
        this.atmosphereInfo = atmosphereInfo;
        this.ringColors = ringColors;
        this.checklist = checklist;
        this.icon = icon;
        this.recipe = recipe;
    }

    @Override
    public Optional<Holder<Celestial>> parent() {
        return Optional.ofNullable(this.parent);
    }

    @Override
    public Vec3 position() {
        return this.position;
    }

    @Override
    public Component description() {
        return this.description;
    }

    @Override
    public float relativeSize() {
        return this.relativeSize;
    }

    @Override
    public float relativeOrbitTime() {
        return this.relativeOrbitTime;
    }

    @Override
    public float phaseShift() {
        return this.phaseShift;
    }

    @Override
    public ScalableDistance relativeDistanceFromCenter() {
        return this.relativeDistanceFromCenter;
    }

    @Override
    public Optional<Holder<DimensionType>> targetDimension() {
        return Optional.ofNullable(this.targetDimension);
    }

    @Override
    public FeatureTier requiredTier() {
        return this.requiredTier;
    }

    @Override
    public AtmosphereInfo atmosphereInfo() {
        return this.atmosphereInfo;
    }

    @Override
    public Vec3 ringColors() {
        return this.ringColors;
    }

    @Override
    public HolderSet<ChecklistEntry> checklist() {
        return this.checklist;
    }

    @Override
    public CelestialBodyIcon icon() {
        return this.icon;
    }

    @Override
    public Optional<Holder<SpaceStationRecipe>> recipe() {
        return Optional.ofNullable(this.recipe);
    }

    @Override
    public int compareTo(@NonNull Celestial other) {
        return Float.compare(relativeDistanceFromCenter().unscaled(), other.relativeDistanceFromCenter().unscaled());
    }
}
