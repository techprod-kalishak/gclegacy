/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.component.flag;

import net.minecraft.client.resources.model.sprite.SpriteGetter;

public abstract class AbstractFlagProvider implements FlagTypeProvider {
    protected final int width;
    protected final int height;
    protected byte[][][] colors;

    protected AbstractFlagProvider(int width, int height) {
        this.width = width;
        this.height = height;
        this.colors = new byte[width][height][3];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.colors[i][j][0] = 127;
                this.colors[i][j][1] = 127;
                this.colors[i][j][2] = 127;
            }
        }
    }

    @Override
    public byte[][][] resolveColors(SpriteGetter spriteGetter) {
        return this.colors;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    public byte[][][] getColors() {
        return this.colors;
    }
}
