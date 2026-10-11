package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.galaxies.ScalableDistance;
import io.kalishak.galacticraftlegacy.galaxies.component.CelestialBodyIcon;
import io.kalishak.galacticraftlegacy.galaxies.component.TransferableDimension;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.phys.Vec3;

public class Satellite extends AbstractCelestialBody {
    public static final MapCodec<Satellite> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.lazyInitialized(() -> Celestial.CODEC).fieldOf("parent").forGetter(Satellite::getParent),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(Satellite::description),
            Codec.FLOAT.fieldOf("relative_size").forGetter(Satellite::relativeSize),
            Codec.FLOAT.fieldOf("relative_orbit_size").forGetter(Satellite::relativeOrbitTime),
            Codec.FLOAT.fieldOf("phase_shift").forGetter(Satellite::phaseShift),
            ScalableDistance.CODEC.fieldOf("relative_distance").forGetter(Satellite::relativeDistanceFromCenter),
            TransferableDimension.CODEC.fieldOf("target_dimension").forGetter(Satellite::getTargetDimension),
            Vec3.CODEC.fieldOf("ring_colors").forGetter(Satellite::ringColors),
            ChecklistEntry.HOLDER_SET_CODEC.fieldOf("checklist").forGetter(Satellite::checklist),
            SpaceStationRecipe.CODEC.fieldOf("recipe").forGetter(Satellite::getRecipe)
    ).apply(instance, Satellite::new));
    
    public Satellite(Holder<Celestial> parent, Component description, float relativeSize, float relativeOrbitTime, float phaseShift, ScalableDistance relativeDistanceFromCenter, TransferableDimension targetDimension, Vec3 ringColors, HolderSet<ChecklistEntry> checklist, Holder<SpaceStationRecipe> recipe) {
        super(parent, Vec3.ZERO, description, relativeSize, relativeOrbitTime, phaseShift, relativeDistanceFromCenter, targetDimension, AtmosphereInfo.builder().build(), ringColors, checklist, CelestialBodyIcon.SATELLITE, recipe);
    }
    
    private Holder<Celestial> getParent() {
        if (this.parent == null) {
            throw new IllegalStateException("Satellite must have a parent body");
        }

        return this.parent;
    }

    private TransferableDimension getTargetDimension() {
        if (this.targetDimension == null) {
            throw new IllegalStateException("Satellite must have a target dimension");
        }

        return this.targetDimension;
    }

    private Holder<SpaceStationRecipe> getRecipe() {
        if (this.recipe == null) {
            throw new IllegalStateException("Satellite must have a recipe");
        }

        return this.recipe;
    }

    @Override
    public MapCodec<Satellite> type() {
        return MAP_CODEC;
    }
}
