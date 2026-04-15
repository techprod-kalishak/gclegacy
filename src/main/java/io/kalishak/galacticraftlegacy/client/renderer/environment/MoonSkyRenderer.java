package io.kalishak.galacticraftlegacy.client.renderer.environment;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.renderer.environment.state.MoonSkyRenderState;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
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
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import org.joml.*;
import org.jspecify.annotations.NonNull;

import java.lang.Math;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class MoonSkyRenderer extends SpaceSkyRenderer<MoonSkyRenderState> {
    //public static final MoonSkyRenderer INSTANCE = new MoonSkyRenderer();
    public static final Identifier ID = Constants.id("moon_sky");
    private GpuBuffer earthBuffer;

    public MoonSkyRenderer() {

    }

    @Override
    protected void init(AtlasManager atlasManager) {
        super.init(atlasManager);
        this.earthBuffer = buildEarthPhases(this.celestialsAtlas);
    }

    @Override
    protected MoonSkyRenderState createRenderState(@NonNull SkyRenderState skyRenderState) {
        return new MoonSkyRenderState(skyRenderState);
    }

    @Override
    public void extractRenderState(float partialTicks, Camera camera, MoonSkyRenderState state, @NonNull EnvironmentAttributeProbe attributeProbe) {
        super.extractRenderState(partialTicks, camera, state, attributeProbe);
        state.earthAngle = attributeProbe.getValue(GalacticraftEnvironmentAttributes.EARTH_ANGLE.get(), partialTicks) * (float) (Math.PI / 180.0D);
        state.earthPhase = attributeProbe.getValue(GalacticraftEnvironmentAttributes.EARTH_PHASE.get(), partialTicks);
    }

    @Override
    protected void extractSky(PoseStack poseStack, Camera camera, LevelRenderState levelRenderState, MoonSkyRenderState renderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
        renderMoonSky(
                poseStack,
                renderState.sunAngle,
                renderState.starAngle,
                renderState.earthAngle,
                renderState.earthPhase,
                renderState.starBrightness
        );
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
        GpuTextureView color = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView depth = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky earth", color, OptionalInt.empty(), depth, OptionalDouble.empty())) {
            renderPass.setPipeline(RenderPipelines.CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
            renderPass.setVertexBuffer(0, this.earthBuffer);
            renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
            renderPass.drawIndexed(baseVertex, 0, 6, 1);
        }

        modelViewStack.popMatrix();
    }

    private static GpuBuffer buildEarthPhases(TextureAtlas atlas) {
        EarthPhase[] phases = EarthPhase.values();
        VertexFormat format = DefaultVertexFormat.POSITION_TEX;

        GpuBuffer var15;
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(phases.length * 4 * format.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, format);

            for (EarthPhase phase : phases) {
                TextureAtlasSprite sprite = atlas.getSprite(Constants.id("earth_phase/" + phase.getSerializedName()));
                bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV1());
                bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV1());
                bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(sprite.getU0(), sprite.getV0());
                bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(sprite.getU1(), sprite.getV0());
            }

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                var15 = RenderSystem.getDevice().createBuffer(() -> "Earth phases", 32, mesh.vertexBuffer());
            }
        }

        return var15;
    }

    @Override
    public void close() {
        super.close();
        this.earthBuffer.close();
    }
}
