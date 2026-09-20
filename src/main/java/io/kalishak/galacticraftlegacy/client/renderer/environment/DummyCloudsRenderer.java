/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment;

import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.oit.OitRenderPassProvider;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.CustomCloudsRenderer;
import org.joml.Matrix4fc;

public record DummyCloudsRenderer() implements CustomCloudsRenderer {
    public static final Identifier ID = Constants.id("clouds/space");

    @Override
    public void prepare(LevelRenderState levelRenderState, Vec3 camPos, CloudStatus cloudStatus, int cloudColor, float cloudHeight, int cloudRange, Matrix4fc modelViewMatrix) {

    }

    @Override
    public boolean renderClouds(LevelRenderState levelRenderState, CloudStatus cloudStatus, Matrix4fc modelViewMatrix, RenderPass renderPass) {
        return true;
    }

    @Override
    public boolean renderCloudsOit(LevelRenderState levelRenderState, CloudStatus cloudStatus, Matrix4fc modelViewMatrix, OitStage stage, GpuTextureView mainDepthTextureView, OitRenderPassProvider.Parameters params) {
        return true;
    }
}
