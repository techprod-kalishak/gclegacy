package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

import java.util.List;

public record TexturedFlagProvider(Identifier image, int width, int height) implements FlagTypeProvider {
    public static final MapCodec<TexturedFlagProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("image").forGetter(TexturedFlagProvider::image),
            Codec.INT.fieldOf("width").forGetter(TexturedFlagProvider::width),
            Codec.INT.fieldOf("height").forGetter(TexturedFlagProvider::height)
    ).apply(instance, TexturedFlagProvider::new));

    @Override
    public List<IndexedColor> resolve(SpriteGetter spriteGetter) {
        SpriteId id = new SpriteId(GalacticraftSheets.FLAG_SHEET, this.image);
        TextureAtlasSprite sprite = spriteGetter.get(id);

        return FlagTypeProvider.populate(this.width, this.height, (x, y) -> sprite.getPixelRGBA(1, x, y));
    }

    @Override
    public MapCodec<TexturedFlagProvider> type() {
        return MAP_CODEC;
    }
}
