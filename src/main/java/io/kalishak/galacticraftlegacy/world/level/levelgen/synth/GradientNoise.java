/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.synth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class GradientNoise extends NoiseModule implements DensityFunction.SimpleFunction {
    public static final MapCodec<GradientNoise> DATA_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("x_offset").forGetter(noise -> noise.xOffset),
            Codec.DOUBLE.fieldOf("y_offset").forGetter(noise -> noise.yOffset),
            Codec.DOUBLE.fieldOf("z_offset").forGetter(noise -> noise.zOffset),
            Codec.INT.fieldOf("octaves_count").forGetter(noise -> noise.octavesCount),
            Codec.DOUBLE.fieldOf("persistence").forGetter(noise -> noise.persistence)
    ).apply(instance, GradientNoise::createUnseeded));
    public static final KeyDispatchDataCodec<GradientNoise> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    private final FishyNoise noise;
    private final double xOffset;
    private final double yOffset;
    private final double zOffset;
    private final int octavesCount;
    private final double persistence;

    private GradientNoise(FishyNoise noise, double xOffset, double yOffset, double zOffset, int octavesCount, double persistence) {
        this.noise = noise;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.octavesCount = octavesCount;
        this.persistence = persistence;
    }

    public static GradientNoise createUnseeded(double xOffset, double yOffset, double zOffset, int octavesCount, double persistence) {
        return new GradientNoise(new FishyNoise(new XoroshiroRandomSource(0L)), xOffset, yOffset, zOffset, octavesCount, persistence);
    }

    public static GradientNoise createSeeded(long seed, int octavesCount, double persistence) {
        return new GradientNoise(new XoroshiroRandomSource(seed), octavesCount, persistence);
    }

    private GradientNoise(RandomSource random, int octavesCount, double persistence) {
        this(
                new FishyNoise(random),
                random.nextFloat() / 2.0F,
                random.nextFloat() / 2.0F,
                random.nextFloat() / 2.0F,
                octavesCount,
                persistence
        );
    }

    private static double getAverage(GradientNoise gradientNoise, double frequency, int chunkX, int chunkY, int chunkZ) {
        gradientNoise.setFrequency(frequency);

        double x = chunkX;
        double y = chunkY;
        double z = chunkZ;

        if (gradientNoise.octavesCount == 1)
            return gradientNoise.noise.getValue(x * gradientNoise.xFreq + gradientNoise.xOffset, y * gradientNoise.yFreq + gradientNoise.yOffset, z * gradientNoise.zFreq + gradientNoise.zOffset) * gradientNoise.amplitude;
        x *= gradientNoise.xFreq;
        y *= gradientNoise.yFreq;
        z *= gradientNoise.zFreq;

        double val = 0.0D;
        double curAmplitude = gradientNoise.amplitude;

        for (int n = 0; n < gradientNoise.octavesCount; n++) {
            val += gradientNoise.noise.getValue(x + gradientNoise.xOffset, y + gradientNoise.yOffset, z + gradientNoise.zOffset) * curAmplitude;
            x *= 2.0D;
            y *= 2.0D;
            z *= 2.0D;
            curAmplitude *= gradientNoise.persistence;
        }

        return val;
    }

    @Override
    public double compute(FunctionContext context) {
        return getAverage(this, 1.0D, context.blockX(), context.blockY(), context.blockZ());
    }

    @Override
    public double minValue() {
        return 0;
    }

    @Override
    public double maxValue() {
        return 1;
    }

    @Override
    public KeyDispatchDataCodec<GradientNoise> codec() {
        return CODEC;
    }
}
