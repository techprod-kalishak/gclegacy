/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public record CrudeOilPoolFeature(RuleTest target, IntProvider radius, IntProvider height, Optional<Integer> distanceFromRoof) implements Feature {
    public static final MapCodec<CrudeOilPoolFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RuleTest.CODEC.fieldOf("target").forGetter(CrudeOilPoolFeature::target),
            IntProviders.codec(0, 16).fieldOf("radius").forGetter(CrudeOilPoolFeature::radius),
            IntProviders.codec(0, 16).fieldOf("height").forGetter(CrudeOilPoolFeature::height),
            Codec.INT.optionalFieldOf("max_column_height").forGetter(CrudeOilPoolFeature::distanceFromRoof)
    ).apply(instance, CrudeOilPoolFeature::new));

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        Optional<Column> column = GalacticraftFeatures.findBottom(level, origin);
        OptionalInt yLevel = column.map(Column::getFloor).orElseGet(OptionalInt::empty);

        if (yLevel.isEmpty()) return false;

        BlockPos bottomPos = origin.atY(yLevel.getAsInt());
        Vec3i poolSize = new Vec3i(radius().sample(random), height().sample(random), radius().sample(random));
        BoundingBox poolBox = BoundingBox.fromCorners(bottomPos, bottomPos.offset(poolSize));

        if (BlockPos.betweenClosedStream(poolBox).filter(pos -> isValidPlacement(level, pos)).mapToInt(pos -> {
            level.setBlock(pos, GalacticraftBlocks.OIL.get().defaultBlockState(), 2);
            return 1;
        }).sum() > 0) {
            return distanceFromRoof().isEmpty() || fillHeight(level, poolBox.getCenter(), distanceFromRoof().get());
        }

        return false;
    }

    @Override
    public MapCodec<CrudeOilPoolFeature> codec() {
        return MAP_CODEC;
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

    private static boolean isValidPlacement(WorldGenLevel levelGen, BlockPos pos) {
        return OxygenHelper.surrounding(pos).noneMatch(relative -> levelGen.getBlockState(relative).isAir());
    }

    public static class Builder {
        private final RuleTest target;
        private IntProvider radius = ConstantInt.ZERO;
        private IntProvider height = ConstantInt.ZERO;
        private @Nullable Integer distanceFromRoof;

        public Builder(Block block) {
            this.target = new BlockMatchTest(block);
        }

        public Builder(RuleTest target) {
            this.target = target;
        }

        public static Builder ofBlock(Holder<Block> block) {
            return new Builder(block.value());
        }

        public static Builder ofTag(TagKey<Block> targets) {
            return new Builder(new TagMatchTest(targets));
        }

        public Builder radius(int min, int max) {
            this.radius = UniformInt.of(min, max);
            return this;
        }

        public Builder radius(int radius) {
            this.radius = ConstantInt.of(radius);
            return this;
        }

        public Builder height(int min, int max) {
            this.height = UniformInt.of(min, max);
            return this;
        }

        public Builder height(int height) {
            this.height = ConstantInt.of(height);
            return this;
        }

        public Builder distanceFromRoof(@Nullable Integer distanceFromRoof) {
            this.distanceFromRoof = distanceFromRoof;
            return this;
        }

        public CrudeOilPoolFeature build() {
            return new CrudeOilPoolFeature(this.target, this.radius, this.height, Optional.ofNullable(this.distanceFromRoof));
        }
    }
}
