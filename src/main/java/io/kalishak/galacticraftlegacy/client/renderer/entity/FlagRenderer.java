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
import io.kalishak.galacticraftlegacy.component.flag.FlagTypeProvider;
import io.kalishak.galacticraftlegacy.world.entity.Flag;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;

public class FlagRenderer extends EntityRenderer<Flag, FlagRenderState> {
    public static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/entity/flag/base.png");
    public static final Identifier GREEN = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/entity/flag/material/green");
    private final FlagModel model;
    private final TextureAtlas flags;

    public FlagRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FlagModel(context.bakeLayer(GalacticraftModelLayers.FLAG));
        this.flags = context.getAtlas(GalacticraftSpritesProvider.FLAGS);
    }

    @Override
    public @NonNull FlagRenderState createRenderState() {
        return new FlagRenderState();
    }

    @Override
    public void extractRenderState(@NonNull Flag flag, @NonNull FlagRenderState reusedState, float partialTick) {
        super.extractRenderState(flag, reusedState, partialTick);
        reusedState.flagData = flag.getFlagData().extractProvider();
        reusedState.id = flag.getId();
        reusedState.rotation = flag.getYRot();
        reusedState.windLevel = flag.getWindLevel();

        try {
            reusedState.colors = reusedState.flagData.resolveColors(id -> this.flags.getSprite(id.texture()));
        } catch (IllegalArgumentException _) {
            reusedState.colors = new byte[reusedState.flagData.width()][reusedState.flagData.height()][3];

            for (int i = 0; i < reusedState.flagData.width(); i++) {
                for (int j = 0; j < reusedState.flagData.height(); j++) {
                    reusedState.colors[i][j][0] = 127;
                    reusedState.colors[i][j][1] = 127;
                    reusedState.colors[i][j][2] = 127;
                }
            }
        }
    }

    @Override
    public void submit(FlagRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, @NonNull CameraRenderState cameraRenderState) {
        long seed = (renderState.id * 493286711L);
        seed *= seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

        poseStack.pushPose();
        poseStack.translate(seedX, seedY + 1.5F, seedZ);
        poseStack.rotateDegrees(Axis.YP, 180.0F - renderState.rotation);
        poseStack.rotateDegrees(Axis.XP, 180.0F);
        nodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entitySolid(TEXTURES), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.outlineColor);
        submitFlag(renderState, poseStack, nodeCollector);
        poseStack.popPose();
    }

    @Override
    protected @NonNull AABB getBoundingBoxForCulling(Flag entity, float partialTicks) {
        return entity.getBoundingBox().inflate(1.0D, 2.0D, 1.0D);
    }

    private void submitFlag(FlagRenderState renderState, PoseStack stack, SubmitNodeCollector nodeCollector) {
        TextureAtlasSprite defaultTexture = this.flags.getSprite(GREEN);
        byte[][][] colors = renderState.colors;
        float windLevel = renderState.windLevel;

        stack.pushPose();
        
        stack.scale(0.5F, 0.5F, 0.5F);
        stack.translate(0.0F, -1.1F, 0.0F);

        nodeCollector.submitCustomGeometry(
                stack,
                RenderTypes.entitySolidZOffsetForward(defaultTexture.atlasLocation()),
                (pose, buffer) -> submitVertexes(
                        pose,
                        buffer,
                        renderState.flagData.width(),
                        renderState.flagData.height(),
                        windLevel,
                        renderState.partialTick,
                        colors,
                        renderState.lightCoords
                )
        );
        stack.popPose();
    }

    private void submitVertexes(PoseStack.Pose pose, VertexConsumer buffer, int width, int height, float windLevel, float partialTick, byte[][][] colors, int lightCoords) {
        float offsetX = (float) (-width) / 2.0F;
        float offsetY = (float) (-height) / 2.0F;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                float x = offsetX + i;
                float y = offsetY + j;
                float offset = 0.0F;
                float offsetAhead = 0.0F;

                if (windLevel > 0) {
                    offset = (float) (Math.sin(partialTick / 2.0F + i * 50 + 3) / 25.0F) * i / 30.0F;
                    offsetAhead = (float) (Math.sin(partialTick / 2.0F + (i + 1) * 50 + 3) / 25.0F) * (i + 1) / 30.0F;
                    offset *= windLevel;
                    offsetAhead *= windLevel;
                }

                int color = FlagTypeProvider.toColor(colors[i][j]);

                vertex(pose, buffer, x, y, offset, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, lightCoords, color);
                vertex(pose, buffer, x, y, offset, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, lightCoords, color);
                vertex(pose, buffer, x, y, offsetAhead, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, lightCoords, color);

                vertex(pose, buffer, x, y, offset, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, lightCoords, color);
                vertex(pose, buffer, x, y, offsetAhead, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, lightCoords, color);
                vertex(pose, buffer, x, y, offsetAhead, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offsetAhead, lightCoords, color);
            }
        }
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float nx, float ny, int lightCoords, int color) {
        buffer.addVertex(pose, x, y, z).setUv(0, 0).setColor(color).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(nx, ny, 0.0F);
    }
}
