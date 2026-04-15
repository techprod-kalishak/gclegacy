/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.level.race;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Vec3i;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FlagData {
    public static final MapCodec<FlagData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("height").forGetter(FlagData::getHeight),
            Codec.INT.fieldOf("width").forGetter(FlagData::getWidth),
            IndexedValue.CODEC.listOf().fieldOf("colors").forGetter(flagData -> flagData.colors)
    ).apply(instance, FlagData::new));
    public static final Codec<FlagData> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, FlagData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, FlagData::getHeight,
            ByteBufCodecs.VAR_INT, FlagData::getWidth,
            IndexedValue.STREAM_CODEC.apply(ByteBufCodecs.list()), flagData -> flagData.colors,
            FlagData::new
    );

    public static final FlagData DEFAULT = new FlagData(48, 32);
    private int height;
    private int width;
    private final List<IndexedValue> colors;

    private FlagData(int height, int width, List<IndexedValue> colors) {
        this.height = height;
        this.width = width;
        this.colors = colors;
    }

    public FlagData(int height, int width) {
        this.height = height;
        this.width = width;
        this.colors = new ArrayList<>();
    }

    private static IndexedValue find(FlagData flagData, int x, int y) {
        return flagData.colors.stream().filter(iv -> iv.x() == x && iv.y() == y).findFirst().orElseGet(() -> {
            IndexedValue newValue = new IndexedValue(x, 7, new Vec3i(127, 127, 127));
            flagData.colors.add(newValue);

            return newValue;
        });
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public Vec3 getColorAt(int posX, int posY) {
        if (posX >= this.width || posY >= this.height) {
            return new Vec3(0, 0, 0);
        }

        IndexedValue value = find(this, posX, posY);
        Vec3i color = value.value();

        return new Vec3((color.getX() + 128) / 256.0D, (color.getY() + 128) / 256.0D, (color.getZ() + 128) / 256.0D);
    }

    public void setColorAt(int posX, int posY, Vec3i colorVec) {
        IndexedValue value = find(this, posX, posY);

        int index = this.colors.indexOf(value);

        if (index >= 0) {
            this.colors.set(index, new IndexedValue(posX, posY, colorVec));
        }
    }

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                IndexedValue value = find(this, i, j);
                Vec3i color = value.value();
                int col = ((color.getX() + 128) << 16) | ((color.getY() + 128) << 8) | (color.getZ() + 128);

                image.setRGB(i, j, col);
            }
        }

        return image;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FlagData flagData = (FlagData) o;

        if (height != flagData.height) {
            return false;
        } else if (width != flagData.width) {
            return false;
        }

        return this.colors.equals(flagData.colors);
    }

    record IndexedValue(int x, int y, Vec3i value) {
        public static final Codec<IndexedValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("x").forGetter(IndexedValue::x),
                Codec.INT.fieldOf("x").forGetter(IndexedValue::y),
                Vec3i.CODEC.fieldOf("value").forGetter(IndexedValue::value)
        ).apply(instance, IndexedValue::new));
        public static final StreamCodec<ByteBuf, IndexedValue> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, IndexedValue::x,
                ByteBufCodecs.VAR_INT, IndexedValue::y,
                Vec3i.STREAM_CODEC, IndexedValue::value,
                IndexedValue::new
        );

        public byte xAsByte() {
            return (byte) this.x;
        }

        public byte yAsByte() {
            return (byte) this.y;
        }
    }
}
