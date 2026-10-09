package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.galaxies.ScalableDistance;
import io.kalishak.galacticraftlegacy.galaxies.component.CelestialBodyIcon;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.phys.Vec3;

public class SolarSystem extends AbstractCelestialBody {
    public static final MapCodec<SolarSystem> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.lazyInitialized(() -> Celestial.CODEC).fieldOf("star").forGetter(s -> s.star),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(SolarSystem::description),
            CelestialBodyIcon.CODEC.fieldOf("icon").forGetter(SolarSystem::icon)
    ).apply(instance, SolarSystem::new));
    private final Holder<Celestial> star;

    public SolarSystem(Holder<Celestial> star, Component description, CelestialBodyIcon icon) {
        super(null, Vec3.ZERO, description, 0.0F, 0.0F, 0.0F, new ScalableDistance(0.0F), null, FeatureTier.UNREACHABLE, AtmosphereInfo.builder().build(), Vec3.ZERO, HolderSet.empty(), icon, null);
        this.star = star;
    }

    public Holder<Celestial> getStar() {
        if (!(this.star.value() instanceof Star)) {
            throw new IllegalStateException(this.star.getKey() + " is not a star!");
        }

        return this.star;
    }

    @Override
    public MapCodec<SolarSystem> type() {
        return MAP_CODEC;
    }
}
