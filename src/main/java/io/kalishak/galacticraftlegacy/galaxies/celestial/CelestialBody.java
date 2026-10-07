/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.galaxies.ScalableDistance;
import io.kalishak.galacticraftlegacy.galaxies.component.CelestialBodyIcon;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.TieredRocket;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class CelestialBody implements FeatureElement, Comparable<CelestialBody> {
    public static final Codec<Holder<CelestialBody>> CODEC = Codec.lazyInitialized(GalacticraftRegistries.CELESTIAL_OBJECT::holderByNameCodec);
    public static final StreamCodec<RegistryFriendlyByteBuf, CelestialBody> STREAM_CODEC = ByteBufCodecs.registry(GalacticraftRegistries.Keys.CELESTIAL_BODY);
    private final Holder<CelestialObjectType> celestialBodyType;
    private final @Nullable ResourceKey<CelestialBody> parent;
    private final FeatureFlagSet requiredFeatures;
    private final Vec3 initialPosition;
    private final String descriptionId;
    private final float relativeSize;
    private final float relativeOrbitTime;
    private final float phaseShift;
    private final ScalableDistance relativeDistanceFromCenter;
    private final boolean forceLoad;
    private final @Nullable ResourceKey<DimensionType> targetDimension;
    private final @Nullable FeatureTier requiredTier;
    private final AtmosphereInfo atmosphereInfo;
    private final Vec3 ringColors;
    private final TagKey<ChecklistEntry> checklist;
    private final CelestialBodyIcon icon;
    private final @Nullable ResourceKey<SpaceStationRecipe> recipe;

    protected CelestialBody(Builder builder, ResourceKey<CelestialBody> key) {
        this.celestialBodyType = builder.celestialObjectType;
        this.parent = builder.parent;
        this.requiredFeatures = builder.requiredFeatures;
        this.initialPosition = builder.initialPosition;
        this.descriptionId = builder.descriptionId.get(key);
        this.relativeSize = builder.relativeSize;
        this.relativeOrbitTime = builder.relativeOrbitTime;
        this.phaseShift = builder.phaseShift;
        this.relativeDistanceFromCenter = builder.relativeDistanceFromCenter;
        this.forceLoad = builder.forceLoad;
        this.targetDimension = builder.targetDimension;
        this.requiredTier = builder.requiredTier;
        this.atmosphereInfo = builder.atmosphereInfo;
        this.ringColors = builder.ringColors;
        this.checklist = builder.checklist;
        this.icon = builder.model.get(key);
        this.recipe = builder.spaceStationRecipe.get(key).orElse(null);
    }

    public CelestialObjectType getCelestialBodyType() {
        return this.celestialBodyType.value();
    }

    public @Nullable ResourceKey<CelestialBody> getParent() {
        return this.parent;
    }

    public @Nullable CelestialBody getParent(RegistryAccess registryAccess) {
        if (getParent() == null) return null;

        return registryAccess.getOrThrow(getParent()).value();
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    public String getDescriptionId() {
        return this.descriptionId;
    }

    public Vec3 getInitialPosition() {
        return this.initialPosition;
    }

    /**
     * Used for rendering planet's location on the map. <p> Value of 2.0F would result in the planet being rendered twice as large as earth.
     *
     * @return Size of the planet/moon relative to earth.
     */
    public float getRelativeSize() {
        return this.relativeSize;
    }

    /**
     * Used for rendering planet's location on the map. <p> Value of 2.0F would result in an ellipse with twice the radius of the overworld.
     *
     * @return Distance from the center of the map relative to earth.
     */
    public ScalableDistance getRelativeDistanceFromCenter() {
        return this.relativeDistanceFromCenter;
    }

    /**
     * Multiplier for length of time relative to earth that this planet takes to orbit fully. <p> Value of 2.0F would result in the planet rotating twice as slow (and therefore take twice as long) as the earth takes to revolve around the sun.
     *
     * @return Multiple value for planet's revolution around the sun.
     */
    public float getRelativeOrbitTime() {
        return this.relativeOrbitTime;
    }

    /**
     * Used for rendering planet's location on the map. <p> Value of 1π would result in the planet being rendered directly accross from the original position <p> Value of 2π is a full rotation and therefore would be rendered at the same spot as the original position
     *
     * @return Phase shift of planet for planet's revolution around the sun.
     */
    public float getPhaseShift() {
        return this.phaseShift;
    }

    public @Nullable ResourceKey<DimensionType> getTargetDimension() {
        return this.targetDimension;
    }

    public boolean isReachable() {
        return this.targetDimension != null;
    }

    public boolean isReachable(FeatureTier featureTier) {
        return isReachable() && this.requiredTier != null && this.requiredTier.getLevel() <= featureTier.getLevel();
    }

    public boolean isForceLoaded() {
        return this.forceLoad;
    }

    public @Nullable FeatureTier getTierRequired() {
        return this.requiredTier;
    }

    /**
     * Use this to list the atmospheric gases on the celestial body, starting with the most abundant Do not include trace gases (anything less than 0.25%) (Do not use for stars!)
     */
    public AtmosphereInfo getAtmosphereInfo() {
        return this.atmosphereInfo;
    }

    public Identifier getBodyIcon() {
        return this.icon.icon();
    }

    public @Nullable Identifier getRingIcon() {
        return this.icon.ringsIcon().orElse(null);
    }

    public Vec3 getRingColors() {
        return this.ringColors;
    }

    public TagKey<ChecklistEntry> getChecklistKeys() {
        return this.checklist;
    }

    public @Nullable ResourceKey<SpaceStationRecipe> getRecipe() {
        return this.recipe;
    }

    public boolean isLesserBody() {
        return isSatellite() || getCelestialBodyType() == CelestialObjectType.MOON.value();
    }

    public boolean isSatellite() {
        return hasParent() && this.recipe != null;
    }

    public boolean hasParent() {
        return isSatellite() || this.parent != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CelestialBody other) {
            if (other == this) {
                return true;
            } else if (Objects.nonNull(this.targetDimension) && Objects.nonNull(other.targetDimension)) {
                return this.targetDimension.equals(other.targetDimension);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 31 * this.getCelestialBodyType().hashCode() + getDescriptionId().hashCode();
    }

    @Override
    public int compareTo(@NonNull CelestialBody o) {
        return Float.compare(this.relativeDistanceFromCenter.unscaled(), o.relativeDistanceFromCenter.unscaled());
    }

    public static boolean areSameType(CelestialBody first, CelestialBody other) {
        return first.getCelestialBodyType().equals(other.getCelestialBodyType());
    }

    public static boolean isOwnedByPlayer(CelestialBodyInstance celestialBody, Player player) {
        return celestialBody.getOwner() != null && celestialBody.getOwner().matches(player);
    }

    public static Set<CelestialBody> showReachable(Collection<CelestialBody> registry, TieredRocket rocket, Player player) {
        return registry.stream().filter(celestialObject -> celestialObject.isReachable(rocket.getFeatureTier())).collect(Collectors.toSet());
    }

    public static Collection<CelestialBody> getChildren(RegistryAccess access, CelestialBody celestialBody) {
        if (!celestialBody.getCelestialBodyType().allowChildren()) {
            return Set.of();
        }

        Registry<CelestialBody> celestialBodies = access.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL_BODY);

        return celestialBodies.stream()
                .filter(body -> Objects.nonNull(body.getParent()))
                .filter(child -> celestialBodies.getResourceKey(celestialBody).filter(bodyId -> bodyId.equals(child.getParent())).isPresent())
                .toList();
    }

    public static Collection<CelestialBody> getSiblings(RegistryAccess access, CelestialBody celestialBody) {
        Registry<CelestialBody> celestialBodies = access.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL_BODY);

        return celestialBodies.stream()
                .filter(body -> Objects.nonNull(body.getParent()))
                .filter(body -> haveSameParent(celestialBody, body))
                .toList();
    }

    public static CelestialBody getLastParent(RegistryAccess access, CelestialBody child) {
        if (child.getParent() == null) {
            return child;
        }

        Holder<CelestialBody> parent = access.getOrThrow(child.getParent());

        if (parent.value().getParent() != null) {
            return getLastParent(access, parent.value());
        }

        return parent.value();
    }

    public static @Nullable CelestialBody getStar(RegistryAccess access, CelestialBody body) {
        CelestialBody solarSystem = getLastParent(access, body);

        if (solarSystem instanceof SolarSystem system) {
            return system.getStar(access.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL_BODY));
        }

        return null;
    }

    private static boolean haveSameParent(CelestialBody first, CelestialBody second) {
        if (first.getParent() == null || second.getParent() == null) {
            return false;
        }

        return first.getParent().equals(second.getParent());
    }

    public static class Builder {
        private static final DependantName<CelestialBody, String> DESCRIPTION_ID = id -> Util.makeDescriptionId("celestial_body", id.identifier());
        private static final DependantName<CelestialBody, CelestialBodyIcon> MODEL_ID = id -> new CelestialBodyIcon(id.identifier().withPath(path -> "textures/celestial_body/" + path), Optional.empty());
        private static final DependantName<CelestialBody, Optional<ResourceKey<SpaceStationRecipe>>> RECIPE_ID = id -> Optional.of(
                ResourceKey.create(GalacticraftRegistries.Keys.SPACE_STATION_RECIPE, id.identifier())
        );

        private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
        private DependantName<CelestialBody, String> descriptionId = DESCRIPTION_ID;
        private DependantName<CelestialBody, CelestialBodyIcon> model = MODEL_ID;
        private DependantName<CelestialBody, Optional<ResourceKey<SpaceStationRecipe>>> spaceStationRecipe = _ -> Optional.empty();
        private final Holder<CelestialObjectType> celestialObjectType;
        private @Nullable ResourceKey<CelestialBody> parent;
        private Vec3 initialPosition = Vec3.ZERO;
        private float relativeSize = 1.0F;
        private float relativeOrbitTime = 1.0F;
        private float phaseShift = 0.0F;
        private ScalableDistance relativeDistanceFromCenter = new ScalableDistance(1.0F);
        private boolean forceLoad = true;
        private @Nullable ResourceKey<DimensionType> targetDimension;
        private @Nullable FeatureTier requiredTier;
        private AtmosphereInfo atmosphereInfo = AtmosphereInfo.EARTH;
        private Vec3 ringColors = new Vec3(0.1F, 0.9F, 0.6F);
        private TagKey<ChecklistEntry> checklist;

        private Builder(Holder<CelestialObjectType> celestialObjectType) {
            this.celestialObjectType = celestialObjectType;
        }

        public static Builder of(Holder<CelestialObjectType> celestialObjectType) {
            return new Builder(celestialObjectType);
        }

        public Builder parent(ResourceKey<CelestialBody> parent) {
            this.parent = parent;
            return this;
        }

        public Builder initialPosition(Vec3 initialPosition) {
            this.initialPosition = initialPosition;
            return this;
        }

        public Builder relativeSize(float relativeSize) {
            this.relativeSize = relativeSize;
            return this;
        }

        public Builder relativeDistanceFromCenter(ScalableDistance relativeDistanceFromCenter) {
            this.relativeDistanceFromCenter = relativeDistanceFromCenter;
            return this;
        }

        public Builder relativeDistanceFromCenter(float distance) {
            return relativeDistanceFromCenter(new ScalableDistance(distance));
        }

        public Builder relativeOrbitTime(float relativeOrbitTime) {
            this.relativeOrbitTime = relativeOrbitTime;
            return this;
        }

        public Builder phaseShift(float phaseShift) {
            this.phaseShift = phaseShift;
            return this;
        }

        public Builder noForceLoad() {
            this.forceLoad = false;
            return this;
        }

        public Builder transferableTo(ResourceKey<DimensionType> targetDimension, FeatureTier requiredTier) {
            this.targetDimension = targetDimension;
            this.requiredTier = requiredTier;

            return this;
        }

        public Builder atmosphereInfo(AtmosphereInfo.Builder atmosphereInfoBuilder) {
            this.atmosphereInfo = atmosphereInfoBuilder.build();
            return this;
        }

        public Builder atmosphereInfo(UnaryOperator<AtmosphereInfo.Builder> builder) {
            return atmosphereInfo(builder.apply(AtmosphereInfo.builder()));
        }

        public Builder ringColors(Vec3 ringColors) {
            this.ringColors = ringColors;
            return this;
        }

        public Builder ringColors(float red, float green, float blue) {
            return ringColors(new Vec3(red, green, blue));
        }

        public Builder checklist(TagKey<ChecklistEntry> checklist) {
            this.checklist = checklist;
            return this;
        }

        public Builder requiredFeatures(FeatureFlag... flags) {
            this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
            return this;
        }

        public Builder requiredFeatures(FeatureFlagSet flags) {
            if (!FeatureFlags.REGISTRY.isSubset(flags)) {
                throw new IllegalArgumentException("Mismatched flag sets");
            }

            this.requiredFeatures = flags;
            return this;
        }

        public Builder overrideDescription(String descriptionId) {
            this.descriptionId = DependantName.fixed(descriptionId);
            return this;
        }

        public Builder dummyModel() {
            this.model = DependantName.fixed(CelestialBodyIcon.DUMMY);
            return this;
        }

        public Builder withRings(@NonNull Identifier ringsLocation) {
            this.model = id -> new CelestialBodyIcon(id.identifier().withPath(path -> "textures/celestial_body/" + path), Optional.of(ringsLocation));
            return this;
        }

        public Builder withRecipe() {
            this.spaceStationRecipe = RECIPE_ID;
            return this;
        }

        public CelestialBody create(ResourceKey<CelestialBody> id) {
            return new CelestialBody(this, id);
        }
    }
}
