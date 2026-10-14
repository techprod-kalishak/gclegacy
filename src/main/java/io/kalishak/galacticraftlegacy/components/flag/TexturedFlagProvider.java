package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;

public record TexturedFlagProvider(Identifier image, int width, int height) implements FlagTypeProvider {
    public static final MapCodec<TexturedFlagProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("image").forGetter(TexturedFlagProvider::image),
            Codec.INT.fieldOf("width").forGetter(TexturedFlagProvider::width),
            Codec.INT.fieldOf("height").forGetter(TexturedFlagProvider::height)
    ).apply(instance, TexturedFlagProvider::new));

    @Override
    public List<IndexedColor> resolvedColors() {
        return List.of();
    }

    @Override
    public MapCodec<TexturedFlagProvider> type() {
        return MAP_CODEC;
    }
}
