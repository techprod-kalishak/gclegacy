package io.kalishak.galacticraftlegacy.component.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

public final class TexturedFlagProvider extends AbstractFlagProvider {
    public static final MapCodec<TexturedFlagProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("image").forGetter(TexturedFlagProvider::image),
            Codec.INT.fieldOf("width").forGetter(TexturedFlagProvider::width),
            Codec.INT.fieldOf("height").forGetter(TexturedFlagProvider::height)
    ).apply(instance, TexturedFlagProvider::new));
    private final Identifier image;

    public TexturedFlagProvider(Identifier image, int width, int height) {
        super(width, height);
        this.image = image;
    }

    @Override
    public byte[][][] resolveColors(SpriteGetter spriteGetter) {
        SpriteId id = new SpriteId(GalacticraftSheets.FLAG_SHEET, this.image);
        TextureAtlasSprite sprite = spriteGetter.get(id);

        this.colors = FlagTypeProvider.populateColor(this.width, this.height, (x, y) -> sprite.getPixelARGB(1, x, y));

        return this.colors;
    }

    public Identifier image() {
        return this.image;
    }

    @Override
    public MapCodec<TexturedFlagProvider> type() {
        return MAP_CODEC;
    }
}
