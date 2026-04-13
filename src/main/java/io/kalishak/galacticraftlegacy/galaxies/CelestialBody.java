package io.kalishak.galacticraftlegacy.galaxies;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.TieredRocket;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class CelestialBody extends CelestialObject implements Comparable<CelestialBody> {
    private final ResourceKey<CelestialObject> id;
    protected final float relativeSize;
    protected final ScalableDistance relativeDistanceFromCenter;
    protected final float relativeOrbitTime;
    protected final float phaseShift;
    protected final ResourceKey<Level> levelId;
    protected final boolean autoRegisterDimension;
    protected final boolean isReachable;
    protected final boolean forceLoad;
    protected final FeatureTier tierRequired;
    protected final @Nullable Supplier<CelestialObject> parent;
    protected final TagKey<Biome> biomeInfo;
    public final AtmosphereInfo atmosphereInfo;
    protected final Identifier celestialBodyIcon;
    protected final Vec3 ringColors;
    protected final TagKey<ChecklistEntry> checklist;

    protected CelestialBody(Holder<CelestialBodyType> celestialBodyType, Properties properties) {
        super(celestialBodyType);
        this.id = properties.id;
        this.relativeSize = properties.relativeSize;
        this.relativeDistanceFromCenter = properties.relativeDistanceFromCenter;
        this.relativeOrbitTime = properties.relativeOrbitTime;
        this.phaseShift = properties.phaseShift;
        this.levelId = properties.levelId;
        this.autoRegisterDimension = properties.autoRegisterDimension;
        this.isReachable = properties.isReachable;
        this.forceLoad = properties.forceLoad;
        this.tierRequired = properties.tierRequired;
        this.parent = properties.parent;
        this.biomeInfo = properties.biomeInfo;
        this.atmosphereInfo = properties.atmosphereInfo.build();
        this.celestialBodyIcon = properties.celestialBodyIcon;
        this.ringColors = properties.ringColors;
        this.checklist = properties.checklist;
    }

    public static boolean isReachable(CelestialObject celestial, FeatureTier featureTier) {
        return celestial instanceof CelestialBody celestialBody && celestialBody.isReachable(featureTier);
    }

    public static Set<CelestialObject> showReachable(Collection<CelestialObject> registry, TieredRocket rocket, Player player) {
        return registry.stream().filter(celestialObject -> isReachable(celestialObject, rocket.getFeatureTier())).collect(Collectors.toSet());
    }

    @Override
    protected ResourceKey<CelestialObject> id() {
        return this.id;
    }

    @Override
    public String getDescriptionId() {
        return this.id.identifier().toLanguageKey(this.celestialBodyType.getRegisteredName());
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

    public ResourceKey<Level> getLevelId() {
        return this.levelId;
    }

    public boolean isAutoRegisterDimension() {
        return this.autoRegisterDimension;
    }

    public boolean isReachable() {
        return this.isReachable;
    }

    public boolean isReachable(FeatureTier featureTier) {
        return isReachable() && getTierRequired().getLevel() <= featureTier.getLevel();
    }

    public boolean isForceLoad() {
        return this.forceLoad;
    }

    public FeatureTier getTierRequired() {
        return this.tierRequired;
    }

    public HolderSet<Biome> getBiomeInfo(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(this.biomeInfo.registry()).getOrThrow(this.biomeInfo);
    }

    /**
     * Use this to list the atmospheric gases on the celestial body, starting with the most abundant Do not include trace gases (anything less than 0.25%) (Do not use for stars!)
     */
    public AtmosphereInfo getAtmosphereInfo() {
        return this.atmosphereInfo;
    }

    public Identifier getCelestialBodyIcon() {
        return this.celestialBodyIcon;
    }

    public Vec3 getRingColors() {
        return this.ringColors;
    }

    public HolderSet<ChecklistEntry> getChecklistKeys(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(this.checklist.registry()).getOrThrow(this.checklist);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() * 31 + getCelestialBodyType().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof CelestialBody other) {
            return this.id.equals(other.id);
        }

        return false;
    }

    @Override
    public int compareTo(@NonNull CelestialBody other) {
        return Float.compare(getRelativeDistanceFromCenter().unscaled(), other.getRelativeDistanceFromCenter().unscaled());
    }

    public static class Properties {
        private final ResourceKey<CelestialObject> id;
        private float relativeSize = 1.0F;
        private ScalableDistance relativeDistanceFromCenter = new ScalableDistance(1.0F);
        private float relativeOrbitTime = 1.0F;
        private float phaseShift = 0.0F;
        private ResourceKey<Level> levelId = Level.OVERWORLD;
        private boolean autoRegisterDimension = false;
        private boolean isReachable = true;
        private boolean forceLoad = true;
        private FeatureTier tierRequired = FeatureTier.TIER_1;
        private @Nullable Supplier<CelestialObject> parent;
        private @Nullable TagKey<Biome> biomeInfo = null;
        private AtmosphereInfo.Builder atmosphereInfo = AtmosphereInfo.builder();
        private Identifier celestialBodyIcon = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/celestial_body/dummy");
        private Vec3 ringColors = new Vec3(0.1F, 0.9F, 0.6F);
        private @Nullable TagKey<ChecklistEntry> checklist = null;

        private Properties(ResourceKey<CelestialObject> id) {
            this.id = id;
        }

        public static Properties of(ResourceKey<CelestialObject> id) {
            return new Properties(id);
        }

        public Properties relativeSize(float relativeSize) {
            this.relativeSize = relativeSize;
            return this;
        }

        public Properties relativeDistanceFromCenter(ScalableDistance relativeDistanceFromCenter) {
            this.relativeDistanceFromCenter = relativeDistanceFromCenter;
            return this;
        }

        public Properties relativeDistanceFromCenter(float distance) {
            this.relativeDistanceFromCenter = new ScalableDistance(distance);
            return this;
        }

        public Properties relativeOrbitTime(float relativeOrbitTime) {
            this.relativeOrbitTime = relativeOrbitTime;
            return this;
        }

        public Properties phaseShift(float phaseShift) {
            this.phaseShift = phaseShift;
            return this;
        }

        public Properties autoRegisterDimension() {
            this.autoRegisterDimension = true;
            return this;
        }

        public Properties unreachable() {
            this.isReachable = false;
            return this;
        }

        public Properties forceLoad() {
            this.forceLoad = true;
            return this;
        }

        public Properties tierRequired(FeatureTier tierRequired) {
            this.tierRequired = tierRequired;
            return this;
        }

        public Properties parent(Supplier<CelestialObject> parent) {
            this.parent = parent;
            return this;
        }

        public Properties biome(TagKey<Biome> biomeInfo) {
            this.biomeInfo = biomeInfo;
            return this;
        }

        public Properties atmosphereInfo(AtmosphereInfo.Builder atmosphereInfo) {
            this.atmosphereInfo = atmosphereInfo;
            return this;
        }

        public Properties atmosphereInfo(UnaryOperator<AtmosphereInfo.Builder> builder) {
            return atmosphereInfo(builder.apply(this.atmosphereInfo));
        }

        public Properties icon(Identifier celestialBodyIcon) {
            this.celestialBodyIcon = celestialBodyIcon;
            return this;
        }

        public Properties ringColors(Vec3 ringColors) {
            this.ringColors = ringColors;
            return this;
        }

        public Properties ringColors(float red, float green, float blue) {
            return ringColors(new Vec3(red, green, blue));
        }

        public Properties checklist(TagKey<ChecklistEntry> checklist) {
            this.checklist = checklist;
            return this;
        }
    }
}
