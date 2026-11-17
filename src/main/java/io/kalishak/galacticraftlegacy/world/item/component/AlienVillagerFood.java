package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record AlienVillagerFood(int nutrition) {
    public static final Codec<AlienVillagerFood> CODEC = RecordCodecBuilder.create(
            i -> i.group(ExtraCodecs.POSITIVE_INT.fieldOf("nutrition").forGetter(AlienVillagerFood::nutrition)).apply(i, AlienVillagerFood::new)
    );
    public static final StreamCodec<ByteBuf, AlienVillagerFood> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, AlienVillagerFood::nutrition, AlienVillagerFood::new
    );
}
