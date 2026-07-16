/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.renderer.environment.state.SpaceSkyRenderState;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import org.joml.*;

import java.lang.Math;

public class MoonSkyRenderer extends SpaceSkyRenderer {
    public static final Identifier ID = Constants.id("moon");

    @Override
    protected void extractRenderState(LevelRenderState levelRenderState, EnvironmentAttributeProbe attributeProbe, float partialTicks) {
        levelRenderState.setRenderData(SpaceSkyRenderState.TYPE_ID, MoonSkyRenderer.ID);
        levelRenderState.setRenderData(SpaceSkyRenderState.EARTH_ANGLE_ID, attributeProbe.getValue(GalacticraftEnvironmentAttributes.EARTH_ANGLE.get(), partialTicks) * ((float) Math.PI / 180.0F));
        levelRenderState.setRenderData(SpaceSkyRenderState.EARTH_PHASE_ID, attributeProbe.getValue(GalacticraftEnvironmentAttributes.EARTH_PHASE.get(), partialTicks));
    }

    @Override
    protected void renderSkybox(PoseStack poseStack, Camera camera, LevelRenderState levelRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
        renderMoonSky(
                poseStack,
                levelRenderState.skyRenderState.sunAngle,
                levelRenderState.skyRenderState.starAngle,
                levelRenderState.getRenderDataOrDefault(SpaceSkyRenderState.EARTH_ANGLE_ID, 0.0F),
                levelRenderState.getRenderDataOrDefault(SpaceSkyRenderState.EARTH_PHASE_ID, EarthPhase.FULL_EARTH),
                levelRenderState.skyRenderState.starBrightness
        );
    }

    private void renderMoonSky(PoseStack poseStack, float sunAngle, float starAngle, float earthAngle, EarthPhase earthPhase, float starBrightness) {
        renderSunStars(poseStack, sunAngle, starAngle, starBrightness, starBrightness > 0.5F);
        transformEarth(poseStack, earthAngle, earthPhase);
    }

    @Override
    protected Identifier getSunSprite() {
        return PLANETARY_SUN_SPRITE;
    }

    @Override
    protected GpuBuffer buildEarth(TextureAtlas atlas) {
        EarthPhase[] phases = EarthPhase.values();
        VertexFormat format = DefaultVertexFormat.POSITION_TEX;

        GpuBuffer currentBuffer;

        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(phases.length * 4 * format.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, format);

            for (EarthPhase phase : phases) {
                TextureAtlasSprite sprite = atlas.getSprite(Constants.id("earth_phase/" + phase.getSerializedName()));
                bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV1());
                bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV1());
                bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(sprite.getU0(), sprite.getV0());
                bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(sprite.getU1(), sprite.getV0());
            }

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                currentBuffer = RenderSystem.getDevice().createBuffer(() -> "Earth phases", 32, mesh.vertexBuffer());
            }
        }

        return currentBuffer;
    }
}
