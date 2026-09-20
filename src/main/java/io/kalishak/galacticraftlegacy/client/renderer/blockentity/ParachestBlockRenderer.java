/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.OpenableBlockRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.common.CustomChestRenderer;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.ParachestBlock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.ParachestBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ParachestBlockRenderer implements BlockEntityRenderer<ParachestBlockEntity, OpenableBlockRenderState>, CustomChestRenderer<OpenableBlockRenderState> {
    private final SpriteGetter sprites;
    private final ChestModel model;

    public ParachestBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.model = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
    }

    @Override
    public OpenableBlockRenderState createRenderState() {
        return new OpenableBlockRenderState();
    }

    @Override
    public void extractRenderState(ParachestBlockEntity blockEntity, OpenableBlockRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        BlockState state = blockEntity.hasLevel() ? blockEntity.getBlockState() : GalacticraftBlocks.PARACHEST.get().defaultBlockState().setValue(ParachestBlock.FACING, Direction.SOUTH);

        renderState.facing = state.getValue(ParachestBlock.FACING);
        renderState.open = blockEntity.getOpenNess(partialTick);
    }

    @Override
    public Model<Float> getChestModel() {
        return this.model;
    }

    @Override
    public SpriteId getSprite(SpriteGetter spriteGetter, OpenableBlockRenderState renderState) {
        return GalacticraftSheets.PARACHEST;
    }

    @Override
    public float getOpenness(OpenableBlockRenderState renderState) {
        float openess = renderState.open;
        openess = 1.0F - openess;
        openess = 1.0F - openess * openess * openess;
        return openess;
    }

    @Override
    public Direction getFacing(OpenableBlockRenderState renderState) {
        return renderState.facing;
    }

    @Override
    public void submit(OpenableBlockRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        submitCommonModel(renderState, submitNodeCollector, poseStack, this.sprites, null);
    }

    @Override
    public AABB getRenderBoundingBox(ParachestBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return AABB.encapsulatingFullBlocks(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
    }
}
