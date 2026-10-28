package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.BiFunction;

public interface FlagTypeProvider {
    int width();
    int height();

    byte[][][] resolveColors(SpriteGetter spriteGetter);

    MapCodec<? extends FlagTypeProvider> type();

    StreamCodec<ByteBuf, byte[][][]> BYTE_ARRAYS_STREAM_CODEC = StreamCodec.of(
            (output, value) -> {
                output.writeInt(value.length);
                output.writeInt(value[0].length);

                for (byte[][] row : value) {
                    for (byte[] column : row) {
                        FriendlyByteBuf.writeByteArray(output, column);
                    }
                }
            },
            input -> {
                int width = input.readInt();
                int height = input.readInt();

                byte[][][] color = new byte[width][height][3];

                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        color[i][j] = FriendlyByteBuf.readByteArray(input);
                    }
                }

                return color;
            }
    );

    static byte[][][] populateColor(int width, int height, BiFunction<Integer, Integer, Integer> colorGetter) {
        byte[][][] colors = new byte[width][height][3];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = fromColor(colorGetter.apply(i, j));
            }
        }

        return colors;
    }

    static byte[] fromColor(int color) {
        byte[] colors = new byte[3];

        colors[0] = (byte) ((color >> 16) & 0xFF);
        colors[1] = (byte) ((color >> 8) & 0xFF);
        colors[2] = (byte) (color & 0xFF);

        return colors;
    }

    static int toColor(byte[] color) {
        return ((color[0] + 128) << 16) | ((color[1] + 128) << 8) | (color[2] + 128);
    }
}
