package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import io.kalishak.galacticraftlegacy.world.level.block.FallenMeteorBlock;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations.FallenMeteorConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Optional;
import java.util.function.Predicate;

public class FallenMeteorFeature extends Feature<FallenMeteorConfiguration> {
    public FallenMeteorFeature() {
        super(FallenMeteorConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<FallenMeteorConfiguration> context) {
        WorldGenLevel levelGen = context.level();
        BlockPos startingPos = context.origin();
        FallenMeteorConfiguration config = context.config();
        RandomSource random = context.random();

        BlockPos fallPos = startingPos.atY(levelGen.getMaxY());
        float craterChance = config.craterChance().sample(random);
        int minDistanceBetween = config.minDistanceBetween().sample(random);
        int maxDistanceBetween = config.maxDistanceBetween().sample(random);

        if (craterChance + 0.2F * 0.8F > 1.0F) {
            makeCrater(fallPos.getX() - 64, fallPos.getZ() - 64, 4, levelGen);
        }

        Optional<Column> column = findBottom(levelGen, fallPos);

        if (column.isEmpty() || column.get().getFloor().isEmpty()) {
            return false;
        }

        int minY = column.get().getFloor().getAsInt();

        BlockState meteorState = GalacticraftBlocks.FALLEN_METEOR.get().defaultBlockState();
        BlockPos meteorPos = startingPos.atY(minY);

        if (levelGen.getFluidState(meteorPos).is(FluidTags.WATER)) {
            meteorState = meteorState.setValue(BlockStateProperties.WATERLOGGED, true);
        }

        levelGen.setBlock(meteorPos, meteorState, FallenMeteorBlock.UPDATE_CLIENTS);

        return true;
    }


    private static void makeCrater(int craterX, int craterZ, int size, WorldGenLevel level) {
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

    private static Optional<Column> findBottom(WorldGenLevel levelGen, BlockPos pos) {
        Predicate<BlockState> inWater = state -> state.is(Blocks.WATER);
        Predicate<BlockState> notWater = state -> !state.is(Blocks.WATER);

        return Column.scan(levelGen, pos, 90, inWater, notWater);
    }
}
