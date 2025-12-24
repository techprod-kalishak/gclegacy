package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record ImmutableItemCapacitor(int stored, int capacity) implements EnergyTooltip {
    public static final Codec<ImmutableItemCapacitor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("stored").forGetter(ImmutableItemCapacitor::stored),
            ExtraCodecs.POSITIVE_INT.fieldOf("capacity").forGetter(ImmutableItemCapacitor::capacity)
    ).apply(instance, ImmutableItemCapacitor::new));
    public static final StreamCodec<ByteBuf, ImmutableItemCapacitor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ImmutableItemCapacitor::stored,
            ByteBufCodecs.INT, ImmutableItemCapacitor::capacity,
            ImmutableItemCapacitor::new
    );

    public static ImmutableItemCapacitor withCapacity(int capacity) {
        return new ImmutableItemCapacitor(0, capacity);
    }
}
