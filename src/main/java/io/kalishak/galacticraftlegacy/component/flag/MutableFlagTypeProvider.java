/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.component.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

@SuppressWarnings("unused")
public class MutableFlagTypeProvider extends AbstractFlagProvider {
    public static final MapCodec<MutableFlagTypeProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    Codec.INT.fieldOf("height").forGetter(MutableFlagTypeProvider::height),
                    Codec.INT.fieldOf("width").forGetter(MutableFlagTypeProvider::width),
                    Codec.BYTE_BUFFER.fieldOf("color").forGetter(MutableFlagTypeProvider::toByteBuffer)
            ).apply(i, MutableFlagTypeProvider::fromBuffer)
    );
    public static final Codec<MutableFlagTypeProvider> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, MutableFlagTypeProvider> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MutableFlagTypeProvider::height,
            ByteBufCodecs.VAR_INT, MutableFlagTypeProvider::width,
            FlagTypeProvider.BYTE_ARRAYS_STREAM_CODEC, MutableFlagTypeProvider::getColors,
            MutableFlagTypeProvider::new
    );

    public static final MutableFlagTypeProvider DEFAULT = new MutableFlagTypeProvider(48, 32);

    private MutableFlagTypeProvider(int width, int height, byte[][][] color) {
        super(width, height);
        this.colors = color;
    }

    public MutableFlagTypeProvider(int width, int height) {
        super(width, height);
    }

    private static MutableFlagTypeProvider fromBuffer(int width, int height, ByteBuffer buf) {
        byte[][][] color = new byte[width][height][3];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                buf.get(color[i][j]);
            }
        }

        return new MutableFlagTypeProvider(width, height, color);
    }

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                int col = FlagTypeProvider.toColor(this.colors[i][j]);
                image.setRGB(i, j, col);
            }
        }

        return image;
    }

    private ByteBuffer toByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.width * this.height * 3);

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                byteBuffer.put(this.colors[i][j]);
            }
        }

        byteBuffer.flip();

        return byteBuffer;
    }

    public Vec3 getColorAt(int posX, int posY) {
        if (posX >= this.width || posY >= this.height) {
            return Vec3.ZERO;
        }

        return new Vec3((this.colors[posX][posY][0] + 128) / 256.0D, (this.colors[posX][posY][1] + 128) / 256.0D, (this.colors[posX][posY][2] + 128) / 256.0D);
    }

    public int getARGBAt(int posX, int posY) {
        return ARGB.color(getColorAt(posX, posY));
    }

    public void setColorAt(int posX, int posY, Vec3 colorVec) {
        this.colors[posX][posY][0] = (byte) (colorVec.x() - 128);
        this.colors[posX][posY][1] = (byte) (colorVec.y() - 128);
        this.colors[posX][posY][2] = (byte) (colorVec.z() - 128);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MutableFlagTypeProvider flagData = (MutableFlagTypeProvider) o;

        if (this.height != flagData.height) {
            return false;
        } else if (this.width != flagData.width) {
            return false;
        }

        return Arrays.deepEquals(this.colors, flagData.colors);
    }

    @Override
    public MapCodec<MutableFlagTypeProvider> type() {
        return MAP_CODEC;
    }
}
