package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import io.kalishak.galacticraftlegacy.world.level.levelgen.CraterSize;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations.CraterConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CraterFeature extends Feature<CraterConfiguration> {
    public CraterFeature() {
        super(CraterConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<CraterConfiguration> featurePlaceContext) {
        WorldGenLevel levelGen = featurePlaceContext.level();
        BlockPos startingPos = featurePlaceContext.origin();
        CraterConfiguration config = featurePlaceContext.config();
        RandomSource random = featurePlaceContext.random();

        CraterSize craterSize = config.craterSize();
        int size = random.nextInt(craterSize.getMaxSize() - craterSize.getMinSize()) + craterSize.getMinSize();
        makeCrater(startingPos.getX(), startingPos.getZ(), size, levelGen);

        return true;
    }

    private void makeCrater(int craterX, int craterZ, int size, WorldGenLevel level) {
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
