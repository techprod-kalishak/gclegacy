/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment.sky;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import io.kalishak.galacticraftlegacy.client.renderer.environment.state.SpaceSkyRenderState;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import org.joml.Matrix4fc;

public class OrbitalSkyRenderer extends SpaceSkyRenderer {
    public static final Identifier ID = Constants.id("orbit");

    public OrbitalSkyRenderer() {
    }

    @Override
    protected void renderSkybox(RenderPass renderPass, PoseStack poseStack, Camera camera, LevelRenderState levelRenderState, Matrix4fc modelViewMatrix, GpuBufferSlice skyFog) {
        renderSunMoonEarthAndStars(renderPass, poseStack, levelRenderState.skyRenderState.starAngle, levelRenderState.skyRenderState.starBrightness);
    }

    @Override
    protected void extractRenderState(LevelRenderState levelRenderState, EnvironmentAttributeProbe attributeProbe, float partialTicks) {
        levelRenderState.setRenderData(SpaceSkyRenderState.EARTH_ANGLE_ID, attributeProbe.getValue(GalacticraftEnvironmentAttributes.EARTH_ANGLE.get(), partialTicks) * ((float) Math.PI / 180.0F));
    }

    public void renderSunMoonEarthAndStars(RenderPass renderPass, PoseStack poseStack, float starAngle, float starBrightness) {
        poseStack.pushPose();
        renderSunStars(renderPass, poseStack, 45.0F, starAngle, starBrightness, true);

        poseStack.pushPose();
        poseStack.rotate(Axis.YP, 90);
        renderEarth(renderPass, this.earthBuffer, EarthPhase.FULL_EARTH, poseStack);
        poseStack.popPose();

        poseStack.popPose();
    }
}
