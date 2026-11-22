/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import io.kalishak.galacticraftlegacy.client.model.item.ThrownMeteorChunkModel;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.ThrownMeteorChunkRenderState;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.entity.projectile.ThrownMeteorChunk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;

public class ThrownMeteorRenderer extends EntityRenderer<ThrownMeteorChunk, ThrownMeteorChunkRenderState> {
    public static final Identifier TEXTURES = Constants.id("textures/entity/throwable_meteor_chunk/plain.png");
    public static final Identifier HOT_TEXTURES = Constants.id("textures/entity/throwable_meteor_chunk/hot.png");

    public ThrownMeteorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ThrownMeteorChunkRenderState createRenderState() {
        return new ThrownMeteorChunkRenderState();
    }

    @Override
    public void submit(ThrownMeteorChunkRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        QuadCollection baseQuads = Minecraft.getInstance().getModelManager().getStandaloneModel(ThrownMeteorChunkModel.KEY);

        if (baseQuads != null) {
            poseStack.pushPose();

            RenderType renderType = RenderTypes.entitySolid(state.isHot ? HOT_TEXTURES : TEXTURES);
            submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
                baseQuads.getQuads(Direction.fromYRot(camera.yRot)).forEach(quad -> {
                    QuadInstance instance = new QuadInstance();
                    instance.setLightCoords(state.lightCoords);
                    instance.setOverlayCoords(OverlayTexture.NO_OVERLAY);
                    vertexConsumer.putBakedQuad(pose, quad, instance);
                });
            });

            poseStack.popPose();
        }
    }

    @Override
    public void extractRenderState(ThrownMeteorChunk entity, ThrownMeteorChunkRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isHot = entity.isHot();
    }
}
