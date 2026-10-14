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
import io.kalishak.galacticraftlegacy.components.flag.*;
import io.kalishak.galacticraftlegacy.world.entity.Flag;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

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
        reusedState.flagData = flag.getFlagData();
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
        TextureAtlasSprite sprite = null;

        if (renderState.flagData instanceof FixedFlagData(Holder<FlagMaterial> material)) {
            if (material.value().flagProvider() instanceof TexturedFlagProvider texturedFlagProvider) {
                sprite = this.flags.getSprite(texturedFlagProvider.image());
            }
        }

        poseStack.pushPose();
        poseStack.translate(seedX, seedY + 1.5F, seedZ);
        poseStack.rotateDegrees(Axis.YP, 180.0F - renderState.rotation);
        poseStack.scale(-1.0F, -1.0F, 0.0F);
        nodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entitySolid(TEXTURES), 1, 1, 1);

        submitFlag(renderState, poseStack, nodeCollector, sprite);
        poseStack.popPose();
    }

    @Override
    protected AABB getBoundingBoxForCulling(Flag entity, float partialTicks) {
        return entity.getBoundingBox().inflate(1.0D, 2.0D, 1.0D);
    }

    private void submitFlag(FlagRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, @Nullable TextureAtlasSprite sprite) {
        poseStack.pushPose();

        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0D, -1.1D, 0.0D);
        FlagData flagData = renderState.flagData;

        nodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) -> {
            float offset = 0.0F;
            float offsetAhead = 0.0F;

            for (int i = 0; i < flagData.width(); i++) {
                for (int j = 0; j < flagData.height(); j++) {
                    if (renderState.windLevel > 0.0F) {
                        offset = (float) (Math.sin(renderState.partialTick / 2.0F + i * 50 + 3) / 25.0F) * i / 30.0F;
                        offsetAhead = (float) (Math.sin(renderState.partialTick / 2.0F + (i + 1) * 50 + 3) / 25.0F) * (i + 1) / 30.0F;
                        offset *= renderState.windLevel;
                        offsetAhead *= renderState.windLevel;
                    }

                    if (flagData.material().value().flagProvider() instanceof TexturedFlagProvider textured) {
                        sumbitSprite(pose, buffer, flagData, i, j, offset, offsetAhead, this.flags.getSprite(textured.image()));
                    } else {
                        sumbitFixed(pose, buffer, flagData, i, j, offset, offsetAhead);
                    }
                }
            }
        });
    }

    private void sumbitSprite(PoseStack.Pose pose, VertexConsumer buffer, FlagData flagData, int x, int y, float windOffset, float windOffsetAhead, TextureAtlasSprite sprite) {
        float offsetX = -flagData.width() / 2.0F;
        float offsetY = -flagData.height() / 2.0F;

        vertex(pose, buffer, x / 24.0F + 0.0F / 24.0F, y / 24.0F + 0.0F / 24.0F + windOffset, windOffset, -1)
                .setUv(sprite.getU(offsetX), sprite.getV(offsetY));
        vertex(pose, buffer, x / 24.0F + 0.0F / 24.0F, y / 24.0F + 1.0F / 24.0F + windOffset, windOffset, -1)
                .setUv(sprite.getU(offsetX), sprite.getV(offsetY));
        vertex(pose, buffer, x / 24.0F + 1.0F / 24.0F, y / 24.0F + 1.0F / 24.0F + windOffsetAhead, windOffsetAhead, -1)
                .setUv(sprite.getU(offsetX), sprite.getV(offsetY));

        vertex(pose, buffer, x / 24.0F + 0.0F / 24.0F, y / 24.0F + 0.0F / 24.0F + windOffset, windOffset, -1)
                .setUv(sprite.getU(offsetX), sprite.getV(offsetY));
        vertex(pose, buffer, x / 24.0F + 1.0F / 24.0F, y / 24.0F + 1.0F / 24.0F + windOffsetAhead, windOffsetAhead, -1)
                .setUv(sprite.getU(offsetX), sprite.getV(offsetY));
        vertex(pose, buffer, x / 24.0F + 1.0F / 24.0F, y / 24.0F + 0.0F / 24.0F + windOffsetAhead, windOffsetAhead, -1)
                .setUv(sprite.getU(offsetX), sprite.getV(offsetY));
    }

    private static void sumbitFixed(PoseStack.Pose pose, VertexConsumer buffer, FlagData flagData, int x, int y, float offset, float offsetAhead) {
        FlagTypeProvider.IndexedColor color = FlagTypeProvider.find(flagData.material().value().flagProvider(), x, y);
        vertex(pose, buffer, x / 24.0F + 0.0F / 24.0F, y / 24.0F + 0.0F / 24.0F + offset, offset, color.color());
        vertex(pose, buffer, x / 24.0F + 0.0F / 24.0F, y / 24.0F + 1.0F / 24.0F + offset, offset, color.color());
        vertex(pose, buffer, x / 24.0F + 1.0F / 24.0F, y / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead, color.color());

        vertex(pose, buffer, x / 24.0F + 0.0F / 24.0F, y / 24.0F + 0.0F / 24.0F + offset, offset, color.color());
        vertex(pose, buffer, x / 24.0F + 1.0F / 24.0F, y / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead, color.color());
        vertex(pose, buffer, x / 24.0F + 1.0F / 24.0F, y / 24.0F + 0.0F / 24.0F + offsetAhead, offsetAhead, color.color());
    }

    private static VertexConsumer vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, int color) {
        return buffer.addVertex(pose, x, y, z).setColor(color).setOverlay(OverlayTexture.NO_OVERLAY);
    }
}
