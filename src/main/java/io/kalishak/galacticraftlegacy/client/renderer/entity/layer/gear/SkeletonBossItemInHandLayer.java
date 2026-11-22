package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.entity.SkeletonBossModel;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.monster.SkeletonBossRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SkeletonBossItemInHandLayer extends ItemInHandLayer<SkeletonBossRenderState, SkeletonBossModel> {
    public SkeletonBossItemInHandLayer(RenderLayerParent<SkeletonBossRenderState, SkeletonBossModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkeletonBossRenderState state, float yRot, float xRot) {
        if (state.throwTimer + state.postThrowDelay == 0) {
            super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
        }
    }

    @Override
    protected void submitArmWithItem(SkeletonBossRenderState state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        if (!item.isEmpty()) {
            boolean isLeftHand = arm == HumanoidArm.LEFT;

            poseStack.pushPose();
            getParentModel().translateToHand(state, arm, poseStack);
            if (isLeftHand) {
                poseStack.translate(0.025F, 1.525F, -0.125F);
            } else {
                poseStack.rotateDegrees(Axis.XP, -90.0F);
                poseStack.rotateDegrees(Axis.YP, 180.0F);
                poseStack.translate(0.05F, 0.125F, -1.525F);
            }

            float ticksUsingItem = state.ticksUsingItem(arm);
            if (ticksUsingItem != 0.0F) {
                (arm == HumanoidArm.RIGHT ? state.rightArmPose : state.leftArmPose).animateUseItem(state, poseStack, ticksUsingItem, arm, itemStack);
            }

            item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
