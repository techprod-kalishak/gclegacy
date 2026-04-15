/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment.state;

import net.minecraft.client.renderer.state.level.SkyRenderState;

public class SpaceSkyRenderState extends SkyRenderState {
    public float earthAngle;

    public SpaceSkyRenderState(SkyRenderState other) {
        this.skybox = other.skybox;
        this.shouldRenderDarkDisc = other.shouldRenderDarkDisc;
        this.sunAngle = other.sunAngle;
        this.moonAngle = other.moonAngle;
        this.starAngle = other.starAngle;
        this.rainBrightness = other.rainBrightness;
        this.starBrightness = other.starBrightness;
        this.sunriseAndSunsetColor = other.sunriseAndSunsetColor;
        this.moonPhase = other.moonPhase;
        this.skyColor = other.skyColor;
        this.endFlashIntensity = other.endFlashIntensity;
        this.endFlashXAngle = other.endFlashXAngle;
        this.endFlashYAngle = other.endFlashYAngle;
    }
}
