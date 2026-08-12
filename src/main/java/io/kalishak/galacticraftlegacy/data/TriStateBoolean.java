/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public record TriStateBoolean(boolean isBound, boolean value) {
    public static final Codec<TriStateBoolean> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("is_bound", false).forGetter(TriStateBoolean::isBound),
            Codec.BOOL.fieldOf("value").forGetter(TriStateBoolean::value)
    ).apply(instance, TriStateBoolean::new));
    public static final StreamCodec<ByteBuf, TriStateBoolean> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, TriStateBoolean::isBound,
            ByteBufCodecs.BOOL, TriStateBoolean::value,
            TriStateBoolean::new
    );
    public static final TriStateBoolean NONE = of(null);
    public static final TriStateBoolean TRUE = of(true);
    public static final TriStateBoolean FALSE = of(false);

    public static TriStateBoolean of(@Nullable Boolean value) {
        return new TriStateBoolean(value != null, value != null);
    }

    public Optional<Boolean> asOptional() {
        return this.isBound ? Optional.of(this.value) : Optional.empty();
    }
}
