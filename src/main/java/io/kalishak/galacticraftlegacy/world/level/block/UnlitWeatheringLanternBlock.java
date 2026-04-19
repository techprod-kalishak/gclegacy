/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class UnlitWeatheringLanternBlock extends UnlitLanternBlock implements WeatheringCopper {
    public static final MapCodec<UnlitWeatheringLanternBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            WeatherState.CODEC.fieldOf("weathering_state").forGetter(block -> block.weatherState),
            BlockState.CODEC.fieldOf("lit_state").forGetter(block -> block.litState),
            propertiesCodec()
    ).apply(instance, UnlitWeatheringLanternBlock::new));
    private final WeatheringCopper.WeatherState weatherState;

    public UnlitWeatheringLanternBlock(WeatheringCopper.WeatherState weatherState, BlockState litState, Properties properties) {
        super(litState, properties);
        this.weatherState = weatherState;
    }

    @Override
    public MapCodec<UnlitWeatheringLanternBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (OxygenHelper.hasOxygenNearby(level, pos, 1.0D, false)) {
            changeOverTime(state, level, pos, random);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
