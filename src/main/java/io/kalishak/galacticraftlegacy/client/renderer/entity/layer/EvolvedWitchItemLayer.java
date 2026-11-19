package io.kalishak.galacticraftlegacy.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.entity.EvolvedWitchModel;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.EvolvedWitchRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;

public class EvolvedWitchItemLayer extends CrossedArmsItemLayer<EvolvedWitchRenderState, EvolvedWitchModel> {
    public EvolvedWitchItemLayer(RenderLayerParent<EvolvedWitchRenderState, EvolvedWitchModel> renderer) {
        super(renderer);
    }

    @Override
    protected void applyTranslation(EvolvedWitchRenderState state, PoseStack poseStack) {
        if (state.isHoldingPotion) {
            getParentModel().root().translateAndRotate(poseStack);
            getParentModel().translateToHead(poseStack);
            getParentModel().getNose().translateAndRotate(poseStack);
            poseStack.translate(0.0625F, 0.25F, 0.0F);
            poseStack.rotateDegrees(Axis.ZP, 180.0F);
            poseStack.rotateDegrees(Axis.XP, 140.0F);
            poseStack.rotateDegrees(Axis.ZP, 10.0F);
            poseStack.rotateDegrees(Axis.XP, 180.0F);
        } else {
            super.applyTranslation(state, poseStack);
        }
    }
}
