package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public interface FlagTypeProvider {
    int width();
    int height();

    List<IndexedColor> resolve(SpriteGetter spriteGetter);

    default List<IndexedColor> cachedColors() {
        return new ArrayList<>();
    }

    MapCodec<? extends FlagTypeProvider> type();

    static BufferedImage toBufferedImage(FlagTypeProvider flagTypeProvider) {
        BufferedImage image = new BufferedImage(flagTypeProvider.width(), flagTypeProvider.height(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < flagTypeProvider.width(); i++) {
            for (int j = 0; j < flagTypeProvider.height(); j++) {
                IndexedColor color = find(flagTypeProvider.cachedColors(), i, j);

                image.setRGB(i, j, color.color());
            }
        }

        return image;
    }

    static List<IndexedColor> fromBufferedImage(BufferedImage image) {
        List<IndexedColor> colors = new ArrayList<>();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int color = image.getRGB(x, y);

                colors.add(new IndexedColor(x, y, color));
            }
        }

        return colors;
    }

    static IndexedColor find(List<IndexedColor> colors, int x, int y) {
        return colors.stream().filter(iv -> iv.x() == x && iv.y() == y).findFirst().orElseGet(() -> {
            IndexedColor newValue = new IndexedColor(x, y, 127);
            colors.add(newValue);

            return newValue;
        });
    }

    static List<IndexedColor> populate(int width, int height, BiFunction<Integer, Integer, Integer> colorGetter) {
        List<IndexedColor> colors = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors.add(new IndexedColor(i, j, colorGetter.apply(i, j)));
            }
        }

        return colors;
    }

    record IndexedColor(int x, int y, int color) {
        public static final Codec<IndexedColor> CODEC = Codec.INT_STREAM.comapFlatMap(
                input -> Util.fixedSize(input, 3).map(ints -> new IndexedColor(ints[0], ints[1], ints[2])),
                iColor -> IntStream.of(iColor.x(), iColor.y(), iColor.color())
        );
        public static final StreamCodec<ByteBuf, IndexedColor> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, IndexedColor::x,
                ByteBufCodecs.VAR_INT, IndexedColor::y,
                ByteBufCodecs.INT, IndexedColor::color,
                IndexedColor::new
        );
    }
}
