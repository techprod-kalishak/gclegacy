package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.resources.model.sprite.SpriteGetter;

import java.util.List;

public record BlankFlagProvider(int width, int height, int color) implements FlagTypeProvider {
    public static final MapCodec<BlankFlagProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("width").forGetter(BlankFlagProvider::width),
            Codec.INT.fieldOf("height").forGetter(BlankFlagProvider::height),
            Codec.INT.fieldOf("color").forGetter(BlankFlagProvider::color)
    ).apply(instance, BlankFlagProvider::new));

    @Override
    public List<IndexedColor> resolve(SpriteGetter spriteGetter) {
        return FlagTypeProvider.populate(this.width, this.height, (_, _) -> this.color);
    }

    @Override
    public MapCodec<BlankFlagProvider> type() {
        return MAP_CODEC;
    }
}
