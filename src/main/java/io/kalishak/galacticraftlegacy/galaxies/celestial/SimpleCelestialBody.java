package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.galaxies.ScalableDistance;
import io.kalishak.galacticraftlegacy.galaxies.component.CelestialBodyIcon;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record SimpleCelestialBody(Optional<Holder<Celestial>> parent, Vec3 position, Component description, float relativeSize, float relativeOrbitTime, float phaseShift, ScalableDistance relativeDistanceFromCenter,
                                  Optional<Holder<DimensionType>> targetDimension, FeatureTier requiredTier, AtmosphereInfo atmosphereInfo, Vec3 ringColors, HolderSet<ChecklistEntry> checklist, CelestialBodyIcon icon,
                                  Optional<Holder<SpaceStationRecipe>> recipe) implements Celestial {
    public static final MapCodec<SimpleCelestialBody> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.lazyInitialized(() -> Celestial.CODEC).optionalFieldOf("parent").forGetter(SimpleCelestialBody::parent),
            Vec3.CODEC.optionalFieldOf("position", Vec3.ZERO).forGetter(SimpleCelestialBody::position),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(SimpleCelestialBody::description),
            Codec.FLOAT.fieldOf("relative_size").forGetter(SimpleCelestialBody::relativeSize),
            Codec.FLOAT.fieldOf("relative_orbit_size").forGetter(SimpleCelestialBody::relativeOrbitTime),
            Codec.FLOAT.fieldOf("phase_shift").forGetter(SimpleCelestialBody::phaseShift),
            ScalableDistance.CODEC.fieldOf("relative_distance").forGetter(SimpleCelestialBody::relativeDistanceFromCenter),
            DimensionType.CODEC.optionalFieldOf("target_dimension").forGetter(SimpleCelestialBody::targetDimension),
            FeatureTier.CODEC.optionalFieldOf("required_tier", FeatureTier.UNREACHABLE).forGetter(SimpleCelestialBody::requiredTier),
            AtmosphereInfo.CODEC.fieldOf("atmosphere_info").forGetter(SimpleCelestialBody::atmosphereInfo),
            Vec3.CODEC.fieldOf("ring_colors").forGetter(SimpleCelestialBody::ringColors),
            ChecklistEntry.HOLDER_SET_CODEC.fieldOf("checklist").forGetter(SimpleCelestialBody::checklist),
            CelestialBodyIcon.CODEC.fieldOf("icon").forGetter(SimpleCelestialBody::icon),
            SpaceStationRecipe.CODEC.optionalFieldOf("recipe").forGetter(SimpleCelestialBody::recipe)
    ).apply(instance, SimpleCelestialBody::new));
    
    @Override
    public MapCodec<SimpleCelestialBody> type() {
        return MAP_CODEC;
    }
}
