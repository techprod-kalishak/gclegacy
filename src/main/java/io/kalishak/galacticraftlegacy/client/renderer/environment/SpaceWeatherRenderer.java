/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.CustomWeatherEffectRenderer;

import java.util.function.BiConsumer;

public class SpaceWeatherRenderer implements CustomWeatherEffectRenderer {
    public static final Identifier ID = Constants.id("weather_effect/space");

    public static void create(BiConsumer<Identifier, CustomWeatherEffectRenderer> registry) {
    }

    @Override
    public boolean renderSnowAndRain(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, MultiBufferSource bufferSource, Vec3 camPos) {
        return false;
    }

    @Override
    public boolean tickRain(ClientLevel level, int ticks, Camera camera) {
        return false;
    }
}
