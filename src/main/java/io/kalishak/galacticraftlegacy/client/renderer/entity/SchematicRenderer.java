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
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.SchematicRenderState;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftSpritesProvider;
import io.kalishak.galacticraftlegacy.world.entity.SchematicEntity;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class SchematicRenderer extends EntityRenderer<SchematicEntity, SchematicRenderState> {
    private static final Identifier BACK_SPRITE_LOCATION = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "back");
    private final TextureAtlas schematicAtlas;

    public SchematicRenderer(EntityRendererProvider.Context cxt) {
        super(cxt);
        this.schematicAtlas = cxt.getAtlas(GalacticraftSpritesProvider.SCHEMATICS);
    }

    @Override
    public void submit(SchematicRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        SchematicVariant schematicVariant = renderState.schematicVariant;
        
        if (schematicVariant != null) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - renderState.direction.get2DDataValue() * 90));
            TextureAtlasSprite schematicSprite = this.schematicAtlas.getSprite(schematicVariant.assetId());
            TextureAtlasSprite backSprite = this.schematicAtlas.getSprite(BACK_SPRITE_LOCATION);
            render(poseStack, submitNodeCollector, RenderTypes.entitySolidZOffsetForward(backSprite.atlasLocation()), renderState.lightCoordsPerBlock, schematicSprite, backSprite);
            poseStack.popPose();
            super.submit(renderState, poseStack, submitNodeCollector, camera);
        }
    }

    @Override
    public SchematicRenderState createRenderState() {
        return new SchematicRenderState();
    }

    @Override
    public void extractRenderState(SchematicEntity schematicEntity, SchematicRenderState reusedState, float partialTick) {
        super.extractRenderState(schematicEntity, reusedState, partialTick);
        Direction direction = schematicEntity.getDirection();
        SchematicVariant schematicVariant = schematicEntity.getSchematic().schematic().value();
        reusedState.direction = direction;
        reusedState.schematicVariant = schematicVariant;
        int width = 4;
        int height = 4;
        
        if (reusedState.lightCoordsPerBlock.length != width * height) {
            reusedState.lightCoordsPerBlock = new int[width * height];
        }

        float widthStart = -width / 2.0F;
        float HeightStart = -height / 2.0F;
        Level level = schematicEntity.level();

        for (int k = 0; k < height; k++) {
            for (int l = 0; l < width; l++) {
                float offset = l + widthStart + 0.5F;
                float verticalOffset = k + HeightStart + 0.5F;
                int fixedPosX = schematicEntity.getBlockX();
                int fixedPosY = Mth.floor(schematicEntity.getY() + verticalOffset);
                int fixedPosZ = schematicEntity.getBlockZ();
                
                switch (direction) {
                    case NORTH -> fixedPosX = Mth.floor(schematicEntity.getX() + offset);
                    case WEST -> fixedPosZ = Mth.floor(schematicEntity.getZ() - offset);
                    case SOUTH -> fixedPosX = Mth.floor(schematicEntity.getX() - offset);
                    case EAST -> fixedPosZ = Mth.floor(schematicEntity.getZ() + offset);
                }

                reusedState.lightCoordsPerBlock[l + k * width] = LevelRenderer.getLightCoords(level, new BlockPos(fixedPosX, fixedPosY, fixedPosZ));
            }
        }
    }
    
    private void render(PoseStack poseStack, SubmitNodeCollector nodeCollector, RenderType renderType, int[] lightCoords, TextureAtlasSprite frontSprite, TextureAtlasSprite backSprite) {
        nodeCollector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> {
            float f = -4 / 2.0F;
            float f1 = -4 / 2.0F;
            float f2 = 0.03125F;
            float f3 = backSprite.getU0();
            float f4 = backSprite.getU1();
            float f5 = backSprite.getV0();
            float f6 = backSprite.getV1();
            float f7 = backSprite.getU0();
            float f8 = backSprite.getU1();
            float f9 = backSprite.getV0();
            float f10 = backSprite.getV(0.0625F);
            float f11 = backSprite.getU0();
            float f12 = backSprite.getU(0.0625F);
            float f13 = backSprite.getV0();
            float f14 = backSprite.getV1();
            double d0 = 1.0 / 4;
            double d1 = 1.0 / 4;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    float f15 = f + (i + 1);
                    float f16 = f + i;
                    float f17 = f1 + (j + 1);
                    float f18 = f1 + j;
                    int k = lightCoords[i + j * 4];
                    float f19 = frontSprite.getU((float)(d0 * (4 - i)));
                    float f20 = frontSprite.getU((float)(d0 * (4 - (i + 1))));
                    float f21 = frontSprite.getV((float)(d1 * (4 - j)));
                    float f22 = frontSprite.getV((float)(d1 * (4 - (j + 1))));
                    this.vertex(pose, consumer, f15, f18, f20, f21, -0.03125F, 0, 0, -1, k);
                    this.vertex(pose, consumer, f16, f18, f19, f21, -0.03125F, 0, 0, -1, k);
                    this.vertex(pose, consumer, f16, f17, f19, f22, -0.03125F, 0, 0, -1, k);
                    this.vertex(pose, consumer, f15, f17, f20, f22, -0.03125F, 0, 0, -1, k);
                    this.vertex(pose, consumer, f15, f17, f4, f5, 0.03125F, 0, 0, 1, k);
                    this.vertex(pose, consumer, f16, f17, f3, f5, 0.03125F, 0, 0, 1, k);
                    this.vertex(pose, consumer, f16, f18, f3, f6, 0.03125F, 0, 0, 1, k);
                    this.vertex(pose, consumer, f15, f18, f4, f6, 0.03125F, 0, 0, 1, k);
                    this.vertex(pose, consumer, f15, f17, f7, f9, -0.03125F, 0, 1, 0, k);
                    this.vertex(pose, consumer, f16, f17, f8, f9, -0.03125F, 0, 1, 0, k);
                    this.vertex(pose, consumer, f16, f17, f8, f10, 0.03125F, 0, 1, 0, k);
                    this.vertex(pose, consumer, f15, f17, f7, f10, 0.03125F, 0, 1, 0, k);
                    this.vertex(pose, consumer, f15, f18, f7, f9, 0.03125F, 0, -1, 0, k);
                    this.vertex(pose, consumer, f16, f18, f8, f9, 0.03125F, 0, -1, 0, k);
                    this.vertex(pose, consumer, f16, f18, f8, f10, -0.03125F, 0, -1, 0, k);
                    this.vertex(pose, consumer, f15, f18, f7, f10, -0.03125F, 0, -1, 0, k);
                    this.vertex(pose, consumer, f15, f17, f12, f13, 0.03125F, -1, 0, 0, k);
                    this.vertex(pose, consumer, f15, f18, f12, f14, 0.03125F, -1, 0, 0, k);
                    this.vertex(pose, consumer, f15, f18, f11, f14, -0.03125F, -1, 0, 0, k);
                    this.vertex(pose, consumer, f15, f17, f11, f13, -0.03125F, -1, 0, 0, k);
                    this.vertex(pose, consumer, f16, f17, f12, f13, -0.03125F, 1, 0, 0, k);
                    this.vertex(pose, consumer, f16, f18, f12, f14, -0.03125F, 1, 0, 0, k);
                    this.vertex(pose, consumer, f16, f18, f11, f14, 0.03125F, 1, 0, 0, k);
                    this.vertex(pose, consumer, f16, f17, f11, f13, 0.03125F, 1, 0, 0, k);
                }
            }
        });
    }

    private void vertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float u, float v, float z, int normalX, int normalY, int normalZ, int packedLight) {
        consumer.addVertex(pose, x, y, z)
                .setColor(-1)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, normalX, normalY, normalZ);
    }
}
