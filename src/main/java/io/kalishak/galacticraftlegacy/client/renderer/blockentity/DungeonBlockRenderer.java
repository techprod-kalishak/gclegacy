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
import io.kalishak.galacticraftlegacy.client.model.item.KeyModel;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.DungeonBlockRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.common.CustomChestRenderer;
import io.kalishak.galacticraftlegacy.world.level.block.DungeonChestBlock;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.ParachestBlock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.DungeonChestBlockEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

public class DungeonBlockRenderer implements BlockEntityRenderer<DungeonChestBlockEntity, DungeonBlockRenderState>, CustomChestRenderer<DungeonBlockRenderState> {
    private final SpriteGetter sprites;
    private final ChestModel chestModel;
    private final KeyModel keyModel;

    public DungeonBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.chestModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.keyModel = new KeyModel(context.bakeLayer(GalacticraftModelLayers.KEY));
    }

    @Override
    public DungeonBlockRenderState createRenderState() {
        return new DungeonBlockRenderState();
    }

    @Override
    public void extractRenderState(DungeonChestBlockEntity blockEntity, DungeonBlockRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        BlockState state = blockEntity.hasLevel() ? blockEntity.getBlockState() : GalacticraftBlocks.MOON_DUNGEON_CHEST.get().defaultBlockState().setValue(ParachestBlock.FACING, Direction.SOUTH);

        renderState.facing = state.getValue(DungeonChestBlock.FACING);
        renderState.open = blockEntity.getOpenNess(partialTick);
        renderState.unlocked = state.getValue(DungeonChestBlock.UNLOCKED);
        renderState.featureTier = blockEntity.getFeatureTier();
    }

    @Override
    public ChestModel getChestModel() {
        return this.chestModel;
    }

    @Override
    public SpriteId getSprite(SpriteGetter spriteGetter, DungeonBlockRenderState renderState) {
        return GalacticraftSheets.getDungeonChestMaterial(renderState.featureTier);
    }

    @Override
    public float getOpenness(DungeonBlockRenderState renderState) {
        float openess = renderState.open;
        openess = 1.0F - openess;
        openess = 1.0F - openess * openess * openess;
        return openess;
    }

    @Override
    public Direction getFacing(DungeonBlockRenderState renderState) {
        return renderState.facing;
    }

    @Override
    public void submit(DungeonBlockRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        submitCommonModel(renderState, submitNodeCollector, poseStack, this.sprites, renderState.breakProgress);

        if (renderState.unlocked) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(new Matrix4f().rotate(Axis.YN.rotationDegrees(renderState.facing.toYRot() + 90.0F)));
            poseStack.mulPose(new Matrix4f().rotate(Axis.XP.rotationDegrees(180.0F)));
            poseStack.translate(0.4F, -1.1F, 0.0F);

            submitNodeCollector.submitModelPart(
                    this.keyModel.root(),
                    poseStack,
                    this.keyModel.renderType(KeyModel.getTexture(renderState.featureTier)),
                    renderState.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    null
            );
            poseStack.popPose();
        }
    }
}
