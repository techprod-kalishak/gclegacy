/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment.state;

import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.client.renderer.state.level.SkyRenderState;

public class MoonSkyRenderState extends SpaceSkyRenderState {
    public EarthPhase earthPhase = EarthPhase.FULL_EARTH;

    public MoonSkyRenderState(SkyRenderState other) {
        super(other);
    }
}
