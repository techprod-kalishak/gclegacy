/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import io.kalishak.galacticraftlegacy.world.level.block.FallenMeteorBlock;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.Optional;

public record FallenMeteorFeature(HotContent warmth, FloatProvider craterChance, IntProvider minDistanceBetween, IntProvider maxDistanceBetween) implements Feature {
    public static final MapCodec<FallenMeteorFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HotContent.MAP_CODEC.forGetter(FallenMeteorFeature::warmth),
            FloatProviders.CODEC.fieldOf("crater_chance").forGetter(FallenMeteorFeature::craterChance),
            IntProviders.codec(4, 32).fieldOf("min_distance_between").forGetter(FallenMeteorFeature::minDistanceBetween),
            IntProviders.codec(4, 32).fieldOf("max_distance_between").forGetter(FallenMeteorFeature::maxDistanceBetween)
    ).apply(instance, FallenMeteorFeature::new));

    public static final FallenMeteorFeature DEFAULT = new FallenMeteorFeature(
            HotContent.DEFAULT,
            ConstantFloat.of(1.0F),
            ConstantInt.of(4),
            UniformInt.of(8, 12)
    );

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos fallPos = origin.atY(level.getMaxY());
        float craterChance = craterChance().sample(random);

        if (craterChance + 0.2F * 0.8F > 1.0F) {
            CraterFeature.makeCrater(fallPos.getX() - 64, fallPos.getZ() - 64, 4, level);
        }

        Optional<Column> column = GalacticraftFeatures.findBottom(level, fallPos);

        if (column.isEmpty() || column.get().getFloor().isEmpty()) {
            return false;
        }

        int minY = column.get().getFloor().getAsInt();

        BlockState meteorState = GalacticraftBlocks.FALLEN_METEOR.get().defaultBlockState();
        BlockPos meteorPos = origin.atY(minY);

        if (level.getFluidState(meteorPos).is(FluidTags.WATER)) {
            meteorState = meteorState.setValue(BlockStateProperties.WATERLOGGED, true);
        }

        level.setBlock(meteorPos, meteorState, FallenMeteorBlock.UPDATE_CLIENTS);

        return true;
    }

    @Override
    public MapCodec<FallenMeteorFeature> codec() {
        return MAP_CODEC;
    }

    public static class Builder {
        private HotContent warmth = HotContent.DEFAULT;
        private FloatProvider craterChance = ConstantFloat.of(1.0F);
        private IntProvider minDistanceBetween = ConstantInt.of(4);
        private IntProvider maxDistanceBetween = UniformInt.of(8, 12);

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder warmth(int initialWarmth) {
            this.warmth = new HotContent(initialWarmth);
            return this;
        }

        public Builder noCrater() {
            this.craterChance = ConstantFloat.ZERO;
            return this;
        }

        public Builder craterChance(float chance) {
            this.craterChance = ConstantFloat.of(chance);
            return this;
        }

        public Builder craterChance(float min, float max) {
            this.craterChance = UniformFloat.of(min, max);
            return this;
        }

        public Builder minDistanceBetween(int lowerDistance, int upperDistance) {
            this.minDistanceBetween = UniformInt.of(lowerDistance, upperDistance);
            return this;
        }

        public Builder maxDistanceBetween(int lowerDistance, int upperDistance) {
            this.maxDistanceBetween = UniformInt.of(lowerDistance, upperDistance);
            return this;
        }

        public FallenMeteorFeature build() {
            return new FallenMeteorFeature(this.warmth, this.craterChance, this.minDistanceBetween, this.maxDistanceBetween);
        }
    }
}
