/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.gear.ParachuteModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import io.kalishak.galacticraftlegacy.client.renderer.ParachuteRenderable;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.ParachestRenderState;
import io.kalishak.galacticraftlegacy.world.entity.FallingParachest;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChestBlock;

public class FallingParachestRenderer extends EntityRenderer<FallingParachest, ParachestRenderState> implements ParachuteRenderable<ParachestRenderState> {
    private final SpriteGetter spriteGetter;
    private final ChestModel chestModel;
    private final ParachuteModel<ParachestRenderState> parachuteModel;

    public FallingParachestRenderer(EntityRendererProvider.Context cxt) {
        super(cxt);
        this.spriteGetter = cxt.getSprites();
        this.parachuteModel = new ParachuteModel<>(cxt.bakeLayer(GalacticraftModelLayers.PARACHUTE));
        this.chestModel = new ChestModel(cxt.bakeLayer(ModelLayers.CHEST));
        this.shadowRadius = 0.5F;
    }

    @Override
    public void submit(ParachestRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.angle));
        //poseStack.translate(-1F, -1F, -1F);
        submitNodeCollector.submitModel(
                this.chestModel,
                0.0F,
                poseStack,
                GalacticraftSheets.PARACHEST.renderType(RenderTypes::entityCutout),
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                this.spriteGetter.get(GalacticraftSheets.PARACHEST),
                0,
                null
        );

        poseStack.pushPose();
        poseStack.translate(0.5D, 2.2D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        renderParachute(poseStack, submitNodeCollector, -1, renderState);
        poseStack.popPose();

        poseStack.popPose();
        super.submit(renderState, poseStack, submitNodeCollector, camera);
    }

    @Override
    public ParachestRenderState createRenderState() {
        return new ParachestRenderState();
    }

    @Override
    public void extractRenderState(FallingParachest entity, ParachestRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.blockState = entity.getBlockState();
        reusedState.angle = entity.getBlockState().getValueOrElse(ChestBlock.FACING, Direction.SOUTH).toYRot();
        reusedState.parachuteColor = entity.getParachuteColor();
    }

    @Override
    public ParachuteModel<ParachestRenderState> getParachuteModel() {
        return parachuteModel;
    }

    @Override
    public SpriteId getParachuteMaterial(ParachestRenderState renderState) {
        return GalacticraftSheets.getParachuteMaterial(renderState.parachuteColor);
    }

    @Override
    public SpriteGetter spriteGetter() {
        return this.spriteGetter;
    }
}
