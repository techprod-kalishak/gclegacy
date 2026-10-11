package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.galaxies.ScalableDistance;
import io.kalishak.galacticraftlegacy.galaxies.component.CelestialBodyIcon;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.phys.Vec3;

public class Star extends AbstractCelestialBody {
    public static final MapCodec<Star> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Vec3.CODEC.fieldOf("position").forGetter(Star::position),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(Star::description),
            Codec.FLOAT.fieldOf("relative_size").forGetter(Star::relativeSize),
            AtmosphereInfo.CODEC.optionalFieldOf("atmosphere", AtmosphereInfo.builder().build()).forGetter(Star::atmosphereInfo),
            CelestialBodyIcon.CODEC.fieldOf("icon").forGetter(Star::icon)
    ).apply(instance, Star::new));

    public Star(Vec3 position, Component description, float relativeSize, AtmosphereInfo atmosphereInfo, CelestialBodyIcon icon) {
        super(null, position, description, relativeSize, 0.0F, 0.0F, new ScalableDistance(0.0F), null, atmosphereInfo, Vec3.ZERO, HolderSet.empty(), icon, null);
    }

    @Override
    public MapCodec<Star> type() {
        return MAP_CODEC;
    }
}
