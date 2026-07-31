/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.model.object.NasaWorkbenchModel;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.block.entity.NasaWorkbenchBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;

public class NasaWorkbenchBlockRenderer implements BlockEntityRenderer<NasaWorkbenchBlockEntity, BlockEntityRenderState> {
    public static final SpriteId NASA_WORKBENCH_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(Constants.id("nasa_workbench/workbench_arms"));
    private final SpriteGetter sprites;
    private final NasaWorkbenchModel model;

    public NasaWorkbenchBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.model = new NasaWorkbenchModel(context.bakeLayer(GalacticraftModelLayers.NASA_WORKBENCH));
    }

    @Override
    public BlockEntityRenderState createRenderState() {
        return new BlockEntityRenderState();
    }

    @Override
    public void submit(BlockEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.translate(0.0F, -2.0F, 0.0F);
        submitNodeCollector.submitModel(this.model, renderState, poseStack, renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, NASA_WORKBENCH_TEXTURE, this.sprites, 0, renderState.breakProgress);
        poseStack.popPose();
    }
}
