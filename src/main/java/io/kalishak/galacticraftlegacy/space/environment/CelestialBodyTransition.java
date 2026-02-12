package io.kalishak.galacticraftlegacy.space.environment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

/**
 * Transition information for celestial bodies when entering the celestial body.
 * @param initialVelocity entry vector
 * @param transitionHeight height at which the transition starts, exclusive from the bottom of the celestial body
 * @param type Type of lander which entity uses during transition
 */
public record CelestialBodyTransition(Vec3 initialVelocity, int transitionHeight, Type type) {
    public static final Codec<CelestialBodyTransition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("initial_velocity").forGetter(CelestialBodyTransition::initialVelocity),
            Codec.INT.fieldOf("transition_height").forGetter(CelestialBodyTransition::transitionHeight),
            Type.CODEC.fieldOf("type").forGetter(CelestialBodyTransition::type)
    ).apply(instance, CelestialBodyTransition::new));
    public static final StreamCodec<ByteBuf, CelestialBodyTransition> STREAM_CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC, CelestialBodyTransition::initialVelocity,
            ByteBufCodecs.VAR_INT, CelestialBodyTransition::transitionHeight,
            Type.STREAM_CODEC, CelestialBodyTransition::type,
            CelestialBodyTransition::new
    );

    public enum Type implements SerializableEnum {
        PARACHUTE(0, "parachute"),
        LANDER(1, "lander"),
        BUBBLE_LANDER(2, "air_balloon"),
        CAPSULE(3, "capsule");

        public static final Codec<Type> CODEC = SerializableEnum.codec(Type.class);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = SerializableEnum.streamCodec(Type.class);
        private final int id;
        private final String name;

        Type(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.id;
        }
    }
}
