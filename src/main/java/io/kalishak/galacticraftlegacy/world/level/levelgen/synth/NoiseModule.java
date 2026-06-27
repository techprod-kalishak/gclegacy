/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.synth;

public abstract class NoiseModule {
    public double xFreq = 1.0D;
    public double yFreq = 1.0D;
    public double zFreq = 1.0D;
    public double amplitude = 1.0D;

    public double evaluateNoise(double x) {
        return x;
    }

    public double evaluateNoise(double x, double y) {
        return x;
    }

    public double evaluateNoise(double x, double y, double z) {
        return x;
    }

    public void setFrequency(double frequency) {
        this.xFreq = frequency;
        this.yFreq = frequency;
        this.zFreq = frequency;
    }
}
