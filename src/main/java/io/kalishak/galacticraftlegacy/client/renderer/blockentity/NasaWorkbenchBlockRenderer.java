/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.block.NasaWorkbenchModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.common.NasaWorkbenchModelRenderer;
import io.kalishak.galacticraftlegacy.world.level.block.entity.NasaWorkbenchBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.phys.AABB;

public class NasaWorkbenchBlockRenderer implements BlockEntityRenderer<NasaWorkbenchBlockEntity, BlockEntityRenderState>, NasaWorkbenchModelRenderer<BlockEntityRenderState> {
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
    public NasaWorkbenchModel getWorkbenchModel() {
        return this.model;
    }

    @Override
    public void submit(BlockEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        submitCommonModel(renderState, submitNodeCollector, poseStack, this.sprites, renderState.breakProgress);
    }

    @Override
    public AABB getRenderBoundingBox(NasaWorkbenchBlockEntity blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity).expandTowards(0.2D, 1.0D, 0.2D);
    }
}
