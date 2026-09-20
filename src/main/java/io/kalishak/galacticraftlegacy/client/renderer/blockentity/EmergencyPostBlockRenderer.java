package io.kalishak.galacticraftlegacy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.block.EmergencyPostModel;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.EmergencyPostRenderState;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.block.EmergencyPostBlock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.EmergencyPostBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EmergencyPostBlockRenderer implements BlockEntityRenderer<EmergencyPostBlockEntity, EmergencyPostRenderState> {
    private static final Identifier FLIP_TEXTURES = Constants.texture("entity/emergency_post/flip");
    private static final Identifier BOX_TEXTURES = Constants.texture("entity/emergency_post/box");
    private static final Identifier TANK_TEXTURES = Constants.texture("entity/emergency_post/tank");
    private static final Identifier MASK_TEXTURES = Constants.texture("entity/emergency_post/mask");
    private static final Identifier PACK_TEXTURES = Constants.texture("entity/emergency_post/pack");
    private final EmergencyPostModel postModel;

    public EmergencyPostBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.postModel = new EmergencyPostModel(context.entityModelSet()::bakeLayer);
    }

    @Override
    public EmergencyPostRenderState createRenderState() {
        return new EmergencyPostRenderState();
    }

    @Override
    public void extractRenderState(EmergencyPostBlockEntity blockEntity, EmergencyPostRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        blockEntity.extractSides(state::updateOpenness, partialTicks);
        state.hasKit = blockEntity.getBlockState().getValue(EmergencyPostBlock.WITH_KIT);
        this.postModel.setVisibility(blockEntity.isOpened());
    }

    @Override
    public void submit(EmergencyPostRenderState emergencyPostRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);

//        if (emergencyPostRenderState.hasKit) {
//            poseStack.pushPose();
//            submitNodeCollector
//                    .submitModel(
//                            this.postModel.kit,
//                            emergencyPostRenderState,
//                            poseStack,
//                            PACK_TEXTURES,
//                            emergencyPostRenderState.lightCoords,
//                            OverlayTexture.NO_OVERLAY,
//                            0,
//                            null
//                    );
//            poseStack.mulPose(Axis.XP.rotation(180.0F));
//            poseStack.translate(0.0F, 0.0F, -0.07F);
//            submitNodeCollector
//                    .submitModel(
//                            this.postModel.mask,
//                            emergencyPostRenderState,
//                            poseStack,
//                            MASK_TEXTURES,
//                            emergencyPostRenderState.lightCoords,
//                            OverlayTexture.NO_OVERLAY,
//                            0,
//                            null
//                    );
//            poseStack.translate(0.1F, 0.11F, 0.3F);
//            submitNodeCollector
//                    .submitModel(
//                            this.postModel.leftTank,
//                            emergencyPostRenderState,
//                            poseStack,
//                            TANK_TEXTURES,
//                            emergencyPostRenderState.lightCoords,
//                            OverlayTexture.NO_OVERLAY,
//                            0,
//                            null
//                    );
//            poseStack.translate(-0.2F, 0.0F, 0.0F);
//            submitNodeCollector
//                    .submitModel(
//                            this.postModel.rightTank,
//                            emergencyPostRenderState,
//                            poseStack,
//                            TANK_TEXTURES,
//                            emergencyPostRenderState.lightCoords,
//                            OverlayTexture.NO_OVERLAY,
//                            0,
//                            null
//                    );
//            submitNodeCollector
//                    .submitModel(
//                            this.postModel.platform,
//                            emergencyPostRenderState,
//                            poseStack,
//                            BOX_TEXTURES,
//                            emergencyPostRenderState.lightCoords,
//                            OverlayTexture.NO_OVERLAY,
//                            0,
//                            null
//                    );
//            poseStack.popPose();
//        }

//        submitNodeCollector
//                .submitModel(
//                        this.postModel.northFlap,
//                        emergencyPostRenderState,
//                        poseStack,
//                        FLIP_TEXTURES,
//                        emergencyPostRenderState.lightCoords,
//                        OverlayTexture.NO_OVERLAY,
//                        0,
//                        null
//                );
//        submitNodeCollector
//                .submitModel(
//                        this.postModel.eastFlap,
//                        emergencyPostRenderState,
//                        poseStack,
//                        FLIP_TEXTURES,
//                        emergencyPostRenderState.lightCoords,
//                        OverlayTexture.NO_OVERLAY,
//                        0,
//                        null
//                );
//        submitNodeCollector
//                .submitModel(
//                        this.postModel.southFlap,
//                        emergencyPostRenderState,
//                        poseStack,
//                        FLIP_TEXTURES,
//                        emergencyPostRenderState.lightCoords,
//                        OverlayTexture.NO_OVERLAY,
//                        0,
//                        null
//                );
//        submitNodeCollector
//                .submitModel(
//                        this.postModel.westFlap,
//                        emergencyPostRenderState,
//                        poseStack,
//                        FLIP_TEXTURES,
//                        emergencyPostRenderState.lightCoords,
//                        OverlayTexture.NO_OVERLAY,
//                        0,
//                        null
//                );
        poseStack.popPose();
    }
}
