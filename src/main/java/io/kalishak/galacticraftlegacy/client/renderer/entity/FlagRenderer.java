/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.level.race.FlagData;
import io.kalishak.galacticraftlegacy.client.model.FlagModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.FlagRenderState;
import io.kalishak.galacticraftlegacy.client.resources.GalacticraftModelBakery;
import io.kalishak.galacticraftlegacy.world.entity.Flag;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;

public class FlagRenderer extends EntityRenderer<Flag, FlagRenderState> {
    public static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/entity/flag.png");
    private final FlagModel model;

    public FlagRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FlagModel(context.bakeLayer(GalacticraftModelLayers.FLAG));
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
    }

    @Override
    public void submit(FlagRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        FlagData flagData = renderState.flagData;
        SpriteId spriteId = GalacticraftModelBakery.FLAG_BASE;
        RenderType renderType = spriteId.renderType(RenderTypes::entitySolid);

        long seed = (renderState.id * 493286711L);
        seed *= seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

        poseStack.pushPose();
        poseStack.translate(seedX, seedY + 1.5F, seedZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - renderState.rotation));
        poseStack.scale(-1.0F, -1.0F, 0.0F);
        nodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entitySolid(TEXTURES), 1, 1, 1, null);
        //submitFlag(renderState, poseStack, nodeCollector, renderType);
        poseStack.popPose();
    }

    @Override
    protected AABB getBoundingBoxForCulling(Flag flag) {
        return flag.getBoundingBox().inflate(1.0D, 2.0D, 1.0D);
    }

    private void submitFlag(FlagRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, RenderType renderType) {
        poseStack.pushPose();

        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0D, -1.1D, 0.0D);

        FlagData flagData = renderState.flagData;
        float offset = 0.0F;
        float offsetAhead = 0.0F;
    }
}
