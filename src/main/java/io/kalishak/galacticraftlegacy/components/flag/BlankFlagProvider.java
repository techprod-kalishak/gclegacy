package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.resources.model.sprite.SpriteGetter;

public class BlankFlagProvider extends AbstractFlagProvider {
    public static final MapCodec<BlankFlagProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("width").forGetter(BlankFlagProvider::width),
            Codec.INT.fieldOf("height").forGetter(BlankFlagProvider::height),
            Codec.INT.fieldOf("color").forGetter(x -> x.color)
    ).apply(instance, BlankFlagProvider::new));

    private final int color;

    public BlankFlagProvider(int width, int height, int color) {
        super(width, height);
        this.color = color;
    }

    @Override
    public byte[][][] resolveColors(SpriteGetter spriteGetter) {
        return FlagTypeProvider.populateColor(this.width, this.height, (_, _) -> this.color);
    }

    @Override
    public MapCodec<BlankFlagProvider> type() {
        return MAP_CODEC;
    }
}
