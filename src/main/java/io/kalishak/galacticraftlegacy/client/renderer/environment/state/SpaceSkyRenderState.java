/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment.state;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;

public class SpaceSkyRenderState {
    public static final Identifier ORBIT_ID = Constants.id("orbit");

    public static final ContextKey<Float> EARTH_ANGLE_ID = new ContextKey<>(Constants.id("earth_angle"));
    public static final ContextKey<EarthPhase> EARTH_PHASE_ID = new ContextKey<>(Constants.id("earth_phase"));
    public static final ContextKey<Identifier> TYPE_ID = new ContextKey<>(Constants.id("type_id"));

    public static void extract(float partialTicks, LevelRenderState renderState, EnvironmentAttributeProbe attributeProbe) {
        renderState.setRenderData(EARTH_ANGLE_ID, attributeProbe.getValue(GalacticraftEnvironmentAttributes.EARTH_ANGLE.get(), partialTicks) * (float) (Math.PI / 180.0D));
        renderState.setRenderData(EARTH_PHASE_ID, attributeProbe.getValue(GalacticraftEnvironmentAttributes.EARTH_PHASE.get(), partialTicks));
    }
}
