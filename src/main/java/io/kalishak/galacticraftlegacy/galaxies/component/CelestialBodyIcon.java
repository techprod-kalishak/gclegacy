package io.kalishak.galacticraftlegacy.galaxies.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record CelestialBodyIcon(Identifier icon, Optional<Identifier> ringsIcon) {
    public static final Codec<CelestialBodyIcon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("icon").forGetter(CelestialBodyIcon::icon),
            Identifier.CODEC.optionalFieldOf("rings_icon").forGetter(CelestialBodyIcon::ringsIcon)
    ).apply(instance, CelestialBodyIcon::new));
    public static final StreamCodec<ByteBuf, CelestialBodyIcon> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, CelestialBodyIcon::icon,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), CelestialBodyIcon::ringsIcon,
            CelestialBodyIcon::new
    );
    public static final CelestialBodyIcon DUMMY = new CelestialBodyIcon(
            Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/celestial_body/dummy"),
            Optional.empty()
    );
    public static final CelestialBodyIcon SATELLITE = new CelestialBodyIcon(
            Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/celestial_body/satellite"),
            Optional.empty()
    );
}
