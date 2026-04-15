/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations.CrudeOilPoolConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;

public class CrudeOilPoolFeature extends Feature<CrudeOilPoolConfiguration> {
    public CrudeOilPoolFeature() {
        super(CrudeOilPoolConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<CrudeOilPoolConfiguration> featurePlaceContext) {
        WorldGenLevel levelGen = featurePlaceContext.level();
        BlockPos startingPos = featurePlaceContext.origin();
        CrudeOilPoolConfiguration config = featurePlaceContext.config();
        RandomSource random = featurePlaceContext.random();
        Optional<Column> column = findBottom(levelGen, startingPos);
        OptionalInt yLevel = column.map(Column::getFloor).orElseGet(OptionalInt::empty);

        if (yLevel.isEmpty()) return false;

        BlockPos bottomPos = startingPos.atY(yLevel.getAsInt());
        Vec3i poolSize = new Vec3i(config.radius().sample(random), config.height().sample(random), config.radius().sample(random));
        BoundingBox poolBox = BoundingBox.fromCorners(bottomPos, bottomPos.offset(poolSize));

        if (BlockPos.betweenClosedStream(poolBox).filter(pos -> isValidPlacement(levelGen, pos)).mapToInt(pos -> {
            levelGen.setBlock(pos, GalacticraftBlocks.OIL.get().defaultBlockState(), 2);
            return 1;
        }).sum() > 0) {
            return config.distanceFromRoof().isEmpty() || fillHeight(levelGen, poolBox.getCenter(), config.distanceFromRoof().get());
        }

        return false;
    }

    private static boolean fillHeight(WorldGenLevel level, BlockPos center, int height) {
        int maxHeight = center.getY() + height;

        while (level.getBlockState(center.atY(maxHeight)).isAir()) {
            maxHeight--;
        }

        return BlockPos.betweenClosedStream(center, center.atY(maxHeight)).mapToInt(pos -> {
            level.setBlock(pos, GalacticraftBlocks.OIL.get().defaultBlockState(), 2);
            return 1;
        }).sum() > 0;
    }

    private static Optional<Column> findBottom(WorldGenLevel levelGen, BlockPos pos) {
        Predicate<BlockState> inWater = state -> state.is(Blocks.WATER);
        Predicate<BlockState> notWater = state -> !state.is(Blocks.WATER);

        return Column.scan(levelGen, pos, 90, inWater, notWater);
    }

    private static boolean isValidPlacement(WorldGenLevel levelGen, BlockPos pos) {
        return OxygenHelper.surrounding(pos).noneMatch(relative -> levelGen.getBlockState(relative).isAir());
    }
}
