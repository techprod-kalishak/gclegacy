/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.FluidTankBlockRenderState;
import io.kalishak.galacticraftlegacy.world.level.block.entity.FluidTankBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class FluidTankBlockRenderer implements BlockEntityRenderer<FluidTankBlockEntity, FluidTankBlockRenderState> {
    private static final int MAX_FLUID_LEVEL = 16000;
    private final BlockModelResolver blockModelResolver;

    public FluidTankBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public FluidTankBlockRenderState createRenderState() {
        return new FluidTankBlockRenderState();
    }

    @Override
    public void extractRenderState(FluidTankBlockEntity blockEntity, FluidTankBlockRenderState fluidTankBlockRenderState, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, fluidTankBlockRenderState, partialTicks, cameraPosition, breakProgress);
        fluidTankBlockRenderState.fluid = blockEntity.getFluidStack();
        fluidTankBlockRenderState.liquidBlock = new BlockModelRenderState();
        FluidState fluidState = fluidTankBlockRenderState.fluid.getFluid().defaultFluidState();
        this.blockModelResolver.update(fluidTankBlockRenderState.liquidBlock, fluidState.createLegacyBlock(), BlockDisplayContext.create());
    }

    @Override
    public void submit(FluidTankBlockRenderState fluidTankBlockRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (!fluidTankBlockRenderState.liquidBlock.isEmpty()) {
            float level = (float) fluidTankBlockRenderState.fluid.getAmount() / MAX_FLUID_LEVEL;

            poseStack.pushPose();
            poseStack.translate(0.1F, 0.0F, 0.1F);
            poseStack.scale(0.8F, 1.0F, 0.8F);
            fluidTankBlockRenderState.liquidBlock.submit(poseStack, submitNodeCollector, fluidTankBlockRenderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

    }
}
