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
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftSpritesProvider;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.renderer.environment.state.SpaceSkyRenderState;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.*;

import java.lang.Math;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public abstract class SpaceSkyRenderer implements CustomSkyboxRenderer, AutoCloseable {
    protected static final Identifier ORBITAL_SUN_SPRITE = Constants.id("orbital_sun");
    protected static final Identifier PLANETARY_SUN_SPRITE = Constants.id("atmospheric_sun");
    protected final TextureAtlas celestialsAtlas;
    protected final RenderTarget renderTarget;
    protected final GpuBuffer starBuffer;
    protected final GpuBuffer sunBuffer;
    protected final RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
    protected int starIndexCount;

    static int starCount = 1500;

    protected SpaceSkyRenderer(AtlasManager atlasManager, RenderTarget renderTarget) {
        SpaceSkyRenderer.starCount = ClientConfig.MORE_STARS.get() ? 5000 : 1500;
        this.celestialsAtlas = atlasManager.getAtlasOrThrow(GalacticraftSpritesProvider.CELESTIAL_BODIES);
        this.starBuffer = buildStars();
        this.sunBuffer = buildSunQuad(this.celestialsAtlas);
        this.renderTarget = renderTarget;
    }

    protected abstract void extractSky(PoseStack poseStack, Camera camera, LevelRenderState levelRenderState, Matrix4fc modelViewMatrix, Runnable setupFog);

    /**
     * Each SpaceSkyRenderer should declare its own celestial features to be rendered, this class will have only common renderers
     * @param poseStack stack
     * @param sunAngle angle of the sun
     * @param starAngle angle of stars
     * @param starBrightness brightness of stars
     */
    public void renderSunAndStars(PoseStack poseStack, float sunAngle, float starAngle, float starBrightness) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotation(sunAngle));
        renderSun(-1, poseStack);
        poseStack.popPose();

        if (starBrightness > 0.0F) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotation(starAngle));
            renderStars(starBrightness, poseStack);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    protected Identifier getSunSprite() {
        return ORBITAL_SUN_SPRITE;
    }

    protected void renderSun(float rainBrightness, PoseStack poseStack) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        modelViewStack.translate(0.0F, 100.0F, 0.0F);
        modelViewStack.scale(30.0F, 1.0F, 30.0F);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(new Matrix4f(modelViewStack), new Vector4f(1.0F, 1.0F, 1.0F, rainBrightness));
        GpuTextureView color = this.renderTarget.getColorTextureView();
        GpuTextureView depth = this.renderTarget.getDepthTextureView();
        GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky sun", color, Optional.empty(), depth, OptionalDouble.empty())) {
            renderPass.setPipeline(RenderPipelines.CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
            renderPass.setVertexBuffer(0, this.sunBuffer.slice());
            renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
            renderPass.drawIndexed(6, 1, 0, 0, 0);
        }

        modelViewStack.popMatrix();
    }

    @SuppressWarnings("ConstantConditions")
    protected void renderStars(float starBrightness, PoseStack poseStack) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        RenderPipeline renderPipeline = RenderPipelines.STARS;
        GpuTextureView colorTexture = this.renderTarget.getColorTextureView();
        GpuTextureView depthTexture = this.renderTarget.getDepthTextureView();
        GpuBuffer indexBuffer = this.quadIndices.getBuffer(this.starIndexCount);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(new Matrix4f(modelViewStack), new Vector4f(starBrightness, starBrightness, starBrightness, starBrightness));

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Stars", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty())) {
            renderPass.setPipeline(renderPipeline);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            renderPass.setVertexBuffer(0, this.starBuffer.slice());
            renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
            renderPass.drawIndexed(this.starIndexCount, 1, 0, 0, 0);
        }

        modelViewStack.popMatrix();
    }

    protected GpuBuffer buildStars() {
        RandomSource random = RandomSource.createThreadLocalInstance(10842L);
        float starDistance = 100.0F;

        GpuBuffer buffer;
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * MoonSkyRenderer.starCount * 4)) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);

            for (int i = 0; i < MoonSkyRenderer.starCount; i++) {
                float x = random.nextFloat() * 2.0F - 1.0F;
                float y = random.nextFloat() * 2.0F - 1.0F;
                float z = random.nextFloat() * 2.0F - 1.0F;
                float starSize = 0.15F + random.nextFloat() * 0.1F;
                float lengthSq = Mth.lengthSquared(x, y, z);

                if (!(lengthSq <= 0.010000001F) && !(lengthSq >= 1.0F)) {
                    Vector3f starCenter = new Vector3f(x, y, z).normalize(starDistance);
                    float zRot = (float)(random.nextDouble() * (float) Math.PI * 2.0);
                    Matrix3f rotation = new Matrix3f().rotateTowards(new Vector3f(starCenter).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-zRot);
                    bufferBuilder.addVertex(new Vector3f(starSize, -starSize, 0.0F).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex(new Vector3f(starSize, starSize, 0.0F).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex(new Vector3f(-starSize, starSize, 0.0F).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex(new Vector3f(-starSize, -starSize, 0.0F).mul(rotation).add(starCenter));
                }
            }

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                this.starIndexCount = mesh.drawState().indexCount();
                buffer = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", 40, mesh.vertexBuffer());
            }
        }

        return buffer;
    }

    protected static GpuBuffer buildCelestialQuad(String name, TextureAtlasSprite sprite) {
        VertexFormat format = DefaultVertexFormat.POSITION_TEX;

        GpuBuffer buffer;
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * format.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, format);
            bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV0());
            bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV0());
            bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(sprite.getU1(), sprite.getV1());
            bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(sprite.getU0(), sprite.getV1());

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                buffer = RenderSystem.getDevice().createBuffer(() -> name, 32, mesh.vertexBuffer());
            }
        }

        return buffer;
    }

    protected GpuBuffer buildSunQuad(TextureAtlas atlas) {
        return buildCelestialQuad("Sun quad", atlas.getSprite(getSunSprite()));
    }

    @Override
    public void close() {
        this.starBuffer.close();
        this.sunBuffer.close();
    }

    protected interface SkyboxRendererSupplier extends CustomSkyboxRenderer {
        @Override
        default boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
            return true;
        }

        SpaceSkyRenderer create(AtlasManager atlasManager);
    }
}
