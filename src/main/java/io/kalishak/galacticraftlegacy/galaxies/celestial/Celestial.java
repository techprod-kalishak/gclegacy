package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.galaxies.ScalableDistance;
import io.kalishak.galacticraftlegacy.galaxies.component.CelestialBodyIcon;
import io.kalishak.galacticraftlegacy.galaxies.component.TransferableDimension;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.PlanetaryTransition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Celestial {
    Codec<Either<SimpleCelestialBody, Celestial>> DISPATCH_CODEC = Codec.either(
            SimpleCelestialBody.MAP_CODEC.codec(),
            Codec.lazyInitialized(GalacticraftRegistries.CELESTIAL_TYPE::byNameCodec).dispatch(Celestial::type, Function.identity())
    );
    Codec<Celestial> DIRECT_CODEC = DISPATCH_CODEC.xmap(
            either -> either.map(Function.identity(), Function.identity()),
            celestial -> celestial instanceof SimpleCelestialBody simple ? Either.left(simple) : Either.right(celestial)
    );
    Codec<Holder<Celestial>> CODEC = RegistryCodecs.holder(GalacticraftRegistries.Keys.CELESTIAL);
    StreamCodec<RegistryFriendlyByteBuf, Holder<Celestial>> STREAM_CODEC = ByteBufCodecs.holderRegistry(GalacticraftRegistries.Keys.CELESTIAL);
    
    Optional<Holder<Celestial>> parent();
    Vec3 position();
    Component description();
    /**
     * Used for rendering planet's location on the map. <p> Value of 2.0F would result in the planet being rendered twice as large as earth.
     *
     * @return Size of the planet/moon relative to earth.
     */
    float relativeSize();
    /**
     * Multiplier for length of time relative to earth that this planet takes to orbit fully. <p> Value of 2.0F would result in the planet rotating twice as slow (and therefore take twice as long) as the earth takes to revolve around the sun.
     *
     * @return Multiple color for planet's revolution around the sun.
     */
    float relativeOrbitTime();
    /**
     * Used for rendering planet's location on the map. <p> Value of 1π would result in the planet being rendered directly accross from the original position <p> Value of 2π is a full rotation and therefore would be rendered at the same spot as the original position
     *
     * @return Phase shift of planet for planet's revolution around the sun.
     */
    float phaseShift();
    /**
     * Used for rendering planet's location on the map. <p> Value of 2.0F would result in an ellipse with twice the radius of the overworld.
     *
     * @return Distance from the center of the map relative to earth.
     */
    ScalableDistance relativeDistanceFromCenter();
    Optional<TransferableDimension> targetDimension();
    /**
     * Use this to list the atmospheric gases on the celestial body, starting with the most abundant Do not include trace gases (anything less than 0.25%) (Do not use for stars!)
     */
    AtmosphereInfo atmosphereInfo();
    Vec3 ringColors();
    HolderSet<ChecklistEntry> checklist();
    CelestialBodyIcon icon();
    Optional<Holder<SpaceStationRecipe>> recipe();

    MapCodec<? extends Celestial> type();

    class Builder {
        private static final DependantName<Celestial, Component> DESCRIPTION_ID = id -> Component.translatable(Util.makeDescriptionId("celestial_body", id.identifier()));
        private static final DependantName<Celestial, CelestialBodyIcon> MODEL_ID = id -> new CelestialBodyIcon(id.identifier().withPath(path -> "textures/celestial_body/" + path), Optional.empty());

        private DependantName<Celestial, Component> descriptionId = DESCRIPTION_ID;
        private DependantName<Celestial, CelestialBodyIcon> model = MODEL_ID;
        private @Nullable Holder<SpaceStationRecipe> spaceStationRecipe;
        private @Nullable Holder<Celestial> parent;
        private Vec3 initialPosition = Vec3.ZERO;
        private float relativeSize = 1.0F;
        private float relativeOrbitTime = 1.0F;
        private float phaseShift = 0.0F;
        private ScalableDistance relativeDistanceFromCenter = new ScalableDistance(1.0F);
        private @Nullable TransferableDimension targetDimension;
        private AtmosphereInfo atmosphereInfo = AtmosphereInfo.EARTH;
        private Vec3 ringColors = new Vec3(0.1F, 0.9F, 0.6F);
        private HolderSet<ChecklistEntry> checklist = HolderSet.empty();

        private Builder() {
        }

        public static Builder of() {
            return new Builder();
        }

        public Builder parent(@Nullable Holder<Celestial> parent) {
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

        public Builder transferableTo(@Nullable Holder<DimensionType> targetDimension, PlanetaryTransition transition, FeatureTier requiredTier) {
            this.targetDimension = new TransferableDimension(targetDimension, transition, requiredTier);

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

        public Builder checklist(HolderSet<ChecklistEntry> checklist) {
            this.checklist = checklist;
            return this;
        }

        public Builder overrideDescription(Component description) {
            this.descriptionId = DependantName.fixed(description);
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

        public Builder withRecipe(@Nullable Holder<SpaceStationRecipe> recipe) {
            this.spaceStationRecipe = recipe;
            return this;
        }

        public SimpleCelestialBody create(ResourceKey<Celestial> id) {
            return new SimpleCelestialBody(
                    Optional.ofNullable(this.parent),
                    this.initialPosition,
                    this.descriptionId.get(id),
                    this.relativeSize,
                    this.relativeOrbitTime,
                    this.phaseShift,
                    this.relativeDistanceFromCenter,
                    Optional.ofNullable(this.targetDimension),
                    this.atmosphereInfo,
                    this.ringColors,
                    this.checklist,
                    this.model.get(id),
                    Optional.ofNullable(this.spaceStationRecipe)
            );
        }

        public <T extends Celestial> T createOther(CelestialFactory<T> factory, ResourceKey<Celestial> id) {
            return factory.create(
                    Optional.ofNullable(this.parent),
                    this.initialPosition,
                    this.descriptionId.get(id),
                    this.relativeSize,
                    this.relativeOrbitTime,
                    this.phaseShift,
                    this.relativeDistanceFromCenter,
                    Optional.ofNullable(this.targetDimension),
                    this.atmosphereInfo,
                    this.ringColors,
                    this.checklist,
                    this.model.get(id),
                    Optional.ofNullable(this.spaceStationRecipe)
            );
        }

        public Star createStar(ResourceKey<Celestial> id) {
            return new Star(
                    this.initialPosition,
                    this.descriptionId.get(id),
                    this.relativeSize,
                    this.atmosphereInfo,
                    this.model.get(id)
            );
        }

        public SolarSystem createSolarSystem(ResourceKey<Celestial> id, Holder<Celestial> star) {
            return new SolarSystem(
                    star,
                    this.descriptionId.get(id),
                    this.model.get(id)
            );
        }

        public Satellite createSatellite(ResourceKey<Celestial> id) {
            return new Satellite(
                    this.parent,
                    this.descriptionId.get(id),
                    this.relativeSize,
                    this.relativeOrbitTime,
                    this.phaseShift,
                    this.relativeDistanceFromCenter,
                    this.targetDimension,
                    this.ringColors,
                    this.checklist,
                    this.spaceStationRecipe
            );
        }
    }

    @FunctionalInterface
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    interface CelestialFactory<T extends Celestial> {
        T create(Optional<Holder<Celestial>> parent, Vec3 position, Component description, float relativeSize, float relativeOrbitTime, float phaseShift, ScalableDistance relativeDistanceFromCenter,
                 Optional<TransferableDimension> targetDimension, AtmosphereInfo atmosphereInfo, Vec3 ringColors, HolderSet<ChecklistEntry> checklist, CelestialBodyIcon icon,
                 Optional<Holder<SpaceStationRecipe>> recipe);
    }
}
