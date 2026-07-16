/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.CustomCloudsRenderer;
import org.joml.Matrix4fc;

public record DummyCloudsRenderer() implements CustomCloudsRenderer {
    public static final Identifier ID = Constants.id("clouds/space");

    @Override
    public boolean renderClouds(LevelRenderState levelRenderState, Vec3 camPos, CloudStatus cloudStatus, int cloudColor, float cloudHeight, int cloudRange, Matrix4fc modelViewMatrix) {
        return true;
    }
}
