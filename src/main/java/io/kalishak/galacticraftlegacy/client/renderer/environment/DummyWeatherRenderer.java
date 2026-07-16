/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.CustomWeatherEffectRenderer;

public record DummyWeatherRenderer() implements CustomWeatherEffectRenderer {
    public static final Identifier ID = Constants.id("weather_effect/space");

    @Override
    public boolean renderSnowAndRain(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, Vec3 camPos) {
        return true;
    }

    @Override
    public boolean tickRain(ClientLevel level, long ticks, Camera camera) {
        return false;
    }
}
