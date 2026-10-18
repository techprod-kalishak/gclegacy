/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftSpritesProvider;
import io.kalishak.galacticraftlegacy.client.model.FlagModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.FlagRenderState;
import io.kalishak.galacticraftlegacy.components.flag.FlagTypeProvider;
import io.kalishak.galacticraftlegacy.world.entity.Flag;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class FlagRenderer extends EntityRenderer<Flag, FlagRenderState> {
    public static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/entity/flag.png");
    private final FlagModel model;
    private final TextureAtlas flags;

    public FlagRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FlagModel(context.bakeLayer(GalacticraftModelLayers.FLAG));
        this.flags = context.getAtlas(GalacticraftSpritesProvider.FLAGS);
    }

    @Override
    public FlagRenderState createRenderState() {
        return new FlagRenderState();
    }

    @Override
    public void extractRenderState(Flag flag, FlagRenderState reusedState, float partialTick) {
        super.extractRenderState(flag, reusedState, partialTick);
        reusedState.flagData = flag.getFlagData().extractProvider();
        reusedState.id = flag.getId();
        reusedState.rotation = flag.getYRot();
        reusedState.windLevel = flag.getWindLevel();
    }

    @Override
    public void submit(FlagRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        long seed = (renderState.id * 493286711L);
        seed *= seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

        poseStack.pushPose();
        poseStack.translate(seedX, seedY + 1.5F, seedZ);
        poseStack.rotateDegrees(Axis.YP, 180.0F - renderState.rotation);
        poseStack.scale(-1.0F, -1.0F, 0.0F);
        nodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entitySolid(TEXTURES), 1, 1, 1);

        submitFlag(renderState, poseStack, nodeCollector);
        poseStack.popPose();
    }

    @Override
    protected AABB getBoundingBoxForCulling(Flag entity, float partialTicks) {
        return entity.getBoundingBox().inflate(1.0D, 2.0D, 1.0D);
    }

    private void submitFlag(FlagRenderState renderState, PoseStack stack, SubmitNodeCollector nodeCollector) {
        List<FlagTypeProvider.IndexedColor> colorList = renderState.flagData.resolve(id -> this.flags.getSprite(id.texture()));
        float windLevel = renderState.windLevel;

        stack.pushPose();
        
        stack.scale(0.5F, 0.5F, 0.5F);
        stack.translate(0.0F, -1.1F, 0.0F);

        nodeCollector.submitCustomGeometry(
                stack,
                RenderTypes.endPortal(),
                (pose, buffer) -> submitVertexes(pose, buffer, renderState.flagData.width(), renderState.flagData.height(), windLevel, renderState.partialTick, colorList, renderState.lightCoords)
        );
        stack.popPose();
    }
    
    private void submitVertexes(PoseStack.Pose pose, VertexConsumer buffer, int width, int height, float windLevel, float partialTick, List<FlagTypeProvider.IndexedColor> colorList, int lightCoords) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pose.translate(0, -1.0F, 0);
                float offset = 0.0F;
                float offsetAhead = 0.0F;

                if (windLevel > 0) {
                    offset = (float) (Math.sin(partialTick / 2.0F + i * 50 + 3) / 25.0F) * i / 30.0F;
                    offsetAhead = (float) (Math.sin(partialTick / 2.0F + (i + 1) * 50 + 3) / 25.0F) * (i + 1) / 30.0F;
                    offset *= windLevel;
                    offsetAhead *= windLevel;
                }

                int color = FlagTypeProvider.find(colorList, i, j).color();

                vertex(pose, buffer, i, j, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset, 0, 0, 0, lightCoords, color);
                vertex(pose, buffer, i, j, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset, 0, 1, 0, lightCoords, color);
                vertex(pose, buffer, i, j, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead, 1, 1, 0, lightCoords, color);

                vertex(pose, buffer, i, j, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset, 0, 0, 0, lightCoords, color);
                vertex(pose, buffer, i, j, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead, 1, 1, 0, lightCoords, color);
                vertex(pose, buffer, i, j, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offsetAhead, offsetAhead, 1, 0, 0, lightCoords, color);
            }
        }
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float u, float v, float z, int nx, int ny, int nz, int lightCoords, int color) {
        buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, nx, ny, nz);
    }
}
