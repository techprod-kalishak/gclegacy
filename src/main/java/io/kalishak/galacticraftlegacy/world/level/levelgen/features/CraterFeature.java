/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.level.levelgen.CraterSize;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

public record CraterFeature(CraterSize craterSize, IntProvider spacing) implements Feature {
    public static final MapCodec<CraterFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CraterSize.CODEC.fieldOf("crater_size").forGetter(CraterFeature::craterSize),
            IntProviders.codec(0, 32).fieldOf("spacing").forGetter(CraterFeature::spacing)
    ).apply(instance, CraterFeature::new));

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        int size = random.nextInt(craterSize().getMaxSize() - craterSize().getMinSize()) + craterSize().getMinSize();
        makeCrater(origin.getX(), origin.getZ(), size, level);

        return true;
    }

    @Override
    public MapCodec<CraterFeature> codec() {
        return MAP_CODEC;
    }

    public static void makeCrater(int craterX, int craterZ, int size, WorldGenLevel level) {
        for (int x = 0; x < 128; x++) {
            for (int z = 0; z < 128; z++) {
                double xPos = craterX - x;
                double zPos = craterZ - z;

                if (xPos * xPos + zPos * zPos < size * size) {
                    xPos /= size;
                    zPos /= size;
                    final double sqrtY = xPos * xPos + zPos * zPos;
                    double yDev = sqrtY * sqrtY * 6;
                    yDev = 5 - yDev;
                    int helper = 0;

                    for (int y = 127; y > 0; y--) {
                        BlockPos blockPos = new BlockPos(x, y, z);

                        if (!level.getBlockState(blockPos).isAir() && helper <= yDev) {
                            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                            helper++;
                        }

                        if (helper > yDev) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
