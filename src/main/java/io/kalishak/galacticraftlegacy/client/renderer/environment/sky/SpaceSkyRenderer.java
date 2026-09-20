/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment.sky;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftSpritesProvider;
import io.kalishak.galacticraftlegacy.client.renderer.environment.CelestialSpritesLocations;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
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
import java.util.function.Supplier;

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
    public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, GpuBufferSlice skyFog) {
        if (!this.seenAtlas) {
            createBuffers(Minecraft.getInstance().getAtlasManager());
        }

        PoseStack poseStack = new PoseStack();
        Camera camera = Minecraft.getInstance().gameRenderer.mainCamera();

        //renderSkybox(poseStack, camera, levelRenderState, modelViewMatrix, skyFog);

        return true;
    }

    protected void renderSkybox(RenderPass renderPass, PoseStack poseStack, Camera camera, LevelRenderState levelRenderState, Matrix4fc modelViewMatrix, GpuBufferSlice skyFog) {

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
    public void renderSunStars(RenderPass renderPass, PoseStack poseStack, float sunAngle, float starAngle, float starBrightness, boolean renderStars) {
        poseStack.pushPose();
        poseStack.rotateDegrees(Axis.YP, -90.0F);
        poseStack.pushPose();

        poseStack.rotate(Axis.XP, sunAngle);
        renderSun(renderPass, 0.5F, poseStack);
        poseStack.popPose();

        if (renderStars) {
            poseStack.pushPose();
            poseStack.rotate(Axis.XP, starAngle);
            renderStars(renderPass, starBrightness, poseStack);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    protected void transformEarth(RenderPass renderPass, PoseStack poseStack, float earthAngle, EarthPhase earthPhase) {
        poseStack.pushPose();
        poseStack.rotate(Axis.XP, earthAngle);
        renderEarth(renderPass, this.earthBuffer, earthPhase, poseStack);
        poseStack.popPose();
    }

    protected Identifier getSunSprite() {
        return CelestialSpritesLocations.ORBITAL_SUN_SPRITE;
    }

    protected void renderMoon(RenderPass renderPass, PoseStack poseStack) {
        int baseVertex = MoonPhase.FULL_MOON.index() * 4;
        Matrix4f modelViewMatrix = this.applyCelestialBodyTransform(poseStack, 100.0F, 20.0F);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(modelViewMatrix, new Vector4f(1.0F, 1.0F, 1.0F, 0.6F));
        renderCelestialBody(() -> "Moon", renderPass, dynamicTransforms, this.quadIndices.getBuffer(6), this.moonBuffer, baseVertex);
    }

    protected void renderEarth(RenderPass renderPass, final GpuBuffer earthBuffer, EarthPhase earthPhase, PoseStack poseStack) {
        int baseVertex = earthPhase.getIndex() * 4;
        Matrix4f modelViewMatrix = this.applyCelestialBodyTransform(poseStack, 100.0F, 20.0F);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(modelViewMatrix, new Vector4f(1.0F, 1.0F, 1.0F, 0.6F));
        renderCelestialBody(() -> "Earth", renderPass, dynamicTransforms, this.quadIndices.getBuffer(6), earthBuffer, baseVertex);
    }

    protected void renderSun(RenderPass renderPass, float rainBrightness, PoseStack poseStack) {
        Matrix4f modelViewMatrix = this.applyCelestialBodyTransform(poseStack, 100.0F, 30.0F);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(modelViewMatrix, new Vector4f(1.0F, 1.0F, 1.0F, rainBrightness));
        renderCelestialBody(() -> "Sun", renderPass, dynamicTransforms, this.quadIndices.getBuffer(6), this.sunBuffer, 0);
    }

    @SuppressWarnings("ConstantConditions")
    protected void renderStars(RenderPass renderPass, float starBrightness, PoseStack poseStack) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        GpuBuffer indexBuffer = this.quadIndices.getBuffer(this.starIndexCount);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(new Matrix4f(modelViewStack), new Vector4f(starBrightness, starBrightness, starBrightness, starBrightness));
        renderPass.pushDebugGroup(() -> "Stars");
        renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.STARS));
        RenderSystem.bindDefaultUniforms(renderPass);
        renderPass.setUniform("DynamicTransforms", dynamicTransforms);
        renderPass.setVertexBuffer(0, this.starBuffer.slice());
        renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
        renderPass.drawIndexed(this.starIndexCount, 1, 0, 0, 0);
        renderPass.popDebugGroup();
        modelViewStack.popMatrix();
    }

    protected Matrix4f applyCelestialBodyTransform(PoseStack poseStack, float height, float scale) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        modelViewStack.translate(0.0F, height, 0.0F);
        modelViewStack.scale(scale, 1.0F, scale);
        Matrix4f modelViewMatrix = new Matrix4f(modelViewStack);
        modelViewStack.popMatrix();
        return modelViewMatrix;
    }

    protected void renderCelestialBody(Supplier<String> label, RenderPass renderPass, GpuBufferSlice dynamicTransforms, GpuBuffer indexBuffer, GpuBuffer vertexBuffer, int baseVertex) {
        renderPass.pushDebugGroup(label);
        renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.CELESTIAL));
        RenderSystem.bindDefaultUniforms(renderPass);
        renderPass.setUniform("DynamicTransforms", dynamicTransforms);
        renderPass.setUniform("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
        renderPass.setVertexBuffer(0, vertexBuffer.slice());
        renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
        renderPass.drawIndexed(6, 1, 0, baseVertex, 0);
        renderPass.popDebugGroup();
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
