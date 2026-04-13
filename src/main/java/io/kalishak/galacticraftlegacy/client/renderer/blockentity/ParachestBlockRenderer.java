package io.kalishak.galacticraftlegacy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.ParachestBlockRenderState;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.ParachestBlock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.ParachestBlockEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ParachestBlockRenderer implements BlockEntityRenderer<ParachestBlockEntity, ParachestBlockRenderState> {
    private final MaterialSet materials;
    private final ChestModel model;

    public ParachestBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.materials = context.materials();
        this.model = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
    }

    @Override
    public ParachestBlockRenderState createRenderState() {
        return new ParachestBlockRenderState();
    }

    @Override
    public void extractRenderState(ParachestBlockEntity blockEntity, ParachestBlockRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        BlockState state = blockEntity.hasLevel() ? blockEntity.getBlockState() : GalacticraftBlocks.PARACHEST.get().defaultBlockState().setValue(ParachestBlock.FACING, Direction.SOUTH);

        renderState.angle = state.getValue(ParachestBlock.FACING).toYRot();
        renderState.open = blockEntity.getOpenNess(partialTick);
    }

    @Override
    public void submit(ParachestBlockRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.angle));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        float openess = renderState.open;
        openess = 1.0F - openess;
        openess = 1.0F - openess * openess * openess;
        Material material = GalacticraftSheets.PARACHEST;
        RenderType renderType = material.renderType(RenderTypes::entityCutout);
        TextureAtlasSprite sprite = this.materials.get(material);
        nodeCollector.submitModel(
                this.model,
                openess,
                poseStack,
                renderType,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                sprite,
                0,
                renderState.breakProgress
        );
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(ParachestBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return AABB.encapsulatingFullBlocks(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
    }
}
