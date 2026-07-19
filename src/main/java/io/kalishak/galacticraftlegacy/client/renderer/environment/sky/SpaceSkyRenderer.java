/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment.sky;

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
import io.kalishak.galacticraftlegacy.client.renderer.environment.CelestialSpritesLocations;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.level.MoonPhase;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import org.joml.*;

import java.lang.Math;
import java.util.Optional;
import java.util.OptionalDouble;

public class SpaceSkyRenderer implements CustomSkyboxRenderer, AutoCloseable {
    protected final RenderSystem.AutoStorageIndexBuffer quadIndices;
    protected TextureAtlas celestialsAtlas;
    protected RenderTarget renderTarget;
    protected GpuBuffer starBuffer;
    protected GpuBuffer sunBuffer;
    protected GpuBuffer earthBuffer;
    protected GpuBuffer moonBuffer;

    protected int starIndexCount;
    private boolean seenAtlas = false;

    static int starCount = 1500;

    public SpaceSkyRenderer() {
        SpaceSkyRenderer.starCount = ClientConfig.MORE_STARS.get() ? 5000 : 1500;
        this.quadIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
    }

    protected void extractRenderState(LevelRenderState levelRenderState, EnvironmentAttributeProbe attributeProbe, float partialTicks) {

    }

    @Override
    public final boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
        if (!this.seenAtlas) {
            createBuffers(Minecraft.getInstance().getAtlasManager());
        }

        PoseStack poseStack = new PoseStack();
        Camera camera = Minecraft.getInstance().gameRenderer.mainCamera();

        renderSkybox(poseStack, camera, levelRenderState, modelViewMatrix, setupFog);

        return true;
    }

    protected void renderSkybox(PoseStack poseStack, Camera camera, LevelRenderState levelRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {

    }

    @SubscribeEvent
    public static void extractSkyRenderState(ExtractLevelRenderStateEvent event) {
        LevelRenderState renderState = event.getRenderState();
        Camera camera = event.getCamera();
        EnvironmentAttributeProbe attributeProbe = camera.attributeProbe();
        float partialTicks = camera.getCameraEntityPartialTicks(event.getDeltaTracker());
        CustomSkyboxRenderer renderer = event.getRenderState().customSkyboxRenderer;

        if (renderer instanceof SpaceSkyRenderer skyRenderer && skyRenderer.seenAtlas) {
            skyRenderer.extractRenderState(renderState, attributeProbe, partialTicks);
        }
    }

    @SubscribeEvent
    public void onAtlasBuilt(TextureAtlasStitchedEvent event) {
        AtlasManager atlasManager = Minecraft.getInstance().getAtlasManager();
        createBuffers(atlasManager);
    }

    private void createBuffers(AtlasManager atlasManager) {
        this.celestialsAtlas = atlasManager.getAtlasOrThrow(GalacticraftSpritesProvider.CELESTIAL_BODIES);
        this.renderTarget = Minecraft.getInstance().gameRenderer.mainRenderTarget();
        this.starBuffer = buildStars();
        this.sunBuffer = buildSunQuad(this.celestialsAtlas);
        this.earthBuffer = buildEarth(this.celestialsAtlas);
        this.moonBuffer = buildMoon(this.celestialsAtlas);
        this.seenAtlas = true;
    }

    /**
     * Each SpaceSkyRenderer should declare its own celestial features to be rendered, this class will have only common renderers
     * @param poseStack stack
     * @param sunAngle angle of the sun
     * @param starAngle angle of stars
     * @param starBrightness brightness of stars
     */
    public void renderSunStars(PoseStack poseStack, float sunAngle, float starAngle, float starBrightness, boolean renderStars) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotation(sunAngle));
        renderSun(-1, poseStack);
        poseStack.popPose();

        if (renderStars) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotation(starAngle));
            renderStars(starBrightness, poseStack);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    protected void transformEarth(PoseStack poseStack, float earthAngle, EarthPhase earthPhase) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotation(earthAngle));
        renderEarth(this.earthBuffer, earthPhase, poseStack);
        poseStack.popPose();
    }

    protected Identifier getSunSprite() {
        return CelestialSpritesLocations.ORBITAL_SUN_SPRITE;
    }

    protected void renderMoon(PoseStack poseStack) {
        int baseVertex = MoonPhase.FULL_MOON.index() * 4;
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        modelViewStack.translate(0.0F, 100.0F, 0.0F);
        modelViewStack.scale(20.0F, 1.0F, 20.0F);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(new Matrix4f(modelViewStack), new Vector4f(1.0F, 1.0F, 1.0F, -1));
        GpuTextureView color = this.renderTarget.getColorTextureView();
        GpuTextureView depth = this.renderTarget.getDepthTextureView();
        GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky moon", color, Optional.empty(), depth, OptionalDouble.empty())) {
            renderPass.setPipeline(RenderPipelines.CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
            renderPass.setVertexBuffer(0, this.moonBuffer.slice());
            renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
            renderPass.drawIndexed(6, 1, 0, baseVertex, 0);
        }

        modelViewStack.popMatrix();
    }

    protected void renderEarth(GpuBuffer earthBuffer, EarthPhase earthPhase, PoseStack poseStack) {
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
            renderPass.setVertexBuffer(0, earthBuffer.slice());
            renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
            renderPass.drawIndexed(baseVertex, 0, 6, baseVertex, 1);
        }

        modelViewStack.popMatrix();
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
        GpuBuffer buffer;

        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * SpaceSkyRenderer.starCount * 4)) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);

            for (int i = 0; i < SpaceSkyRenderer.starCount; ++i) {
                float x = random.nextFloat() * 2.0F - 1.0F;
                float y = random.nextFloat() * 2.0F - 1.0F;
                float z = random.nextFloat() * 2.0F - 1.0F;
                float starSize = 0.15F + random.nextFloat() * 0.1F;
                float lengthSq = Mth.lengthSquared(x, y, z);

                if (!(lengthSq <= 0.010000001F) && !(lengthSq >= 1.0F)) {
                    Vector3f starCenter = (new Vector3f(x, y, z)).normalize(100.0F);
                    float zRot = (float)(random.nextDouble() * (double)(float)Math.PI * (double)2.0F);
                    Matrix3f rotation = (new Matrix3f()).rotateTowards((new Vector3f(starCenter)).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-zRot);

                    bufferBuilder.addVertex((new Vector3f(starSize, -starSize, 0.0F)).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex((new Vector3f(starSize, starSize, 0.0F)).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex((new Vector3f(-starSize, starSize, 0.0F)).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex((new Vector3f(-starSize, -starSize, 0.0F)).mul(rotation).add(starCenter));
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

    protected GpuBuffer buildEarth(TextureAtlas atlas) {
        return buildCelestialQuad("Earth quad", atlas.getSprite(CelestialSpritesLocations.FULL_EARTH_SPRITE));
    }

    protected GpuBuffer buildMoon(TextureAtlas atlas) {
        return buildCelestialQuad("Moon quad", atlas.getSprite(CelestialSpritesLocations.FULL_ORBITAL_MOON_SPRITE));
    }

    @Override
    public void close() {
        if (!this.seenAtlas) {
            this.starBuffer.close();
            this.sunBuffer.close();
            this.earthBuffer.close();
            this.moonBuffer.close();
        }
    }
}
