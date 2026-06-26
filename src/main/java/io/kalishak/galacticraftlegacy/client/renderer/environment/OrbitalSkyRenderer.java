package io.kalishak.galacticraftlegacy.client.renderer.environment;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.MoonPhase;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import org.joml.*;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

public class OrbitalSkyRenderer extends SpaceSkyRenderer {
    public static final Identifier ID = Constants.id("orbit");
    private GpuBuffer moonBuffer;

    public static void create(BiConsumer<Identifier, CustomSkyboxRenderer> registry) {
        if (RenderSystem.isOnRenderThread()) {
            registry.accept(ID, new OrbitalSkyRenderer());
        }
    }

    @Override
    protected void init(AtlasManager atlasManager) {
        super.init(atlasManager);
        this.moonBuffer = buildMoonPhases(this.celestialsAtlas);
    }

    @Override
    protected void extractSky(PoseStack poseStack, Camera camera, LevelRenderState levelRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
        renderSunMoonEarthAndStars(
                poseStack,
                levelRenderState.skyRenderState.sunAngle,
                levelRenderState.skyRenderState.moonAngle,
                levelRenderState.skyRenderState.starAngle,
                levelRenderState.skyRenderState.starBrightness
        );
    }

    public void renderSunMoonEarthAndStars(PoseStack poseStack, float sunAngle, float moonAngle, float starAngle, float starBrightness) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotation(sunAngle));
        renderSun(poseStack);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotation(moonAngle));
        renderMoon(poseStack);
        poseStack.popPose();

        if (starBrightness > 0.0F) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotation(starAngle));
            renderStars(starBrightness, poseStack);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private void renderMoon(PoseStack poseStack) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        modelViewStack.translate(0.0F, 100.0F, 0.0F);
        modelViewStack.scale(20.0F, 1.0F, 20.0F);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(modelViewStack, new Vector4f(1.0F, 1.0F, 1.0F, 0.0F), new Vector3f(), new Matrix4f());
        GpuTextureView color = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView depth = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky moon", color, OptionalInt.empty(), depth, OptionalDouble.empty())) {
            renderPass.setPipeline(RenderPipelines.CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
            renderPass.setVertexBuffer(0, this.moonBuffer);
            renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
            renderPass.drawIndexed(0, 0, 6, 1);
        }

        modelViewStack.popMatrix();
    }

    private static GpuBuffer buildMoonPhases(TextureAtlas atlas) {
        MoonPhase[] phases = MoonPhase.values();
        VertexFormat format = DefaultVertexFormat.POSITION_TEX;

        GpuBuffer var15;
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(phases.length * 4 * format.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, format);

            for (MoonPhase phase : phases) {
                TextureAtlasSprite sprite = atlas.getSprite(Identifier.withDefaultNamespace("moon/" + phase.getSerializedName()));
                bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV1());
                bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV1());
                bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(sprite.getU0(), sprite.getV0());
                bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(sprite.getU1(), sprite.getV0());
            }

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                var15 = RenderSystem.getDevice().createBuffer(() -> "Moon phases", 32, mesh.vertexBuffer());
            }
        }

        return var15;
    }

    @Override
    public void close() {
        super.close();
        this.moonBuffer.close();
    }
}
