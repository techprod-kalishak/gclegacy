package io.kalishak.galacticraftlegacy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.gear.ParachuteModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.ParachestRenderState;
import io.kalishak.galacticraftlegacy.world.entity.FallingParachest;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class FallingParachestRenderer extends EntityRenderer<FallingParachest, ParachestRenderState> {
    private final MaterialSet materials;
    private final ParachuteModel<ParachestRenderState> parachuteModel;

    public FallingParachestRenderer(EntityRendererProvider.Context cxt) {
        super(cxt);
        this.materials = cxt.getMaterials();
        this.parachuteModel = new ParachuteModel<>(cxt.bakeLayer(GalacticraftModelLayers.PARACHUTE));
        this.shadowRadius = 0.5F;
    }

    @Override
    public boolean shouldRender(FallingParachest fallingParachest, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(fallingParachest, camera, camX, camY, camZ) && fallingParachest.getBlockState() != fallingParachest.level().getBlockState(fallingParachest.blockPosition());
    }

    @Override
    public void submit(ParachestRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        BlockState blockState = renderState.movingParachestRenderState.blockState;

        if (blockState.getRenderShape() == RenderShape.MODEL) {
            poseStack.pushPose();
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            nodeCollector.submitMovingBlock(poseStack, renderState.movingParachestRenderState);
            Material material = GalacticraftSheets.getParachuteMaterial(renderState.parachuteColor);
            RenderType renderType = material.renderType(RenderTypes::entityCutout);
            TextureAtlasSprite sprite = this.materials.get(material);

            poseStack.translate(0.0D, 0.5D, 0.0D);
            nodeCollector.submitModel(
                    this.parachuteModel,
                    renderState,
                    poseStack,
                    renderType,
                    renderState.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    -1,
                    sprite,
                    0,
                    null
            );
            poseStack.popPose();
            super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
        }
    }

    @Override
    public ParachestRenderState createRenderState() {
        return new ParachestRenderState();
    }

    @Override
    public void extractRenderState(FallingParachest entity, ParachestRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().minY, entity.getZ());
        reusedState.movingParachestRenderState.randomSeedPos = entity.getStartPos();
        reusedState.movingParachestRenderState.blockPos = blockPos;
        reusedState.movingParachestRenderState.blockState = entity.getBlockState();
        reusedState.movingParachestRenderState.biome = entity.level().getBiome(blockPos);
        reusedState.movingParachestRenderState.level = entity.level();
        reusedState.movingParachestRenderState.blockEntity = entity.level().getBlockEntity(blockPos);
        reusedState.parachuteColor = entity.getParachuteColor();
    }
}
