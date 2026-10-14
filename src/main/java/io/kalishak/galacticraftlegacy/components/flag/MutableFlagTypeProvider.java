/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public class MutableFlagTypeProvider implements FlagTypeProvider {
    public static final MapCodec<MutableFlagTypeProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("height").forGetter(MutableFlagTypeProvider::height),
            Codec.INT.fieldOf("width").forGetter(MutableFlagTypeProvider::width),
            FlagTypeProvider.IndexedColor.CODEC.listOf().fieldOf("colors").forGetter(FlagTypeProvider::resolvedColors)
    ).apply(instance, MutableFlagTypeProvider::new));
    public static final Codec<MutableFlagTypeProvider> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, MutableFlagTypeProvider> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MutableFlagTypeProvider::height,
            ByteBufCodecs.VAR_INT, MutableFlagTypeProvider::width,
            FlagTypeProvider.IndexedColor.STREAM_CODEC.apply(ByteBufCodecs.list()), FlagTypeProvider::resolvedColors,
            MutableFlagTypeProvider::new
    );

    public static final MutableFlagTypeProvider DEFAULT = new MutableFlagTypeProvider(48, 32);
    private final int width;
    private final int height;
    private final List<FlagTypeProvider.IndexedColor> colors;

    private MutableFlagTypeProvider(int width, int height, List<FlagTypeProvider.IndexedColor> colors) {
        this.width = width;
        this.height = height;
        this.colors = colors;
    }

    public MutableFlagTypeProvider(int width, int height) {
        this.width = width;
        this.height = height;
        this.colors = new ArrayList<>();
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public List<IndexedColor> resolvedColors() {
        return this.colors;
    }

    public int getColorAt(int posX, int posY) {
        if (posX >= this.width || posY >= this.height) {
            return 0;
        }

        FlagTypeProvider.IndexedColor value = FlagTypeProvider.find(this, posX, posY);

        return value.color();
    }

    public void setColorAt(int posX, int posY, int color) {
        FlagTypeProvider.IndexedColor value = FlagTypeProvider.find(this, posX, posY);

        int index = this.colors.indexOf(value);

        if (index >= 0) {
            this.colors.set(index, new FlagTypeProvider.IndexedColor(posX, posY, color));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MutableFlagTypeProvider flagData = (MutableFlagTypeProvider) o;

        if (height != flagData.height) {
            return false;
        } else if (width != flagData.width) {
            return false;
        }

        return this.colors.equals(flagData.colors);
    }

    @Override
    public MapCodec<MutableFlagTypeProvider> type() {
        return MAP_CODEC;
    }
}
