/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ScalableDistance(float unscaled, float scaled) {
    public static final Codec<ScalableDistance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("unscaled").forGetter(ScalableDistance::unscaled),
            Codec.FLOAT.fieldOf("scaled").forGetter(ScalableDistance::scaled)
    ).apply(instance, ScalableDistance::new));
    public static final Codec<ScalableDistance> DISTANCE_CODEC = Codec.withAlternative(CODEC, Codec.FLOAT.xmap(ScalableDistance::new, ScalableDistance::unscaled));
    public static final StreamCodec<ByteBuf, ScalableDistance> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ScalableDistance::unscaled,
            ByteBufCodecs.FLOAT, ScalableDistance::scaled,
            ScalableDistance::new
    );

    public ScalableDistance(float distance) {
        this(distance, distance);
    }
}
