/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.renderer.environment.state.SpaceSkyRenderState;
import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import org.joml.*;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

public class MoonSkyRenderer extends SpaceSkyRenderer {
    public static final Identifier ID = Constants.id("moon");
    private final GpuBuffer earthBuffer;

    public MoonSkyRenderer(AtlasManager atlasManager, RenderTarget renderTarget) {
        super(atlasManager, renderTarget);
        this.earthBuffer = buildEarthPhases(this.celestialsAtlas);
    }

    public static void create(BiConsumer<Identifier, CustomSkyboxRenderer> registry) {

    }

    @Override
    protected void extractSky(PoseStack poseStack, Camera camera, LevelRenderState levelRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
        renderMoonSky(
                poseStack,
                levelRenderState.skyRenderState.sunAngle,
                levelRenderState.skyRenderState.starAngle,
                levelRenderState.getRenderDataOrDefault(SpaceSkyRenderState.EARTH_ANGLE_ID, 0.0F),
                levelRenderState.getRenderDataOrDefault(SpaceSkyRenderState.EARTH_PHASE_ID, EarthPhase.FULL_EARTH),
                levelRenderState.skyRenderState.starBrightness
        );
    }

    @Override
    protected Identifier getSunSprite() {
        return PLANETARY_SUN_SPRITE;
    }

    private void renderMoonSky(PoseStack poseStack, float sunAngle, float starAngle, float earthAngle, EarthPhase earthPhase, float starBrightness) {
        renderSunAndStars(poseStack, sunAngle, starAngle, starBrightness);
        renderEarth(poseStack, earthAngle, earthPhase);
    }

    private void renderEarth(PoseStack poseStack, float earthAngle, EarthPhase earthPhase) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotation(earthAngle));
        renderEarth(earthPhase, poseStack);
        poseStack.popPose();
    }

    private void renderEarth(EarthPhase earthPhase, PoseStack poseStack) {
        int baseVertex = earthPhase.getIndex() * 4;
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        modelViewStack.translate(0.0F, 100.0F, 0.0F);
        modelViewStack.scale(20.0F, 1.0F, 20.0F);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(modelViewStack, new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new Vector3f(), new Matrix4f());
        GpuTextureView color = this.renderTarget.getColorTextureView();
        GpuTextureView depth = this.renderTarget.getDepthTextureView();
        GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky earth", color, Optional.empty(), depth, OptionalDouble.empty())) {
            renderPass.setPipeline(RenderPipelines.CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
            renderPass.setVertexBuffer(0, this.earthBuffer.slice());
            renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
            renderPass.drawIndexed(baseVertex, 0, 6, baseVertex, 1);
        }

        modelViewStack.popMatrix();
    }

    private GpuBuffer buildEarthPhases(TextureAtlas atlas) {
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

    @Override
    public void close() {
        super.close();
        this.earthBuffer.close();
    }
}
